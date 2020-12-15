package dtri.com.service;

import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dtri.com.bean.JsonObjBean;
import dtri.com.bean.JsonTemplateBean;
import dtri.com.db.entity.UserEntity;
import dtri.com.db.pgsql.dao.UserDao;
import dtri.com.models.Fm_Time_Model;
import dtri.com.models.JsonDataModel;
import dtri.com.models.MD5HashModel;

@Transactional
@Service
public class BoxBarcodeService {
	@Autowired
	private UserDao userDao;
	@Autowired
	private LoginService loginService;

	/**
	 * @param entity 查詢使用者資料
	 * @return 查詢後清單
	 * 
	 **/
	public UserEntity searchUser(UserEntity entity) {
		// 防止輸入為空 或 null

		// 顯示設定
		Boolean whoCheck = loginService.getSessionUserBean().getId() == 1;
		entity.setUseful(3);
		if (whoCheck) {
			entity.setUseful(0);
		}
		// 選項
		if (entity.getName() == null) {
			entity.setName("");
		}
		if (entity.getE_name() == null) {
			entity.setE_name("");
		}
		if (entity.getAccount() == null) {
			entity.setAccount("");
		}
		if (entity.getEmail() == null) {
			entity.setEmail("");
		}
		if (entity.getPosition() == null) {
			entity.setPosition("");
		}
		// 先刷新
		userDao.updateAll();
		UserEntity list = userDao.queryAll(entity).get(0);
		return list;

	}

	/** 更新 **/
	public Boolean updateUser(UserEntity entity) {
		Boolean check = false;
		userDao.updateOne(entity);
		return check;
	}

	/** JSON to 清單 **/
	public UserEntity jsonToEntities(JSONObject content) {
		MD5HashModel md5 = new MD5HashModel();
		UserEntity entity = new UserEntity();
		entity.setSys_create_date(new Date());
		entity.setSys_create_user(loginService.getSessionUserBean().getAccount());
		entity.setSys_modify_date(new Date());
		entity.setSys_modify_user(loginService.getSessionUserBean().getAccount());

		if (!content.isNull("id") && !content.get("id").equals(""))
			entity.setId(content.getInt("id"));
		if (!content.isNull("name"))
			entity.setName(content.getString("name"));
		if (!content.isNull("e_name"))
			entity.setE_name(content.getString("e_name"));
		if (!content.isNull("position"))
			entity.setPosition(content.getString("position"));

		if (!content.isNull("account"))
			entity.setAccount(content.getString("account"));
		if (!content.isNull("password"))
			entity.setPassword(md5.md5(content.getString("password")));

		if (!content.isNull("email"))
			entity.setEmail(content.getString("email"));
		if (!content.isNull("group_id") && !content.get("group_id").equals(""))
			entity.setGroup_id(content.getInt("group_id"));
		if (!content.isNull("group_name"))
			entity.setGroup_name(content.getString("group_name"));

		if (!content.isNull("useful") && !content.get("useful").equals(""))
			entity.setUseful(content.getInt("useful"));
		if (!content.isNull("note"))
			entity.setNote(content.getString("note"));
		return entity;
	}

	/**
	 * 清單 to JSON
	 * 
	 * @param p_Entities 使用者清單資料
	 **/
	public JSONObject entitiesToJson(UserEntity one) {
		JSONObject list = new JSONObject();
		// 標題
		JSONArray jsonArray = new JSONArray();
		// 內容
		jsonArray = new JSONArray();
		jsonArray.put(Fm_Time_Model.to_yMd_Hms(one.getSys_create_date()));
		jsonArray.put(one.getSys_create_user());
		jsonArray.put(Fm_Time_Model.to_yMd_Hms(one.getSys_modify_date()));
		jsonArray.put(one.getSys_modify_user());

		jsonArray.put(one.getId());
		jsonArray.put(one.getName());
		jsonArray.put(one.getE_name());
		jsonArray.put(one.getAccount());
		jsonArray.put(one.getPosition());
		jsonArray.put(one.getEmail());

		jsonArray.put(one.getGroup_id());
		jsonArray.put(one.getGroup_name());
		jsonArray.put(one.getUseful());
		jsonArray.put(one.getNote());

		list.put("only_user", jsonArray);
		return list;
	}

	/**
	 * 統整回傳包裝
	 * 
	 * @param p_Obj     回傳查詢資料
	 * @param frontData 前端傳入的"控制"資訊 回傳 成功
	 * @param r_Message 回傳訊息
	 **/
	public JSONObject ajaxRspJson(JSONObject p_Obj, JSONObject frontData, String r_Message) {
		JsonDataModel data = new JsonDataModel();
		JSONObject r_allData = new JSONObject();
		JsonObjBean objBean = new JsonObjBean();
		JsonTemplateBean templateBean = new JsonTemplateBean();

		templateBean.setWebPageBody("html/body/box_barcode_body.html");
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
	public JSONObject fail_ajaxRspJson(JSONObject frontData, String r_Message) {
		JsonDataModel data = new JsonDataModel();
		JSONObject r_allData = new JSONObject();
		JsonObjBean objBean = new JsonObjBean();
		JsonTemplateBean templateBean = new JsonTemplateBean();

		objBean.setR_cellBackName(frontData.getString("cellBackName"));
		objBean.setR_action(frontData.getString("action"));
		objBean.setR_status(false);
		objBean.setR_cellBackOrder(frontData.getString("cellBackOrder"));
		objBean.setR_message(r_Message);
		// Step5.包裝
		data.setR_JsonData(objBean);
		data.setR_template(templateBean);
		r_allData = data.getR_allData();
		return r_allData;
	}

}
