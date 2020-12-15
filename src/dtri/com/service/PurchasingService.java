package dtri.com.service;

import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dtri.com.bean.JsonObjBean;
import dtri.com.bean.JsonTemplateBean;
import dtri.com.db.entity.PurchasingEntity;
import dtri.com.db.entity.UserEntity;
import dtri.com.db.pgsql.dao.PurchasingDao;
import dtri.com.models.JsonDataModel;

@Transactional
@Service
public class PurchasingService {
	@Autowired
	private PurchasingDao p_Dao;

	@Autowired
	private LoginService loginService;

	/**
	 * @param entity
	 * @return 查詢後清單
	 * 
	 **/
	public List<PurchasingEntity> searchLink(PurchasingEntity entity) {
		// 條件性查詢 需自動發信清單
		if (entity.getKey_item_word() == null || entity.getKey_item_word().equals("")) {
			entity.setKey_item_word("");
		}
		if (entity.getKey_word() == null || entity.getKey_word().equals("")) {
			entity.setKey_word("");
		}
		if (entity.getUser_name() == null || entity.getUser_name().equals("")) {
			entity.setUser_name("");
		}

		// 後讀取
		List<PurchasingEntity> list = p_Dao.queryAll(entity);
		return list;
	}

	/**
	 * 新增
	 * 
	 * @param entity 新增內容 條件
	 * 
	 **/
	public Boolean addedPurchasingEntity(PurchasingEntity entity) {
		Boolean check = false;

		p_Dao.addedOne(entity);
		check = true;
		return check;
	}

	/** 更新 **/
	public Boolean updatePurchasingEntity(PurchasingEntity entity) {
		Boolean check = false;
		// 需要更新的
		if (p_Dao.updateOne(entity) > 0) {
			check = true;
		}
		return check;
	}

	/**
	 * 
	 * 移除關聯設定
	 **/
	public Boolean deletePurchasingEntity(PurchasingEntity entity) {
		Boolean check = false;
		p_Dao.deleteOne(entity);
		check = true;
		return check;

	}

	/** JSON to 清單 (單數 ) **/
	public PurchasingEntity jsonToEntities(JSONObject content) {
		PurchasingEntity entity = new PurchasingEntity();
		entity.setSys_create_date(new Date());
		entity.setSys_create_user(loginService.getSessionUserBean().getAccount());
		entity.setSys_modify_date(new Date());
		entity.setSys_modify_user(loginService.getSessionUserBean().getAccount());
		entity.setUseful(1);

		if (!content.isNull("id") && !content.get("id").equals(""))
			entity.setId(content.getInt("id"));
		if (!content.isNull("key_item_word"))
			entity.setKey_item_word(content.getString("key_item_word"));
		if (!content.isNull("key_word"))
			entity.setKey_word(content.getString("key_word"));
		if (!content.isNull("note") && !content.get("note").equals(""))
			entity.setNote(content.getString("note"));

		if (!content.isNull("user_id") && !content.get("user_id").equals(""))
			entity.setUser_id(content.getInt("user_id"));
		if (!content.isNull("name"))
			entity.setUser_name(content.getString("name"));
		if (!content.isNull("e_name"))
			entity.setUser_e_name(content.getString("e_name"));
		if (!content.isNull("user_email") && !content.get("user_email").equals(""))
			entity.setUser_email(content.getString("user_email"));

		return entity;
	}

	/** 關鍵字與採購 清單 to JSON **/
	public JSONObject entitiesToJson(List<PurchasingEntity> p_Entities, List<UserEntity> u_Entities) {
		JSONObject jsonAll = new JSONObject();
		JSONArray link_item = new JSONArray();
		JSONArray link_list = new JSONArray();
		JSONArray user_list = new JSONArray();

		// 標題
		JSONArray jsonArray = new JSONArray();
		jsonArray.put("項目ID");
		jsonArray.put("負責人ID");
		jsonArray.put("負責人-中");
		jsonArray.put("負責人-英");
		jsonArray.put("Email");
		jsonArray.put("料號關鍵字");
		jsonArray.put("品號關鍵字");
		jsonArray.put("備註");
		jsonArray.put("建立時間");
		jsonArray.put("建立者");
		jsonArray.put("修改時間");
		jsonArray.put("修改者");
		link_item.put(jsonArray);
		// 內容
		for (PurchasingEntity entity : p_Entities) {
			jsonArray = new JSONArray();
			jsonArray.put(entity.getId());
			jsonArray.put(entity.getUser_id());
			jsonArray.put(entity.getUser_name());
			jsonArray.put(entity.getUser_e_name());
			jsonArray.put(entity.getUser_email());
			jsonArray.put(entity.getKey_word());
			jsonArray.put(entity.getKey_item_word());
			jsonArray.put(entity.getNote());
			jsonArray.put(entity.getSys_create_date());
			jsonArray.put(entity.getSys_create_user());
			jsonArray.put(entity.getSys_modify_date());
			jsonArray.put(entity.getSys_modify_user());
			link_list.put(jsonArray);
		}
		for (UserEntity entity : u_Entities) {
			jsonArray = new JSONArray();
			jsonArray.put(entity.getId());
			jsonArray.put(entity.getName());
			jsonArray.put(entity.getE_name());
			jsonArray.put(entity.getEmail());
			user_list.put(jsonArray);
		}
		jsonAll.put("link_item", link_item);
		jsonAll.put("link_list", link_list);
		jsonAll.put("user_list", user_list);
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

		templateBean.setWebPageBody("html/body/purchasing_link_body.html");
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

}
