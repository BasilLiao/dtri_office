package dtri.com.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import dtri.com.bean.BomProductGroupBean;
import dtri.com.db.entity.BomProductEntity;
import dtri.com.db.entity.GroupEntity;
import dtri.com.models.JsonDataModel;
import dtri.com.service.BomPrintService;
import dtri.com.service.LoginService;
import dtri.com.service.ProductionRecordsService;

@Controller
public class BomPrintController {

	@Autowired
	LoginService loginService;
	@Autowired
	BomPrintService printService;
	@Autowired
	ProductionRecordsService recordsService;
	
	// 功能
	final static String SYS_F = "bom_print.do";
	
	// log 訊息
	Logger logger = LogManager.getLogger();

	/**
	 * 
	 * 基本載入 /查詢
	 * 
	 * @param ajaxJSON 限定用JSON
	 */
	@ResponseBody
	@RequestMapping(value = "/bom_print", method = { RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String search_bom_product(@RequestBody String ajaxJSON) {

		System.out.println("---controller - bom_print");
		// Step1.取出 session 訊息 & 檢查權限
		List<GroupEntity> group = loginService.getSessionGroupBean();
		boolean checkPermission = loginService.checkPermission(group, SYS_F, "01000001");

		// Step2.解析內容-檢查 -> 取出內容物
		JsonDataModel data = new JsonDataModel();
		JSONObject frontData = (JSONObject) data.frontToBack(ajaxJSON).get("data");
		System.out.println(frontData);

		// Step3.建立回傳元素
		JSONObject r_allData = new JSONObject();
		int page_nb = 0, page_total = 100;
		// Step4.檢查許可權 & 輸入物件
		if (checkPermission) {
			// Step4-1 .DB 取出 正確 資料
			List<BomProductEntity> entity = new ArrayList<BomProductEntity>();
			BomProductGroupBean bpg = new BomProductGroupBean();
			if (frontData.get("content") != null && !frontData.get("content").equals("")) {
				entity = printService.jsonToEntities(frontData.getJSONObject("content"), frontData.getString("action"));
				// 取得換頁碼 如果沒有 0
				page_nb = printService.jsonToPageNb(frontData.getJSONObject("content"));
				page_total = printService.jsonToPageTotal(frontData.getJSONObject("content"));
			}
			bpg = printService.search(entity, page_nb, page_total);
			JSONObject p_Obj = printService.entitiesToJson(bpg);

			// Step4-2 .包裝資料
			r_allData = printService.ajaxRspJson(p_Obj, frontData, "訪問成功!!");
		} else {
			// Step4-1 .登出 && 包裝 錯誤 資料
			r_allData = printService.fail_ajaxRspJson(frontData, "你沒有權限!!");
		}

		// Step6.結果回傳
		// System.out.println(r_allData);
		return r_allData.toString();
	}


	/**
	 * 
	 * 修正 單據類型 (量產中/技轉中)
	 * 
	 **/
	@ResponseBody
	@RequestMapping(value = "/bom_product_kind", method = {
			RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String bom_product_kind(@RequestBody String ajaxJSON) {
		System.out.println("---controller - bom_product_kind");

		// Step1.取出 session 訊息 & 檢查權限
		List<GroupEntity> group = loginService.getSessionGroupBean();
		boolean checkPermission = loginService.checkPermission(group, SYS_F, "01000101");

		// Step2.解析內容-檢查 -> 取出內容物
		JsonDataModel data = new JsonDataModel();
		JSONObject frontData = (JSONObject) data.frontToBack(ajaxJSON).get("data");
		System.out.println(frontData);

		// Step3.建立回傳元素
		JSONObject r_allData = new JSONObject();
		int page_nb = 0, page_total = 100;

		// Step4.檢查許可權 & 輸入物件
		if (checkPermission) {
			// Step4-1 .DB 取出 正確 資料
			List<BomProductEntity> entity = new ArrayList<BomProductEntity>();
			BomProductGroupBean bpg = new BomProductGroupBean();
			if (frontData.getJSONObject("content") != null
					&& frontData.getJSONObject("content").getInt("product_id") > 0) {
				JSONObject json = frontData.getJSONObject("content");
				printService.productkind(json.getInt("product_id"), json.getInt("product_kind"));
			}
			entity = printService.jsonToEntities(frontData.getJSONObject("content"), frontData.getString("action"));
			// 取得換頁碼 如果沒有 0
			page_nb = printService.jsonToPageNb(frontData.getJSONObject("content"));
			page_total = printService.jsonToPageTotal(frontData.getJSONObject("content"));

			bpg = printService.search(entity, page_nb, page_total);
			JSONObject p_Obj = printService.entitiesToJson(bpg);
			// Step4-2 .包裝資料
			r_allData = printService.ajaxRspJson(p_Obj, frontData, "訪問成功!!");
		} else {
			// Step4-1 .登出 && 包裝 錯誤 資料
			r_allData = printService.fail_ajaxRspJson(frontData, "你沒有權限!!");
		}

		// Step6.結果回傳
		// System.out.println(r_allData);
		return r_allData.toString();
	}
	
	/**
	 * 
	 * 檢查 && 建立生產列表
	 * 
	 **/
	@ResponseBody
	@RequestMapping(value = "/production_records_add", method = {
			RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String production_print_check(@RequestBody String ajaxJSON) {
		System.out.println("---controller - production_records_add");

		// Step1.取出 session 訊息 & 檢查權限
		List<GroupEntity> group = loginService.getSessionGroupBean();
		boolean checkPermission = loginService.checkPermission(group, SYS_F, "01001001");

		// Step2.解析內容-檢查 -> 取出內容物
		JsonDataModel data = new JsonDataModel();
		JSONObject frontData = (JSONObject) data.frontToBack(ajaxJSON).get("data");
		System.out.println(frontData);

		boolean checked = false;
		// Step3.建立回傳元素
		JSONObject r_allData = new JSONObject();
		int page_nb = 0, page_total = 100;

		// Step4.檢查許可權 & 輸入物件
		if (checkPermission) {
			// Step4-1 .DB 取出 正確 資料
			List<BomProductEntity> entity = new ArrayList<BomProductEntity>();
			BomProductGroupBean bpg = new BomProductGroupBean();
			if (frontData.getJSONObject("content") != null
					&& frontData.getJSONObject("content").getInt("product_id") > 0) {
				// 取得換頁碼 如果沒有 0
				page_nb = printService.jsonToPageNb(frontData.getJSONObject("content"));
				page_total = printService.jsonToPageTotal(frontData.getJSONObject("content"));
				
				// 標記用過產品(printService)
				int id = frontData.getJSONObject("content").getInt("product_id");
				String name = frontData.getJSONObject("content").getString("user");
				printService.checked(id, name);
				
				// 建立生產紀錄(recordsService)
				checked = recordsService.addRecords(frontData.getJSONObject("content"),1);
				//重新查詢
				entity = printService.jsonToEntities(frontData.getJSONObject("content"), frontData.getString("action"));
			}
			bpg = printService.search(entity, page_nb, page_total);
			JSONObject p_Obj = printService.entitiesToJson(bpg);
			// Step4-2 .包裝資料(如果有登記過了)
			if (checked) {
				r_allData = printService.ajaxRspJson(p_Obj, frontData, "訪問成功!!");
			} else {
				r_allData = printService.fail_ajaxRspJson(frontData, "你登記生的工單號已使用!!");
			}

		} else {
			// Step4-1 .登出 && 包裝 錯誤 資料
			r_allData = printService.fail_ajaxRspJson(frontData, "你沒有權限!!");
		}

		// Step6.結果回傳
		// System.out.println(r_allData);
		return r_allData.toString();
	}
}
