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
import dtri.com.db.pgsql.dao.PermissionDao;
import dtri.com.models.Fm_Time_Model;
import dtri.com.models.JsonDataModel;

@Transactional
@Service
public class PermissionService {
	@Autowired
	private PermissionDao permissionDao;
	@Autowired
	private LoginService loginService;
	@Autowired
	private GroupService grService;
	@Autowired
	private GroupDao groupDao;

	/**
	 * @param byName      單元名稱
	 * @param byGroupName 群組名稱
	 * @param byControl   控制單元名稱
	 * @return 查詢後清單
	 * 
	 **/
	public List<PermissionEntity> searchPermission(PermissionEntity entity) {
		// 防止輸入為空 或 null

		if (entity.getName() == null) {
			entity.setName("");
		}
		if (entity.getGroup_name() == null) {
			entity.setGroup_name("");
		}
		if (entity.getControl() == null) {
			entity.setControl("");
		}
		//不能看到最高管理者單元
		Boolean whoCheck = loginService.getSessionUserBean().getId()==1;
		if(!whoCheck) {
			entity.setUseful(3);
		}
		
		//矯正 
		groupDao.updateAll();	
		List<PermissionEntity> list = permissionDao.queryAll(entity);
		return list;
	}

	/** 新增 **/
	public Boolean addedPermission(PermissionEntity entity) {
		Boolean check = false;
		permissionDao.addedOne(entity);

		updateAdminPermission(entity,"G");
		return check;
	}

	/** 新增 (有重複群組資料(同個群組)) **/
	public Boolean addedRepeatGroupPermission(PermissionEntity entity) {
		Boolean check = false;
		// 添加一筆
		permissionDao.addedRepeatGroupOne(entity);

		updateAdminPermission(entity,"G");
		return check;
	}
	/**自動創建權限 入 Admin**/
	private void updateAdminPermission(PermissionEntity entity ,String type) {
		
		//查詢ID -正確輸入
		PermissionEntity entitycheck = new PermissionEntity();
		if((entity.getId()==null || entity.getId()==0) && !type.equals("D")) {
			entitycheck.setName(entity.getName());
			entitycheck.setControl(entity.getControl());
			entitycheck = searchPermission(entitycheck).get(0);
			entity = entitycheck;
		}
		
		// 因Admin 群組自動登記-
		GroupEntity one = new GroupEntity();
		one.setSys_create_date(new Date());
		one.setSys_create_user(loginService.getSessionUserBean().getAccount());
		one.setSys_modify_date(new Date());
		one.setSys_modify_user(loginService.getSessionUserBean().getAccount());
		
		one.setId(1);
		one.setName(loginService.getSessionUserBean().getAccount());
		one.setNote("");
		one.setUseful(3);//3=只給Admin看
		one.setPermission(entity.getPermission());
		one.setPermission_control(entity.getControl());
		one.setPermission_group(entity.getGroup_name());
		one.setPermission_id(entity.getId());
		one.setPermission_name(entity.getName());
		one.setPermission_type(type);
		//新增 or 修改 or 移除 admin權限
		List<GroupEntity> lge = new ArrayList<GroupEntity>();
		lge.add(one);
		grService.updateGroup(lge);
	}	
	/** 更新 **/
	public Boolean updatePermission(PermissionEntity entity) {
		Boolean check = false;
		permissionDao.updateOne(entity);
		return check;
	}

	/** 移除 **/
	public Boolean deletePermission(PermissionEntity entity) {
		Boolean check = false;
		permissionDao.deleteOne(entity);
		updateAdminPermission(entity,"D");
		return check;

	}

	/** JSON to 清單 **/
	public PermissionEntity jsonToEntities(JSONObject content) {
		PermissionEntity entity = new PermissionEntity();
		entity.setSys_create_date(new Date());
		entity.setSys_create_user(loginService.getSessionUserBean().getAccount());
		entity.setSys_modify_date(new Date());
		entity.setSys_modify_user(loginService.getSessionUserBean().getAccount());
		if (!content.isNull("id") && !content.get("id").equals(""))
			entity.setId(content.getInt("id"));
		if (!content.isNull("name"))
			entity.setName(content.getString("name"));
		if (!content.isNull("group_id") && !content.get("group_id").equals(""))
			entity.setGroup_id(content.getInt("group_id"));
		if (!content.isNull("group_name"))
			entity.setGroup_name(content.getString("group_name"));
		if (!content.isNull("control"))
			entity.setControl(content.getString("control"));
		if (!content.isNull("permission"))
			entity.setPermission(content.getString("permission"));
		if (!content.isNull("useful") && !content.get("useful").equals(""))
			entity.setUseful(content.getInt("useful"));
		if (!content.isNull("note"))
			entity.setNote(content.getString("note"));
		if (!content.isNull("nbdesc") && !content.get("nbdesc").equals(""))
			entity.setNbdesc(content.getInt("nbdesc"));
		return entity;
	}

	/** 清單 to JSON **/
	public JSONObject entitiesToJson(List<PermissionEntity> p_Entities) {
		JSONObject list = new JSONObject();
		JSONArray jsonAll = new JSONArray();
		// 標題
		JSONArray jsonArray = new JSONArray();
		jsonArray.put("建立時間");
		jsonArray.put("建立者");
		jsonArray.put("修改時間");
		jsonArray.put("修改者");
		jsonArray.put("單元ID");
		jsonArray.put("單元名稱");
		jsonArray.put("單元群組ID");
		jsonArray.put("群組名稱");
		jsonArray.put("控制單元名稱");
		jsonArray.put("權限");
		jsonArray.put("是否使用");
		jsonArray.put("備註");
		jsonArray.put("順序");
		jsonAll.put(jsonArray);
		// 內容
		for (PermissionEntity entity : p_Entities) {
			jsonArray = new JSONArray();
			jsonArray.put(Fm_Time_Model.to_yMd_Hms(entity.getSys_create_date()));
			jsonArray.put(entity.getSys_create_user());
			jsonArray.put(Fm_Time_Model.to_yMd_Hms(entity.getSys_modify_date()));
			jsonArray.put(entity.getSys_modify_user());
			jsonArray.put(entity.getId());
			jsonArray.put(entity.getName());
			jsonArray.put(entity.getGroup_id());
			jsonArray.put(entity.getGroup_name());
			jsonArray.put(entity.getControl());
			jsonArray.put(entity.getPermission());
			jsonArray.put(entity.getUseful());
			jsonArray.put(entity.getNote());
			jsonArray.put(entity.getNbdesc());
			jsonAll.put(jsonArray);
		}
		list.put("list", jsonAll);
		return list;
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

		templateBean.setWebPageBody("html/body/sys_permission_body.html");
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
