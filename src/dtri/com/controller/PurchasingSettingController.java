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
import dtri.com.db.entity.PurchasingListEntity;
import dtri.com.db.entity.PurchasingMailEntity;
import dtri.com.db.entity.PurchasingSettingEntity;
import dtri.com.models.JsonDataModel;
import dtri.com.service.LoginService;
import dtri.com.service.PermissionService;
import dtri.com.service.PurchasingSettingService;
import dtri.com.service.ScheduleTaskService;

@Controller
public class PurchasingSettingController {

	@Autowired
	LoginService loginService;
	@Autowired
	PurchasingSettingService p_service;
	@Autowired
	PermissionService permissionService;
	@Autowired
	ScheduleTaskService scheduleTaskService;
	// 功能
	final static String SYS_F = "purchasing_setting.do";

	/**
	 * 
	 * 基本載入 /查詢
	 * 
	 * @param ajaxJSON 限定用JSON
	 */
	@ResponseBody
	@RequestMapping(value = "/purchasing_setting", method = {
			RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String search_purchasing_setting(@RequestBody String ajaxJSON) {
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
		int page_nb = 0, page_total = 100;
		// Step4.檢查許可權 & 輸入物件
		if (checkPermission) {
			// Step4-1 .DB 取出 正確 資料
			JSONObject entity = new JSONObject();
			PurchasingListEntity listEntity = new PurchasingListEntity();
			if (frontData.get("content") != null && !frontData.get("content").equals("")) {
				entity = p_service.jsonToEntities(frontData.getJSONObject("content"));
				if (entity != null && !entity.toString().equals("") && entity.get("PurchasingList") != null) {
					listEntity = (PurchasingListEntity) entity.get("PurchasingList");
				}
				// 取得換頁碼 如果沒有 0
				page_nb = p_service.jsonToPageNb(frontData.getJSONObject("content"));
				page_total = p_service.jsonToPageTotal(frontData.getJSONObject("content"));
			}
			// Step4-2 .DB 查詢 正確 資料
			List<PurchasingListEntity> l_Entities = p_service.searchList(listEntity, page_nb, page_total);
			List<PurchasingSettingEntity> s_Entities = p_service.searchSetting();
			PurchasingMailEntity m_Entities = p_service.searchMailContent();
			JSONObject p_Obj = p_service.entitiesToJson(l_Entities, s_Entities, m_Entities);

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
	 * 更新
	 * 
	 * @param ajaxJSON 限定用JSON
	 */
	@ResponseBody
	@RequestMapping(value = "/update_purchasing_setting", method = {
			RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String update_purchasing(@RequestBody String ajaxJSON) {
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
		int page_nb = 0, page_total = 100;
		// Step4.檢查許可權 & 輸入物件
		if (checkPermission) {

			// Step4-1 .DB 取出 正確 資料
			JSONObject entity = new JSONObject();
			PurchasingListEntity listEntity = new PurchasingListEntity();
			if (frontData.get("content") != null && !frontData.get("content").equals("")) {
				entity = p_service.jsonToEntities(frontData.getJSONObject("content"));

				// 更新
				if (entity != null && !entity.toString().equals("") && entity.get("PurchasingSetting") != null
						&& entity.get("PurchasingMail") != null) {
					p_service.updateP_Setting(entity);
				}
			}

			// Step4-2 .DB 查詢 正確 資料
			List<PurchasingListEntity> l_Entities = p_service.searchList(listEntity,page_nb,page_total);
			List<PurchasingSettingEntity> s_Entities = p_service.searchSetting();
			PurchasingMailEntity m_Entities = p_service.searchMailContent();
			JSONObject p_Obj = p_service.entitiesToJson(l_Entities, s_Entities, m_Entities);

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
	 * 測試傳送
	 * 
	 * @param ajaxJSON 限定用JSON
	 */
	@ResponseBody
	@RequestMapping(value = "/testmail_purchasing_setting", method = {
			RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String testmail_purchasing(@RequestBody String ajaxJSON) {
		System.out.println("---controller - update_sys_group");
		// Step1.解析內容-檢查 -> 取出內容物
		JsonDataModel data = new JsonDataModel();
		JSONObject frontData = (JSONObject) data.frontToBack(ajaxJSON).get("data");
		System.out.println(frontData);

		// Step2.取出 session 訊息 & 檢查權限( 新增 更新 )
		List<GroupEntity> group = loginService.getSessionGroupBean();
		boolean checkPermission = loginService.checkPermission(group, SYS_F, "01111111");
		// Step3.建立回傳元素
		JSONObject r_allData = new JSONObject();

		// Step4.檢查許可權 & 輸入物件
		if (checkPermission) {

			// Step4-2 .DB 查詢 正確 資料
			PurchasingListEntity listEntity = new PurchasingListEntity();
			int page_nb = 0, page_total = 100;
			List<PurchasingListEntity> l_Entities = p_service.searchList(listEntity,page_nb,page_total);
			List<PurchasingSettingEntity> s_Entities = p_service.searchSetting();
			PurchasingMailEntity m_Entities = p_service.searchMailContent();
			JSONObject p_Obj = p_service.entitiesToJson(l_Entities, s_Entities, m_Entities);
			scheduleTaskService.sendMailFromPurchasing();
			r_allData = p_service.ajaxRspJson(p_Obj, frontData, "測試寄件成功!!");
		} else {
			// Step4-1 .登出 && 包裝 錯誤 資料
			r_allData = p_service.fail_ajaxRspJson(frontData);
		}
		
		// Step5.結果回傳
		System.out.println(r_allData);
		return r_allData.toString();
	}

}
