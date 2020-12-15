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
import dtri.com.db.entity.SoftwareVersionEntity;
import dtri.com.db.entity.UserEntity;
import dtri.com.models.JsonDataModel;
import dtri.com.service.LoginService;
import dtri.com.service.SoftwareVersionService;

@Controller
public class SoftwareVersionController {

	@Autowired
	LoginService loginService;
	@Autowired
	SoftwareVersionService softwareVersionService;
	// 功能
	final static String SYS_F = "software_version.do";

	/**
	 * 
	 * 基本載入 /查詢
	 * 
	 * @param ajaxJSON 限定用JSON
	 */
	@ResponseBody
	@RequestMapping(value = "/software_version", method = {
			RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String search_software_version(@RequestBody String ajaxJSON) {
		System.out.println("---controller - software_version");
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
			SoftwareVersionEntity entity = new SoftwareVersionEntity();
			if (frontData.get("content") != null && !frontData.get("content").equals("")) {
				entity = softwareVersionService.jsonToEntities(frontData.getJSONObject("content"));

			}
			List<SoftwareVersionEntity> p_Entities = softwareVersionService.searchSoftwareVersion(entity);
			JSONObject p_Obj = softwareVersionService.entitiesToJson(p_Entities);

			// Step4-2 .包裝資料
			r_allData = softwareVersionService.ajaxRspJson(p_Obj, frontData, "訪問成功!!");
		} else {
			// Step4-1 .登出 && 包裝 錯誤 資料
			r_allData = softwareVersionService.fail_ajaxRspJson(frontData, "你沒有權限!!");
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
	@RequestMapping(value = "/added_software_version", method = {
			RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String added_software_version(@RequestBody String ajaxJSON) {
		System.out.println("---controller - added_software_version");
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
			SoftwareVersionEntity entity = new SoftwareVersionEntity();
			SoftwareVersionEntity entitycheck = new SoftwareVersionEntity();
			if (frontData.get("content") != null && !frontData.get("content").equals("")) {
				entity = softwareVersionService.jsonToEntities(frontData.getJSONObject("content"));

				// 檢查 客戶名稱/BOM 料號 是否有重複
				entitycheck = softwareVersionService.jsonToEntities(frontData.getJSONObject("content"));

				List<SoftwareVersionEntity> p_Entities = softwareVersionService.searchSoftwareVersion(entitycheck);
				// 有重複(錯誤回傳)?
				if (p_Entities.size() > 0) {
					r_allData = softwareVersionService.fail_ajaxRspJson(frontData, "(BOM料號 && 客戶名稱)重複!!");

				} else {
					// Step4-1 .DB 新增 正確 資料
					softwareVersionService.addedSoftwareVers(entity);
					// Step4-2 .DB 查詢 正確 資料
					entity = new SoftwareVersionEntity();
					p_Entities = softwareVersionService.searchSoftwareVersion(entity);
					JSONObject p_Obj = softwareVersionService.entitiesToJson(p_Entities);

					// Step4-2 .包裝資料
					r_allData = softwareVersionService.ajaxRspJson(p_Obj, frontData, "新增成功!!");
				}
			}
		} else {

			// Step4-1 .登出 && 包裝 錯誤 資料
			r_allData = softwareVersionService.fail_ajaxRspJson(frontData, "你沒有權限!!");
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
	@RequestMapping(value = "/update_software_version", method = {
			RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String update_software_version(@RequestBody String ajaxJSON) {
		System.out.println("---controller - update_software_version");
		// Step2.取出 session 訊息 & 檢查權限( 新增 更新 )
		List<GroupEntity> group = loginService.getSessionGroupBean();
		boolean checkPermission = loginService.checkPermission(group, SYS_F, "01001101");

		// Step1.解析內容-檢查 -> 取出內容物
		JsonDataModel data = new JsonDataModel();
		JSONObject frontData = (JSONObject) data.frontToBack(ajaxJSON).get("data");
		System.out.println(frontData);

		// Step3.建立回傳元素
		JSONObject r_allData = new JSONObject();

		// Step4.檢查許可權 & 輸入物件
		if (checkPermission) {

			// Step4-1 .DB 取出 正確 資料
			SoftwareVersionEntity entity = new SoftwareVersionEntity();
			if (frontData.get("content") != null && !frontData.get("content").equals("")) {
				entity = softwareVersionService.jsonToEntities(frontData.getJSONObject("content"));

				// Step4-1 .DB 更新->(沒有->新增) 正確 資料
				boolean check = softwareVersionService.updateSoftwareVers(entity);
				if (!check && frontData.getJSONObject("content").getBoolean("added")) {
					softwareVersionService.addedSoftwareVers(entity);
				}
			}

			// Step4-2 .DB 查詢 正確 資料
			entity = new SoftwareVersionEntity();
			List<SoftwareVersionEntity> p_Entities = softwareVersionService.searchSoftwareVersion(entity);
			JSONObject p_Obj = softwareVersionService.entitiesToJson(p_Entities);

			// Step4-3 .包裝資料
			r_allData = softwareVersionService.ajaxRspJson(p_Obj, frontData, "更新成功!!");
		} else {

			// Step4-1 .登出 && 包裝 錯誤 資料
			r_allData = softwareVersionService.fail_ajaxRspJson(frontData, "你沒有權限!!");
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
	@RequestMapping(value = "/delete_software_version", method = {
			RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String delete_software_version(@RequestBody String ajaxJSON) {
		System.out.println("---controller - delete_software_version");
		// Step2.取出 session 訊息 & 檢查權限( 新增 更新 )
		List<GroupEntity> group = loginService.getSessionGroupBean();
		UserEntity u = loginService.getSessionUserBean();
		boolean checkPermission = loginService.checkPermission(group, SYS_F, "01000011");

		// Step1.解析內容-檢查 -> 取出內容物
		JsonDataModel data = new JsonDataModel();
		JSONObject frontData = (JSONObject) data.frontToBack(ajaxJSON).get("data");
		System.out.println(frontData);

		// Step3.建立回傳元素
		JSONObject r_allData = new JSONObject();

		// Step4.檢查許可權 & 輸入物件 & 是否為同一人
		if (checkPermission && frontData.getJSONObject("content").getInt("id") != u.getId()) {

			// Step4-1 .DB 取出 正確 資料
			SoftwareVersionEntity entity = new SoftwareVersionEntity();
			if (frontData.get("content") != null && !frontData.get("content").equals("")) {
				entity = softwareVersionService.jsonToEntities(frontData.getJSONObject("content"));

				// Step4-1 .DB 移除 正確 資料
				softwareVersionService.deleteSoftwareVers(entity);
			}

			// Step4-2 .DB 查詢 正確 資料
			entity = new SoftwareVersionEntity();
			List<SoftwareVersionEntity> p_Entities = softwareVersionService.searchSoftwareVersion(entity);
			JSONObject p_Obj = softwareVersionService.entitiesToJson(p_Entities);

			// Step4-3 .包裝資料
			r_allData = softwareVersionService.ajaxRspJson(p_Obj, frontData, "移除成功!!");
		} else {

			// Step4-1 .登出 && 包裝 錯誤 資料
			r_allData = softwareVersionService.fail_ajaxRspJson(frontData, "你沒有權限!!");
		}

		// Step5.結果回傳
		System.out.println(r_allData);
		return r_allData.toString();
	}
}
