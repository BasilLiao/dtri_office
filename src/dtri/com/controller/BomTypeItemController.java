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

import dtri.com.db.entity.BomTypeItemEntity;
import dtri.com.db.entity.GroupEntity;
import dtri.com.models.JsonDataModel;
import dtri.com.service.BomTypeItemService;
import dtri.com.service.LoginService;

@Controller
public class BomTypeItemController {

	@Autowired
	LoginService loginService;
	@Autowired
	BomTypeItemService itemService;
	// 功能
	final static String SYS_F = "bom_type_item.do";

	/**
	 * 
	 * 基本載入 /查詢
	 * 
	 * @param ajaxJSON 限定用JSON
	 */
	@ResponseBody
	@RequestMapping(value = "/bom_type_item", method = {
			RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String search_bom_type_item(@RequestBody String ajaxJSON) {
		System.out.println("---controller - bom_type_item");
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
			BomTypeItemEntity entity = new BomTypeItemEntity();
			if (frontData.get("content") != null && !frontData.get("content").equals("")) {
				entity = itemService.jsonToEntities(frontData.getJSONObject("content")).get(0);

			}
			entity.setGroup_id(0);
			List<BomTypeItemEntity> p_Entities = itemService.search(entity);
			JSONObject p_Obj = itemService.entitiesToJson(p_Entities);

			// Step4-2 .包裝資料
			r_allData = itemService.ajaxRspJson(p_Obj, frontData, "訪問成功!!");
		} else {
			// Step4-1 .登出 && 包裝 錯誤 資料
			r_allData = itemService.fail_ajaxRspJson(frontData, "你沒有權限!!");
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
	@RequestMapping(value = "/added_bom_type_item", method = {
			RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String added_bom_type_item(@RequestBody String ajaxJSON) {
		System.out.println("---controller - added_bom_type_item");
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
			BomTypeItemEntity entity = new BomTypeItemEntity();
			if (frontData.get("content") != null && !frontData.get("content").equals("")) {
				entity = itemService.jsonToEntities(frontData.getJSONObject("content")).get(0);

				// Step4-1 .DB 新增(項目群組) 正確 資料
				itemService.addedTypeItem(entity, true);
				// Step4-2 .DB 查詢 正確 資料
				entity = new BomTypeItemEntity();
				entity.setGroup_id(0);
				List<BomTypeItemEntity> p_Entities = itemService.search(entity);
				JSONObject p_Obj = itemService.entitiesToJson(p_Entities);

				// Step4-2 .包裝資料
				r_allData = itemService.ajaxRspJson(p_Obj, frontData, "新增成功!!");

			}
		} else {

			// Step4-1 .登出 && 包裝 錯誤 資料
			r_allData = itemService.fail_ajaxRspJson(frontData, "你沒有權限!!");
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
	@RequestMapping(value = "/update_bom_type_item", method = {
			RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String update_bom_type_item(@RequestBody String ajaxJSON) {
		System.out.println("---controller - update_bom_type_item");
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
			List<BomTypeItemEntity> entitys = new ArrayList<BomTypeItemEntity>();
			BomTypeItemEntity entity = new BomTypeItemEntity();
			if (frontData.get("content") != null && !frontData.get("content").equals("")) {
				entitys = itemService.jsonToEntities(frontData.getJSONObject("content"));

				// Step4-1 .DB 更新 正確 資料
				itemService.update(entitys);
			}

			// Step4-2 .DB 查詢 正確 資料
			entity = new BomTypeItemEntity();
			List<BomTypeItemEntity> p_Entities = itemService.search(entity);
			JSONObject p_Obj = itemService.entitiesToJson(p_Entities);

			// Step4-3 .包裝資料
			r_allData = itemService.ajaxRspJson(p_Obj, frontData, "更新成功!!");
		} else {

			// Step4-1 .登出 && 包裝 錯誤 資料
			r_allData = itemService.fail_ajaxRspJson(frontData, "你沒有權限!!");
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
	@RequestMapping(value = "/delete_bom_type_item", method = {
			RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String delete_bom_type_item(@RequestBody String ajaxJSON) {
		System.out.println("---controller - delete_bom_type_item");
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
			List<BomTypeItemEntity> entitys = new ArrayList<BomTypeItemEntity>();
			if (frontData.get("content") != null && !frontData.get("content").equals("")) {
				entitys = itemService.jsonToEntities(frontData.getJSONObject("content"));

				// Step4-1 .DB 移除 正確 資料
				itemService.delete(entitys.get(0));
			}

			// Step4-2 .DB 查詢 正確 資料
			BomTypeItemEntity entity = new BomTypeItemEntity();
			List<BomTypeItemEntity> p_Entities = itemService.search(entity);
			JSONObject p_Obj = itemService.entitiesToJson(p_Entities);
			// Step4-3 .包裝資料
			r_allData = itemService.ajaxRspJson(p_Obj, frontData, "移除成功!!");
		} else {

			// Step4-1 .登出 && 包裝 錯誤 資料
			r_allData = itemService.fail_ajaxRspJson(frontData, "你沒有權限!!");
		}

		// Step5.結果回傳
		System.out.println(r_allData);
		return r_allData.toString();
	}
}
