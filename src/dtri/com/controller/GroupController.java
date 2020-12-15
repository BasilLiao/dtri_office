package dtri.com.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import dtri.com.db.entity.GroupEntity;
import dtri.com.db.entity.PermissionEntity;
import dtri.com.models.JsonDataModel;
import dtri.com.service.GroupService;
import dtri.com.service.LoginService;
import dtri.com.service.PermissionService;

@Controller
public class GroupController {

	@Autowired
	LoginService loginService;
	@Autowired
	GroupService groupService;
	@Autowired
	PermissionService permissionService;
	// 功能
	final static String SYS_F = "sys_group.do";

	/**
	 * 
	 * 基本載入 /查詢
	 * 
	 * @param ajaxJSON 限定用JSON
	 */
	@ResponseBody
	@RequestMapping(value = "/sys_group", method = { RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String search_sys_permission(@RequestBody String ajaxJSON) {
		System.out.println("---controller - sys_group");
		// Step1.取出 session 訊息 & 檢查權限
		List<GroupEntity> group = loginService.getSessionGroupBean();
		boolean checkPermission = loginService.checkPermission(group, SYS_F, "01000001");

		// Step2.解析內容-檢查 -> 取出內容物
		JsonDataModel data = new JsonDataModel();
		JSONObject frontData = (JSONObject) data.frontToBack(ajaxJSON).get("data");
		System.out.println(frontData);

		// Step3.建立回傳元素
		JSONObject r_allData = new JSONObject();

		// Step4.檢查許可權 & 輸入物件
		if (checkPermission) {
			// Step4-1 .DB 取出 正確 資料
			GroupEntity entity = new GroupEntity();
			if (frontData.get("content") != null && !frontData.get("content").equals("")) {
				entity = groupService.jsonToEntities(frontData.getJSONObject("content"));

			}
			// Step4-2 .DB 查詢 正確 資料
			PermissionEntity entity_p = new PermissionEntity();
			List<GroupEntity> g_Entities = groupService.searchGroup(entity);
			List<PermissionEntity> p_Entities = permissionService.searchPermission(entity_p);
			JSONObject p_Obj = groupService.entitiesToJson(g_Entities, p_Entities);

			// Step4-2 .包裝資料
			r_allData = groupService.ajaxRspJson(p_Obj, frontData,"訪問成功!!");
		} else {
			// Step4-1 .登出 && 包裝 錯誤 資料
			
			r_allData = groupService.fail_ajaxRspJson(frontData);
		}

		// Step6.結果回傳
		System.out.println(r_allData);
		return r_allData.toString();
	}

	/**
	 * 新增
	 * 
	 * @param ajaxJSON 限定用JSON
	 */
	@ResponseBody
	@RequestMapping(value = "/added_sys_group", method = {
			RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String added_sys_permission(@RequestBody String ajaxJSON) {
		System.out.println("---controller - added_sys_group");
		// Step2.取出 session 訊息 & 檢查權限( 新增 更新 )
		List<GroupEntity> group = loginService.getSessionGroupBean();
		boolean checkPermission = loginService.checkPermission(group, SYS_F, "01001001");

		// Step1.解析內容-檢查 -> 取出內容物
		JsonDataModel data = new JsonDataModel();
		JSONObject frontData = (JSONObject) data.frontToBack(ajaxJSON).get("data");
		System.out.println(frontData);

		// Step3.建立回傳元素
		JSONObject r_allData = new JSONObject();

		// Step4.檢查許可權 & 輸入物件
		if (checkPermission) {
			// Step4-1 .DB 取出 正確 資料
			List<GroupEntity> entity = new ArrayList<GroupEntity>();
			if (frontData.get("content") != null && !frontData.get("content").equals("")) {
				entity = groupService.jsonToEntitiesN(frontData.getJSONArray("content"));
				// Step4-1 .DB 新增 正確 資料
				groupService.addedGroup(entity,0);
			}
			
			// Step4-2 .DB 查詢 正確 資料
			GroupEntity r_entity = new GroupEntity();
			PermissionEntity entity_p = new PermissionEntity();
			List<GroupEntity> g_Entities = groupService.searchGroup(r_entity);
			List<PermissionEntity> p_Entities = permissionService.searchPermission(entity_p);
			JSONObject p_Obj = groupService.entitiesToJson(g_Entities, p_Entities);

			// Step4-2 .包裝資料
			r_allData = groupService.ajaxRspJson(p_Obj, frontData,"新增成功!!");
		} else {
			// Step4-1 .登出 && 包裝 錯誤 資料
			r_allData = groupService.fail_ajaxRspJson(frontData);
		}

		// Step6.結果回傳
		System.out.println(r_allData);
		return r_allData.toString();
	}

	/**
	 * 更新
	 * 
	 * @param ajaxJSON 限定用JSON
	 */
	@ResponseBody
	@RequestMapping(value = "/update_sys_group", method = {
			RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String update_sys_permission(@RequestBody String ajaxJSON) {
		System.out.println("---controller - update_sys_group");
		// Step2.取出 session 訊息 & 檢查權限( 新增 更新 )
		List<GroupEntity> group = loginService.getSessionGroupBean();
		boolean checkPermission = loginService.checkPermission(group, SYS_F, "01000101");

		// Step1.解析內容-檢查 -> 取出內容物
		JsonDataModel data = new JsonDataModel();
		JSONObject frontData = (JSONObject) data.frontToBack(ajaxJSON).get("data");
		System.out.println(frontData);

		// Step3.建立回傳元素
		JSONObject r_allData = new JSONObject();

		// Step4.檢查許可權 & 輸入物件
		if (checkPermission) {

			// Step4-1 .DB 取出 正確 資料
			List<GroupEntity> entity = new ArrayList<GroupEntity>();
			if (frontData.get("content") != null && !frontData.get("content").equals("")) {
				entity = groupService.jsonToEntitiesN(frontData.getJSONArray("content"));

				// Step4-1 .DB 更新 正確 資料
				groupService.updateGroup(entity);
			}

			// Step4-2 .DB 查詢 正確 資料
			GroupEntity r_entity = new GroupEntity();
			PermissionEntity entity_p = new PermissionEntity();
			List<GroupEntity> g_Entities = groupService.searchGroup(r_entity);
			List<PermissionEntity> p_Entities = permissionService.searchPermission(entity_p);
			JSONObject p_Obj = groupService.entitiesToJson(g_Entities, p_Entities);

			// Step4-3 .包裝資料
			r_allData = groupService.ajaxRspJson(p_Obj, frontData,"更新成功!!");
		} else {

			// Step4-1 .登出 && 包裝 錯誤 資料
			
			r_allData = groupService.fail_ajaxRspJson(frontData);
		}

		// Step5.結果回傳
		System.out.println(r_allData);
		return r_allData.toString();
	}

	/**
	 * 移除
	 * 
	 * @param ajaxJSON 限定用JSON
	 */
	@ResponseBody
	@RequestMapping(value = "/delete_sys_group", method = {
			RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String delete_sys_permission(@RequestBody String ajaxJSON) {
		System.out.println("---controller - delete_sys_group");
		// Step2.取出 session 訊息 & 檢查權限( 新增 更新 )
		List<GroupEntity> group = loginService.getSessionGroupBean();
		boolean checkPermission = loginService.checkPermission(group, SYS_F, "01000011");

		// Step1.解析內容-檢查 -> 取出內容物
		JsonDataModel data = new JsonDataModel();
		JSONObject frontData = (JSONObject) data.frontToBack(ajaxJSON).get("data");
		System.out.println(frontData);

		// Step3.建立回傳元素
		JSONObject r_allData = new JSONObject();

		// Step4.檢查許可權 & 輸入物件
		if (checkPermission) {

			// Step4-1 .DB 取出 正確 資料
			List<GroupEntity> entity = new ArrayList<GroupEntity>();
			if (frontData.get("content") != null && !frontData.get("content").equals("")) {
				entity = groupService.jsonToEntitiesN(frontData.getJSONArray("content"));

				// Step4-1 .DB 移除 正確 資料
				groupService.deleteGroup(entity.get(0), true);

			}

			// Step4-2 .DB 查詢 正確 資料
			GroupEntity r_entity = new GroupEntity();
			PermissionEntity entity_p = new PermissionEntity();
			List<GroupEntity> g_Entities = groupService.searchGroup(r_entity);
			List<PermissionEntity> p_Entities = permissionService.searchPermission(entity_p);
			JSONObject p_Obj = groupService.entitiesToJson(g_Entities, p_Entities);

			// Step4-3 .包裝資料
			r_allData = groupService.ajaxRspJson(p_Obj, frontData,"移除成功!!");
		} else {
			// Step4-1 .登出 && 包裝 錯誤 資料
			r_allData = groupService.fail_ajaxRspJson(frontData);
		}

		// Step5.結果回傳
		System.out.println(r_allData);
		return r_allData.toString();
	}
}
