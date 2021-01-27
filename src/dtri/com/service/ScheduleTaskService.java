package dtri.com.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dtri.com.bean.FtpUtilBean;
import dtri.com.bean.SendMailBean;
import dtri.com.db.entity.ERP_PurchasingEntity;
import dtri.com.db.entity.PurchasingEntity;
import dtri.com.db.entity.PurchasingListEntity;
import dtri.com.db.entity.PurchasingMailEntity;
import dtri.com.db.entity.PurchasingSettingEntity;
import dtri.com.models.Fm_Time_Model;
import dtri.com.models.FtpUtilModel;
import dtri.com.models.MailModel;

@Transactional
@Service
public class ScheduleTaskService {

	// 取得 application.properties-backup
	@Value("${backup.local.path.folderName}")
	private String folderName;
	@Value("${backup.local.path.fileName}")
	private String fileName;
	@Value("${catalina.home}")
	private String apachePath;
	@Value("${backup.local.path.pg_dump}")
	private String pg_dump;
	@Value("${backup.local.dbName}")
	private String dbName;
	@Value("${backup.local.port}")
	private String port;

	// 取得 application.properties-ftp

	@Value("${ftp.ftpHost}")
	private String ftpHost;
	@Value("${ftp.ftpUserName}")
	private String ftpUserName;
	@Value("${ftp.ftpPassword}")
	private String ftpPassword;
	@Value("${ftp.ftpPort}")
	private String ftpPort;
	@Value("${ftp.ftpPath}")
	private String ftpPath;

	@Autowired
	private PurchasingSettingService ps_service;
	@Autowired
	private PurchasingService p_service;
	@Autowired
	private ERP_PurchasingListService erp_service;
	@Autowired
	private LoginService loginService;
	// log 訊息
	private static Logger logger = LogManager.getLogger();

	// mail 傳送
	// 周一到週五 07:00執行一次
	@Async
	@Scheduled(cron = "0 0 7 ? * MON-FRI")
	public void sendMailFromPurchasing() {
		System.out.println("每隔1天 早上7點0分 執行一次：" + new Date());
		logger.info("每隔1天 早上7點0分 執行一次：" + new Date());
		// Step 1.資料準備動作!!!! 取得寄件設定/內容樣本/ERP採購清單/已傳送過 採購清單
		// 建置設定參數(要傳給 發信SMTP)
		/**
		 * {setting1:SMTP設定資料 xxx, setting2:SMTP設定資料 xxx, content:[{採購資料:xxx,寄件資料,xxxx }
		 * ] }
		 **/
		JSONObject mail_Obj = new JSONObject();
		JSONArray mail_content_list = new JSONArray();
		// 寄件設定
		List<PurchasingSettingEntity> mail_settings = ps_service.searchSetting();
		for (PurchasingSettingEntity entity : mail_settings) {
			mail_Obj.put(entity.getSet_name(), entity.getSet_value());
		}

		// 內容樣本
		PurchasingMailEntity mail_content = ps_service.searchMailContent();
		mail_Obj.put("content_demo", mail_content.getMail_content());

		// 採購人員對應清單
		List<PurchasingEntity> mail_link = p_service.searchLink(new PurchasingEntity());

		// DTR 採購單發信紀錄
		PurchasingListEntity entity_DTR_ERP = new PurchasingListEntity();
		Integer start_date = mail_Obj.getInt("s_date");
		Integer end_date = mail_Obj.getInt("e_date");
		// 建立排除條建
		String s = Fm_Time_Model.to_yMd(Fm_Time_Model.to_count(start_date, new Date()));
		String e = Fm_Time_Model.to_yMd(Fm_Time_Model.to_count(end_date, new Date()));
		entity_DTR_ERP.setErp_s_date(s);
		entity_DTR_ERP.setErp_e_date(e);
		List<PurchasingListEntity> mail_DTR_list = ps_service.searchList(entity_DTR_ERP, 0, 5000);

		// ERP 採購單 -> 取得 頂新ERP
		ERP_PurchasingEntity entity_ERP = new ERP_PurchasingEntity();
		List<String> entity_ERP_List = new ArrayList<String>();
		// 建立排除條建
		entity_ERP.setS_TD012(s);
		entity_ERP.setE_TD012(e);
		for (PurchasingListEntity one : mail_DTR_list) {
			entity_ERP_List.add(one.getErp_order_id() + "-" + one.getErp_item_ns() + '-' + one.getErp_item_no());
		}
		entity_ERP.setE_list(entity_ERP_List);
		List<ERP_PurchasingEntity> mail_ERP_List = erp_service.searchERP_PurchasingList(entity_ERP);

		// Step2.清除過期紀錄
		ps_service.deletelistExpired();

		// Step3.建立信件
		StringBuffer mail_Obj_content = new StringBuffer();
		String table_s = "<table border=\"1\">";
		String table_e = "</tbody></table>";
		String tableheader = "<thead><tr>" + "<td>Order No.<br> 採購單號</td>" + "<td>Item No.<br> 品    號</td>"
				+ "<td>Part Name<br> 品    名</td>" + "<td>Delivery<br>Date<br> 預交日</td>" + "<td>Vendor<br> 廠商簡稱</td>"
				+ "<td>Q'ty<br> 採購數量</td>" + "<td>Not delivered <br> Q'ty<br> 未交數量</td>" + "</tr></thead><tbody>";
		StringBuffer tablebody = new StringBuffer();
		Map<Integer, StringBuffer> userId_tablebody = new HashMap<Integer, StringBuffer>();
		// 採購人區隔(必須是排序性質)
		Integer user_IdNext = 0;
		JSONObject obj = new JSONObject();
		JSONArray erp_order_item = new JSONArray();
		String mail_obj = "";
		// 採購關鍵字()
		PurchasingEntity nobody = new PurchasingEntity();
		nobody.setKey_word("nothing");
		// 添加最後一人方便迴圈運作
		mail_link.add(nobody);
		for (PurchasingEntity one : mail_link) {
			// 信件總內容
			mail_Obj_content = new StringBuffer();

			// 累加同一人 body 內容
			if (userId_tablebody.get(one.getUser_id()) != null) {
				userId_tablebody.put(one.getUser_id(), userId_tablebody.get(one.getUser_id()).append(tablebody));
			} else {
				// 如果 換下一位同仁 或是 最後一位同仁-> 先結算(封裝)
				// 進行串聯 說明+表格+結尾(取代 ${sendName} = 收件者(採購負責人) / ${itemList}=貨物項目清單 )
				if ((user_IdNext > 0 && user_IdNext != one.getUser_id())) {
					mail_obj = mail_content.getMail_content() + table_e;
					// 串好後 ->替代文字
					PurchasingEntity last_One = (PurchasingEntity) obj.get("mail_link");
					mail_obj = mail_obj.replace("${sendName}", last_One.getUser_name());
					mail_obj = mail_obj.replace("${itemList}", userId_tablebody.get(user_IdNext).toString());
					obj.put("mail_content", mail_obj);
					obj.put("erp_order_item", erp_order_item);
					mail_content_list.put(obj);
					// 重新初始化
					obj = new JSONObject();
					erp_order_item = new JSONArray();
				}

				// 過濾 (品號關鍵字->該負責人)
				for (ERP_PurchasingEntity erp_Entity : mail_ERP_List) {

					// 如果有關鍵字比對到
					// String pattern = one.getKey_word();
					// Boolean check_key_word = erp_Entity.getTD004().startsWith(pattern);

					// 如果 以人做比對
					String pattern = one.getUser_e_name();
					Boolean check_key_word = erp_Entity.getTC011().equals(pattern);
					if (check_key_word) {
						System.out.println(pattern + " VS " + erp_Entity.getTC011());
						erp_order_item.put(erp_Entity);
						tablebody.append("<tr>\r\n" + "<td>" + erp_Entity.getTC001_TC002() + "&ensp;-"
								+ erp_Entity.getTD003() + "</td>\r\n" + "<td>" + erp_Entity.getTD004() + "</td>\r\n"
								+ "<td>" + erp_Entity.getTD005() + "</td>\r\n" + "<td>" + erp_Entity.getTD012()
								+ "</td>\r\n" + "<td>" + erp_Entity.getMA002() + "</td>\r\n" + "<td>"
								+ erp_Entity.getTD008() + "</td>\r\n" + "<td>" + erp_Entity.getTD008_TD015()
								+ "</td>\r\n" + "</tr>\r\n");

					}
				}
				// 不同人 則重新 建置 tablebody
				obj.put("mail_link", one);
				user_IdNext = one.getUser_id();
				mail_Obj_content.append(table_s + tableheader + tablebody.toString());
				userId_tablebody.put(one.getUser_id(), mail_Obj_content);
				tablebody = new StringBuffer();
			}
		}

		// Step 4.寫入PurchasingList 紀錄
		// 每封信件
		for (Object one : mail_content_list) {
			List<PurchasingListEntity> p_entitys = new ArrayList<PurchasingListEntity>();
			JSONObject porch_obj = (JSONObject) one;
			PurchasingEntity one_link = (PurchasingEntity) porch_obj.get("mail_link");
			JSONArray list_erp = porch_obj.getJSONArray("erp_order_item");
			String one_mail = porch_obj.getString("mail_content");
			boolean checkAll = true;
			// 檢查
			if (one_link.getUser_email() == null || one_link.getUser_email().equals("")) {
				checkAll = false;
			}
			// 人為發送?自動發送
			String user = "admin";
			if (loginService.checkLogin()) {
				user = loginService.getSessionUserBean().getAccount();
			}
			// ERP 採購 項目清單
			if (list_erp.length() > 0) {
				for (Object one_erp : list_erp) {
					ERP_PurchasingEntity erp_p_e = (ERP_PurchasingEntity) one_erp;
					PurchasingListEntity p_noe = new PurchasingListEntity();
					p_noe.setSys_create_date(new Date());
					p_noe.setSys_create_user(user);
					p_noe.setSys_modify_date(new Date());
					p_noe.setSys_modify_user(user);

					p_noe.setErp_order_id(erp_p_e.getTC001_TC002());
					p_noe.setErp_item_ns(erp_p_e.getTD003());
					p_noe.setErp_item_no(erp_p_e.getTD004());
					p_noe.setErp_item_name(erp_p_e.getTD005());
					p_noe.setErp_item_nb(erp_p_e.getTD008());
					p_noe.setErp_in_date(erp_p_e.getTD012());
					p_noe.setErp_store_name(erp_p_e.getMA002());
					p_noe.setErp_store_email(erp_p_e.getMA011());
					p_noe.setUser_name(one_link.getUser_e_name());
					p_noe.setUser_mail(one_link.getUser_email());
					p_noe.setSys_type(checkAll);
					p_noe.setContent_id(0);
					p_noe.setSys_send_time(new Date());
					p_entitys.add(p_noe);
				}

				ps_service.addList(p_entitys);
				// Step 5. 傳送信件->逐筆傳送信件(給採購)
				SendMailBean setting_one = new SendMailBean();
				setting_one.setMail_content(one_mail);
				setting_one.setMail_title(mail_content.getMail_title());
				setting_one.setMail_who(one_link.getUser_email());
				setting_one.setSmtp_acc(mail_Obj.getString("s_mail"));
				setting_one.setSmtp_pass(mail_Obj.getString("s_password"));
				setting_one.setMail_cc(mail_Obj.getBoolean("cc_mail"));
				setting_one.setMail_title(setting_one.getMail_title() + "[日期:" + s + " to " + e + "]");
				MailModel.sendMail(setting_one);
				try {
					System.out.println("暫停一下");
					Thread.sleep(10000);
				} catch (InterruptedException e1) {

					e1.printStackTrace();
				}
				// Step 6. 信件紀錄
				System.out.println("mail (採購通知)傳送：" + mail_Obj.getString("s_mail") + " to " + one_link.getUser_email()
						+ " " + new Date());
				logger.info("mail (採購通知)傳送：" + mail_Obj.getString("s_mail") + " to " + one_link.getUser_email() + " "
						+ new Date());
			} else {
				System.out.println("mail (採購通知)無信件 傳送 :" + new Date());
				logger.info("mail (採購通知)無信件 傳送 ：" + new Date());
			}
		}

	}

	// 每日18:00分執行一次
	// 系統 備份(pgsql+ftp)
	@Async
	@Scheduled(cron = "0 30 18 ? * *")
	public void backupDataBase() {
		System.out.println("每隔1天 早上18點0分 執行一次：" + new Date());
		logger.info("每隔1天 早上18點0分 執行一次：" + new Date());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
		String backupDay = sdf.format(new Date());
		System.out.println("備份資料庫:" + new Date());
		logger.info("備份資料庫：" + new Date());

		//Runtime rt = Runtime.getRuntime();
		Process p;
		ProcessBuilder pb;
		//rt = Runtime.getRuntime();
		// 備份指令-postgres
		pb = new ProcessBuilder("" + pg_dump, "--dbname=" + dbName, "--port=" + port, "--verbose", "--format=p",
				"--clean", "--section=pre-data", "--section=data", "--section=post-data", "--no-privileges",
				"--no-tablespaces", "--no-unlogged-table-data", "--inserts", "--encoding=UTF8",
				"--file=" + apachePath + folderName + fileName + "_" + backupDay + ".sql");
		try {
			// final Map<String, String> env = pb.environment();
			// env.put("PGPASSWORD", "admin");
			p = pb.start();
			final BufferedReader r = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			String line = r.readLine();
			while (line != null) {
				System.err.println(line);
				logger.info(line);
				line = r.readLine();
			}
			r.close();
			p.waitFor();
			System.out.println(p.exitValue());
			logger.info(p.exitValue());
		} catch (IOException | InterruptedException e) {
			logger.error(e.getMessage());
			System.out.println(e.getMessage());
		}
		// 上傳-FTP
		try {
			File initialFile = new File(apachePath + folderName + fileName + "_" + backupDay + ".sql");
			InputStream input = new FileInputStream(initialFile);
			FtpUtilBean f_Bean = new FtpUtilBean(ftpHost, ftpUserName, ftpPassword, Integer.parseInt(ftpPort));
			f_Bean.setInput(input);
			f_Bean.setFtpPath(ftpPath);
			f_Bean.setFileName(fileName + "_" + backupDay + ".sql");
			FtpUtilModel.uploadFile(f_Bean);
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
}
