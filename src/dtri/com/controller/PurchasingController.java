package dtri.com.controller;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import dtri.com.db.entity.GroupEntity;
import dtri.com.db.entity.PurchasingEntity;
import dtri.com.db.entity.UserEntity;
import dtri.com.models.JsonDataModel;
import dtri.com.service.LoginService;
import dtri.com.service.PermissionService;
import dtri.com.service.PurchasingService;
import dtri.com.service.UserService;

@Controller
public class PurchasingController {

	@Autowired
	LoginService loginService;
	@Autowired
	PurchasingService p_service;
	@Autowired
	UserService userService;
	@Autowired
	PermissionService permissionService;
	// 功能
	final static String SYS_F = "purchasing_link.do";

	/**
	 * 
	 * 基本載入 /查詢
	 * 
	 * @param ajaxJSON 限定用JSON
	 */
	@ResponseBody
	@RequestMapping(value = "/purchasing_link", method = {
			RequestMethod.POST }, produces = "application/json;charset=UTF-8")
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
			PurchasingEntity entity = new PurchasingEntity();
			if (frontData.get("content") != null && !frontData.get("content").equals("")) {
				entity = p_service.jsonToEntities(frontData.getJSONObject("content"));

			}
			// Step4-2 .DB 查詢 正確 資料
			List<PurchasingEntity> p_Entities = p_service.searchLink(entity);
			List<UserEntity> u_Entities = userService.searchUser(new UserEntity());
			JSONObject p_Obj = p_service.entitiesToJson(p_Entities, u_Entities);

			// Step4-2 .包裝資料
			r_allData = p_service.ajaxRspJson(p_Obj, frontData, "訪問成功!!");
		} else {
			// Step4-1 .登出 && 包裝 錯誤 資料
			r_allData = p_service.fail_ajaxRspJson(frontData);

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
	@RequestMapping(value = "/added_purchasing_link", method = {
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
			PurchasingEntity entity = new PurchasingEntity();
			if (frontData.get("content") != null && !frontData.get("content").equals("")) {
				entity = p_service.jsonToEntities(frontData.getJSONObject("content"));
				// Step4-1 .DB 新增 正確 資料
				p_service.addedPurchasingEntity(entity);
			}

			// Step4-2 .DB 查詢 正確 資料
			List<PurchasingEntity> p_Entities = p_service.searchLink(new PurchasingEntity());
			List<UserEntity> u_Entities = userService.searchUser(new UserEntity());
			JSONObject p_Obj = p_service.entitiesToJson(p_Entities, u_Entities);

			// Step4-2 .包裝資料
			r_allData = p_service.ajaxRspJson(p_Obj, frontData, "新增成功!!");
		} else {
			// Step4-1 .登出 && 包裝 錯誤 資料
			r_allData = p_service.fail_ajaxRspJson(frontData);
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
	@RequestMapping(value = "/update_purchasing_link", method = {
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
			PurchasingEntity entity = new PurchasingEntity();
			if (frontData.get("content") != null && !frontData.get("content").equals("")) {
				entity = p_service.jsonToEntities(frontData.getJSONObject("content"));

				// Step4-1 .DB 更新 正確 資料
				p_service.updatePurchasingEntity(entity);
			}

			// Step4-2 .DB 查詢 正確 資料

			List<PurchasingEntity> p_Entities = p_service.searchLink(new PurchasingEntity());
			List<UserEntity> u_Entities = userService.searchUser(new UserEntity());
			JSONObject p_Obj = p_service.entitiesToJson(p_Entities, u_Entities);

			// Step4-3 .包裝資料
			r_allData = p_service.ajaxRspJson(p_Obj, frontData, "更新成功!!");
		} else {

			// Step4-1 .登出 && 包裝 錯誤 資料
			r_allData = p_service.fail_ajaxRspJson(frontData);
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
	@RequestMapping(value = "/delete_purchasing_link", method = {
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
			PurchasingEntity entity = new PurchasingEntity();
			if (frontData.get("content") != null && !frontData.get("content").equals("")) {
				entity = p_service.jsonToEntities(frontData.getJSONObject("content"));

				// Step4-1 .DB 移除 正確 資料
				p_service.deletePurchasingEntity(entity);

			}
			// Step4-2 .DB 查詢 正確 資料
			List<PurchasingEntity> p_Entities = p_service.searchLink(new PurchasingEntity());
			List<UserEntity> u_Entities = userService.searchUser(new UserEntity());
			JSONObject p_Obj = p_service.entitiesToJson(p_Entities, u_Entities);

			// Step4-3 .包裝資料
			r_allData = p_service.ajaxRspJson(p_Obj, frontData, "移除成功!!");
		} else {
			// Step4-1 .登出 && 包裝 錯誤 資料
			r_allData = p_service.fail_ajaxRspJson(frontData);
		}

		// Step5.結果回傳
		System.out.println(r_allData);
		return r_allData.toString();
	}
}
