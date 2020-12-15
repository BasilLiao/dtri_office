package dtri.com.service;

import java.util.ArrayList;
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
import dtri.com.db.entity.PermissionEntity;
import dtri.com.db.pgsql.dao.GroupDao;
import dtri.com.models.Fm_Time_Model;
import dtri.com.models.JsonDataModel;

@Transactional
@Service
public class GroupService {
	@Autowired
	private GroupDao groupDao;
	@Autowired
	private LoginService loginService;

	/**
	 * @param entity 
	
	 * @return 查詢後清單
	 * 
	 **/
	public List<GroupEntity> searchGroup(GroupEntity entity) {
		// who 顯示內內容限制 除了 true = admin(ID 固定1)/ false = 一般查詢
		Boolean whoCheck = loginService.getSessionUserBean().getId()==1;
		Integer useful= 3;
		if(whoCheck) {
			useful = 0;
		}
		
		// 防止輸入為空 或 null
		String checkName = "LIKE";
		if (entity.getName() == null || entity.getName().equals("")) {
			entity.setName(" ");
			checkName = "NOT LIKE";
		}

		//後讀取
		List<GroupEntity> list = groupDao.queryAll(checkName, "%" + entity.getName() + "%",useful);
		return list;
	}

	/**
	 * 新增
	 * 
	 * @param entitys 要新增的群組 資訊
	 * @param id      =0 單存新增群組 / id >0 指定新增群組 內權限
	 * 
	 **/
	public Boolean addedGroup(List<GroupEntity> entitys, int id) {
		Boolean check = false;
		for (GroupEntity entity : entitys) {
			// 如果ID 是 0 是新增群組 /反之 新增權限 
			if (entity.getId() == 0 && id == 0) {
				//新增 不可與作業系統 admin 名稱一致
				if (entity.getName().equals("admin")) {
					continue;
				}
				id = groupDao.nextvalGroup();
			}
			entity.setId(id);
			groupDao.addedOne(entity);
		}
		return check;
	}
	
	/** 更新 **/
	public Boolean updateGroup(List<GroupEntity> entitys) {
		Boolean check = false;
		List<GroupEntity> entities = new ArrayList<GroupEntity>();
		for (GroupEntity entity : entitys) {
			// 需要更新的 權限
			int checkNb = groupDao.updateOne(entity);
			// 需要新增的 權限
			if (checkNb == 0) {
				entities.add(entity);
			}
			// 需要移除的 權限
			if (entity.getPermission_type().equals("D")) {
				deleteGroup(entity, false);
			}
		}
		if (entities.size() > 0) {
			addedGroup(entities, entities.get(0).getId());
		}
		return check;
	}

	/**
	 * 移除群組 or 移除群組內的權限
	 * 
	 * @param entity  資料
	 * @param isGroup 是否為移除整個群組
	 **/
	public Boolean deleteGroup(GroupEntity entity, boolean isGroup) {
		Boolean check = false;
		if (isGroup) {
			groupDao.deleteGroupOne(entity);
		} else {
			groupDao.deletePermissionOne(entity);
		}
		return check;

	}

	/** JSON to 清單 (單數 ) **/
	public GroupEntity jsonToEntities(JSONObject content) {
		GroupEntity entity = new GroupEntity();
		entity.setSys_create_date(new Date());
		entity.setSys_create_user(loginService.getSessionUserBean().getAccount());
		entity.setSys_modify_date(new Date());
		entity.setSys_modify_user(loginService.getSessionUserBean().getAccount());

		if (!content.isNull("id") && !content.get("id").equals(""))
			entity.setId(content.getInt("id"));
		if (!content.isNull("name"))
			entity.setName(content.getString("name"));
		if (!content.isNull("permission_type"))
			entity.setPermission_type(content.getString("permission_type"));
		if (!content.isNull("permission_id") && !content.get("permission_id").equals(""))
			entity.setPermission_id(content.getInt("permission_id"));
		if (!content.isNull("permission_group"))
			entity.setPermission_group(content.getString("permission_group"));
		if (!content.isNull("permission_control"))
			entity.setPermission_control(content.getString("permission_control"));
		if (!content.isNull("permission"))
			entity.setPermission(content.getString("permission"));
		if (!content.isNull("useful") && !content.get("useful").equals(""))
			entity.setUseful(content.getInt("useful"));
		if (!content.isNull("note"))
			entity.setNote(content.getString("note"));
		if (!content.isNull("nbdesc"))
			entity.setNote(content.getString("nbdesc"));
		return entity;
	}

	/** JSON to 清單 (複數 ) **/
	public List<GroupEntity> jsonToEntitiesN(JSONArray content) {
		List<GroupEntity> entity = new ArrayList<GroupEntity>();
		for (int one = 0; one < content.length(); one++) {
			GroupEntity entityOne = new GroupEntity();
			JSONObject oneJson = content.getJSONObject(one);
			entityOne.setSys_create_date(new Date());
			entityOne.setSys_create_user(loginService.getSessionUserBean().getAccount());
			entityOne.setSys_modify_date(new Date());
			entityOne.setSys_modify_user(loginService.getSessionUserBean().getAccount());

			entityOne.setId(oneJson.getInt("id"));
			entityOne.setName(oneJson.getString("name"));
			entityOne.setNote(oneJson.getString("note"));
			entityOne.setUseful(oneJson.getInt("useful"));
			entityOne.setPermission(oneJson.getString("permission"));
			entityOne.setPermission_control(oneJson.getString("permission_control"));
			entityOne.setPermission_group(oneJson.getString("permission_group"));
			entityOne.setPermission_id(oneJson.getInt("permission_id"));
			entityOne.setPermission_name(oneJson.getString("permission_name"));
			entityOne.setPermission_type("G");
			entityOne.setNbdesc(oneJson.getInt("nbdesc"));
			// 移除標記
			if (!oneJson.isNull("rm_id") && oneJson.getBoolean("rm_id"))
				entityOne.setPermission_type("D");

			entity.add(entityOne);
		}

		return entity;
	}

	/** 群組 清單 to JSON **/
	public JSONObject entitiesToJson(List<GroupEntity> g_Entities, List<PermissionEntity> p_Entities) {
		JSONObject jsonAll = new JSONObject();
		JSONArray group_list = new JSONArray();
		JSONArray permission_list = new JSONArray();
		JSONObject group_one = new JSONObject();
		JSONArray permission_one = new JSONArray();
		// 群組 標題
		JSONArray jsonArray = new JSONArray();
		jsonArray.put("建立時間");
		jsonArray.put("建立者");
		jsonArray.put("修改時間");
		jsonArray.put("修改者");
		jsonArray.put("群組ID");
		jsonArray.put("群組名稱");
		jsonArray.put("是否使用");
		jsonArray.put("備註");
		group_list.put(jsonArray);
		// 權限 標題
		jsonArray = new JSONArray();
		jsonArray.put("ID");
		jsonArray.put("單元名稱");
		jsonArray.put("單元控制");
		jsonArray.put("單元組名稱");
		jsonArray.put("預留(128)");
		jsonArray.put("訪問(64)");
		jsonArray.put("下載(32)");
		jsonArray.put("上傳(16)");
		jsonArray.put("新增(8)");
		jsonArray.put("修改(4)");
		jsonArray.put("刪除(2)");
		jsonArray.put("查詢(1)");
		group_list.put(jsonArray);

		// 內容()
		int id = 99999;
		for (GroupEntity entity : g_Entities) {

			// 與上個群組不同(同 群組ID 只寫入一次)
			if (entity.getId() != id) {
				// 權限清單
				if (permission_one.length() > 0) {
					group_one.put("permission", permission_one);
					permission_one = new JSONArray();
				}
				// 群組
				group_one = new JSONObject();
				jsonArray = new JSONArray();
				jsonArray.put(Fm_Time_Model.to_yMd_Hms(entity.getSys_create_date()));
				jsonArray.put(entity.getSys_create_user());
				jsonArray.put(Fm_Time_Model.to_yMd_Hms(entity.getSys_modify_date()));
				jsonArray.put(entity.getSys_modify_user());
				jsonArray.put(entity.getId());
				jsonArray.put(entity.getName());
				jsonArray.put(entity.getUseful());
				jsonArray.put(entity.getNote());
				group_one.put("group", jsonArray);
				group_list.put(group_one);
				// 避免重複
				id = entity.getId();
			}
			// 權限
			jsonArray = new JSONArray();
			jsonArray.put(entity.getPermission_id());
			jsonArray.put(entity.getPermission_name());
			jsonArray.put(entity.getPermission_control());
			jsonArray.put(entity.getPermission_group());

			for (char i : entity.getPermission().toCharArray()) {
				jsonArray.put(String.valueOf(i));
			}

			permission_one.put(jsonArray);
		}
		group_one.put("permission", permission_one);
		permission_one = new JSONArray();
		jsonAll.put("group_list", group_list);

		// 有效的權限清單
		for (PermissionEntity entity : p_Entities) {
			jsonArray = new JSONArray();
			jsonArray.put(entity.getId());
			jsonArray.put(entity.getName());
			jsonArray.put(entity.getControl());
			jsonArray.put(entity.getGroup_name());

			for (char i : entity.getPermission().toCharArray()) {
				jsonArray.put(String.valueOf(i));
			}
			jsonArray.put(entity.getNbdesc());
			permission_list.put(jsonArray);
		}
		jsonAll.put("permission_list", permission_list);
		return jsonAll;
	}

	/**
	 * 統整回傳包裝
	 * 
	 * @param p_Obj     回傳查詢資料
	 * @param frontData 前端傳入的"控制"資訊 回傳 成功
	 **/
	public JSONObject ajaxRspJson(JSONObject p_Obj, JSONObject frontData,String r_Message) {
		JsonDataModel data = new JsonDataModel();
		JSONObject r_allData = new JSONObject();
		JsonObjBean objBean = new JsonObjBean();
		JsonTemplateBean templateBean = new JsonTemplateBean();

		templateBean.setWebPageBody("html/body/sys_group_body.html");
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
