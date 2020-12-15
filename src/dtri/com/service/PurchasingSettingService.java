package dtri.com.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dtri.com.bean.JsonObjBean;
import dtri.com.bean.JsonTemplateBean;
import dtri.com.db.entity.PurchasingListEntity;
import dtri.com.db.entity.PurchasingMailEntity;
import dtri.com.db.entity.PurchasingSettingEntity;
import dtri.com.db.pgsql.dao.PurchasingListSettingDao;
import dtri.com.models.Fm_Time_Model;
import dtri.com.models.JsonDataModel;

@Transactional
@Service
public class PurchasingSettingService {
	@Autowired
	private PurchasingListSettingDao p_Dao;

	@Autowired
	private LoginService loginService;

	/**
	 * @param entity
	 * @return 查詢寄信後清單
	 * 
	 **/
	public List<PurchasingListEntity> searchList(PurchasingListEntity entity, int offset, int page_total) {

		if (entity.getErp_item_name() == null || entity.getErp_item_name().equals("")) {
			entity.setErp_item_name("");
		}
		if (entity.getErp_item_no() == null || entity.getErp_item_no().equals("")) {
			entity.setErp_item_no("");
		}
		if (entity.getUser_name() == null || entity.getUser_name().equals("")) {
			entity.setUser_name("");
		}
		// 後讀取
		String all_limit = " OFFSET " + offset + " LIMIT " + page_total;
		entity.setPurchasing_limit(all_limit);

		List<PurchasingListEntity> list = p_Dao.queryAll(entity);
		return list;
	}

	/** 新增紀錄 **/
	public Boolean addList(List<PurchasingListEntity> entitys) {
		Boolean check = false;
		for (PurchasingListEntity entity : entitys) {
			p_Dao.AddedOne(entity);
		}
		check = true;
		return check;
	}

	/** 移除過期紀錄 **/
	public Boolean deletelistExpired() {
		Boolean check = false;
		PurchasingListEntity entity = new PurchasingListEntity();
		entity.setErp_in_date(Fm_Time_Model.to_yMd(new Date()));
		if (p_Dao.deleteExpired(entity) > 0) {
			check = true;
		}
		return check;
	}

	/**
	 * @param entity
	 * @return 取得寄信設定
	 * 
	 **/
	public List<PurchasingSettingEntity> searchSetting() {
		List<PurchasingSettingEntity> list = p_Dao.querySetting();
		return list;
	}

	/**
	 * @param entity
	 * @return 取得寄信內容
	 * 
	 **/
	public PurchasingMailEntity searchMailContent() {
		PurchasingMailEntity content = p_Dao.queryMail();
		return content;
	}

	/** 更新寄件 設定 **/
	public Boolean updateP_Setting(JSONObject entity) {
		Boolean check = false;
		PurchasingMailEntity m_e = (PurchasingMailEntity) entity.get("PurchasingMail");
		@SuppressWarnings("unchecked")
		List<PurchasingSettingEntity> s_e = (List<PurchasingSettingEntity>) entity.get("PurchasingSetting");
		for (PurchasingSettingEntity one : s_e) {
			if (p_Dao.update_s_One(one) > 0) {
				check = true;
			}
		}
		// 需要更新的
		if (p_Dao.update_m_One(m_e) > 0) {
			check = true;
		}
		return check;
	}

	/** JSON to 清單 (單數 ) **/
	public JSONObject jsonToEntities(JSONObject content) {
		JSONObject search = new JSONObject();
		JSONObject setting = new JSONObject();
		JSONObject mail_content = new JSONObject();
		JSONObject returns = new JSONObject();
		// 設定寄件 && 信件模板

		List<PurchasingSettingEntity> entitys = new ArrayList<PurchasingSettingEntity>();
		PurchasingListEntity list = new PurchasingListEntity();
		PurchasingMailEntity mail = new PurchasingMailEntity();
		// 設定
		if (!content.isNull("setting") && !content.isNull("mail_content")) {
			setting = content.getJSONObject("setting");
			mail_content = content.getJSONObject("mail_content");
			Iterator<String> key_value = setting.keys();
			while (key_value.hasNext()) {
				PurchasingSettingEntity entity = new PurchasingSettingEntity();
				String key = key_value.next();
				Object value = setting.get(key);
				entity.setSys_create_date(new Date());
				entity.setSys_create_user(loginService.getSessionUserBean().getAccount());
				entity.setSys_modify_date(new Date());
				entity.setSys_modify_user(loginService.getSessionUserBean().getAccount());
				entity.setSet_name(key);
				entity.setSet_value(value + "");
				entity.setSet_type(1);
				entity.setUseful(1);
				entitys.add(entity);
			}
			mail.setSys_modify_date(new Date());
			mail.setSys_modify_user(loginService.getSessionUserBean().getAccount());
			mail.setId(mail_content.getInt("id"));
			mail.setMail_content(mail_content.getString("content"));
			returns.put("PurchasingMail", mail);
			Object settings = entitys;
			returns.put("PurchasingSetting", settings);
		}
		// 查詢
		if (!content.isNull("search")) {
			search = content.getJSONObject("search");
			list.setErp_item_name(search.getString("erp_item_name"));
			list.setErp_item_no(search.getString("erp_item_no"));
			list.setUser_name(search.getString("user_name"));
			returns.put("PurchasingList", list);
		}
		return returns;
	}

	/** 關鍵字與採購 清單 to JSON **/
	public JSONObject entitiesToJson(List<PurchasingListEntity> l_Entities, List<PurchasingSettingEntity> s_Entities,
			PurchasingMailEntity m_Entities) {
		JSONObject jsonAll = new JSONObject();

		JSONArray list_order = new JSONArray();
		JSONArray list_item = new JSONArray();

		JSONObject content = new JSONObject();
		JSONObject setting = new JSONObject();

		// 標題
		JSONArray jsonArray = new JSONArray();
		jsonArray.put("ID");
		jsonArray.put("採購單ID");
		jsonArray.put("料號");
		jsonArray.put("品號");
		jsonArray.put("數量");

		jsonArray.put("寄件?");
		jsonArray.put("廠商名稱");
		jsonArray.put("廠商mail");
		jsonArray.put("負責人");
		jsonArray.put("採購mail");

		jsonArray.put("進貨日");
		jsonArray.put("信件關聯");

		jsonArray.put("建立時間");
		jsonArray.put("建立者");
		jsonArray.put("修改時間");
		jsonArray.put("修改者");
		list_item.put(jsonArray);
		// 內容採購清單
		for (PurchasingListEntity entity : l_Entities) {
			jsonArray = new JSONArray();
			jsonArray.put(entity.getId());
			jsonArray.put(entity.getErp_order_id());
			jsonArray.put(entity.getErp_item_no());
			jsonArray.put(entity.getErp_item_name());
			jsonArray.put(entity.getErp_item_nb());

			jsonArray.put(entity.getSys_type());
			jsonArray.put(entity.getErp_store_name());
			jsonArray.put(entity.getErp_store_email());
			jsonArray.put(entity.getUser_name());
			jsonArray.put(entity.getUser_mail());

			jsonArray.put(entity.getErp_in_date());
			jsonArray.put(entity.getContent_id());

			jsonArray.put(Fm_Time_Model.to_yMd_Hms(entity.getSys_create_date()));
			jsonArray.put(entity.getSys_create_user());
			jsonArray.put(Fm_Time_Model.to_yMd_Hms(entity.getSys_modify_date()));
			jsonArray.put(entity.getSys_modify_user());
			list_order.put(jsonArray);
		}
		// 設定參數
		for (PurchasingSettingEntity entity : s_Entities) {
			setting.put(entity.getSet_name(), entity.getSet_value());
		}
		// 內容

		content.put("id", m_Entities.getId());
		content.put("content", m_Entities.getMail_content());
		content.put("modify_date", Fm_Time_Model.to_yMd_Hms(m_Entities.getSys_modify_date()));
		content.put("modify_user", m_Entities.getSys_modify_user());

		jsonAll.put("mail_setting", setting);
		jsonAll.put("mail_content", content);
		jsonAll.put("list_order", list_order);
		jsonAll.put("list_item", list_item);

		return jsonAll;
	}

	/**
	 * 統整回傳包裝
	 * 
	 * @param p_Obj     回傳查詢資料
	 * @param frontData 前端傳入的"控制"資訊 回傳 成功
	 **/
	public JSONObject ajaxRspJson(JSONObject p_Obj, JSONObject frontData, String r_Message) {
		JsonDataModel data = new JsonDataModel();
		JSONObject r_allData = new JSONObject();
		JsonObjBean objBean = new JsonObjBean();
		JsonTemplateBean templateBean = new JsonTemplateBean();

		templateBean.setWebPageBody("html/body/purchasing_setting_body.html");
		templateBean.setBodyData(p_Obj);

		objBean.setR_cellBackName(frontData.getString("cellBackName"));
		objBean.setR_action(frontData.getString("action"));
		objBean.setR_status(true);
		objBean.setR_cellBackOrder(frontData.getString("cellBackOrder"));
		objBean.setR_message(r_Message);

		data.setR_JsonData(objBean);
		data.setR_template(templateBean);
		r_allData = data.getR_allData();
		return r_allData;
	}

	/**
	 * 統整回傳包裝
	 * 
	 * @param p_Obj     回傳查詢資料
	 * @param frontData 前端傳入的"控制"資訊 回傳 成功
	 **/
	public JSONObject fail_ajaxRspJson(JSONObject frontData) {
		JsonDataModel data = new JsonDataModel();
		JSONObject r_allData = new JSONObject();
		JsonObjBean objBean = new JsonObjBean();
		JsonTemplateBean templateBean = new JsonTemplateBean();

		objBean.setR_cellBackName(frontData.getString("cellBackName"));
		objBean.setR_action(frontData.getString("action"));
		objBean.setR_status(false);
		objBean.setR_cellBackOrder(frontData.getString("cellBackOrder"));
		objBean.setR_message("你沒有權限!!");
		// Step5.包裝
		data.setR_JsonData(objBean);
		data.setR_template(templateBean);
		r_allData = data.getR_allData();
		return r_allData;
	}

	/** 檢查換頁碼 如果沒有固定為從0開始 **/
	public int jsonToPageNb(JSONObject content) {
		if (!content.isNull("page_nb") && !content.get("page_nb").equals("")) {
			return content.getInt("page_nb");
		}
		return 0;
	}

	/** 檢查換頁碼 如果沒有固定為100筆資料 **/
	public int jsonToPageTotal(JSONObject content) {
		if (!content.isNull("page_total") && !content.get("page_total").equals("")) {
			return content.getInt("page_total");
		}
		return 100;
	}
}
