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
import dtri.com.db.entity.GroupEntity;
import dtri.com.db.entity.UserEntity;
import dtri.com.db.pgsql.dao.UserDao;
import dtri.com.models.Fm_Time_Model;
import dtri.com.models.JsonDataModel;
import dtri.com.models.MD5HashModel;

@Transactional
@Service
public class UserService {
	@Autowired
	private UserDao userDao;
	@Autowired
	private LoginService loginService;
	@Autowired
	private GroupService groupService;

	/**
	 * @param entity 查詢使用者資料
	 * @return 查詢後清單
	 * 
	 **/
	public List<UserEntity> searchUser(UserEntity entity) {
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
		List<UserEntity> list = userDao.queryAll(entity);
		return list;
	}

	/** 新增 **/
	public Boolean addedUser(UserEntity entity) {
		Boolean check = false;
		userDao.addedOne(entity);
		return check;
	}

	/** 更新 **/
	public Boolean updateUser(UserEntity entity) {
		Boolean check = false;
		userDao.updateOne(entity);
		return check;
	}

	/** 移除 **/
	public Boolean deleteUser(UserEntity entity) {
		Boolean check = false;
		userDao.deleteOne(entity);
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
	public JSONObject entitiesToJson(List<UserEntity> p_Entities) {
		JSONObject list = new JSONObject();
		JSONArray jsonAll = new JSONArray();
		JSONObject grouplist = new JSONObject();
		// 標題
		JSONArray jsonArray = new JSONArray();
		jsonArray.put("建立時間");
		jsonArray.put("建立者");
		jsonArray.put("修改時間");
		jsonArray.put("修改者");

		jsonArray.put("使用者ID");
		jsonArray.put("使用者名稱");
		jsonArray.put("英文名稱");
		jsonArray.put("帳號");
		jsonArray.put("職位");
		jsonArray.put("電子信箱");

		jsonArray.put("群組ID");
		jsonArray.put("群組名稱");
		jsonArray.put("使用狀態");
		jsonArray.put("備註");
		jsonAll.put(jsonArray);
		// 內容
		for (UserEntity entity : p_Entities) {
			jsonArray = new JSONArray();
			jsonArray.put(Fm_Time_Model.to_yMd_Hms(entity.getSys_create_date()));
			jsonArray.put(entity.getSys_create_user());
			jsonArray.put(Fm_Time_Model.to_yMd_Hms(entity.getSys_modify_date()));
			jsonArray.put(entity.getSys_modify_user());

			jsonArray.put(entity.getId());
			jsonArray.put(entity.getName());
			jsonArray.put(entity.getE_name());
			jsonArray.put(entity.getAccount());
			jsonArray.put(entity.getPosition());
			jsonArray.put(entity.getEmail());

			jsonArray.put(entity.getGroup_id());
			jsonArray.put(entity.getGroup_name());
			jsonArray.put(entity.getUseful());
			jsonArray.put(entity.getNote());
			jsonAll.put(jsonArray);
		}
		list.put("list", jsonAll);
		List<GroupEntity> entities = groupService.searchGroup(new GroupEntity());
		// 群組清單
		for (GroupEntity groupEntity : entities) {
			grouplist.put(groupEntity.getName(), groupEntity.getId());
		}
		list.put("grouplist", grouplist);
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

		templateBean.setWebPageBody("html/body/sys_user_body.html");
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
