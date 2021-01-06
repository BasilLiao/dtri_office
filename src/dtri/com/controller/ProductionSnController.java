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
import dtri.com.db.entity.ProductionSnEntity;
import dtri.com.models.JsonDataModel;
import dtri.com.service.LoginService;
import dtri.com.service.ProductionSnService;

@Controller
public class ProductionSnController {

	@Autowired
	LoginService loginService;
	@Autowired
	ProductionSnService snService;
	// 功能
	final static String SYS_F = "production_sn.do";

	/**
	 * 
	 * 基本載入 /查詢
	 * 
	 * @param ajaxJSON 限定用JSON
	 */
	@ResponseBody
	@RequestMapping(value = "/production_sn", method = {
			RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String search_basic_sn(@RequestBody String ajaxJSON) {
		System.out.println("---controller - production_sn");
		// Step1.取出 session 訊息 & 檢查權限
		List<GroupEntity> group = loginService.getSessionGroupBean();
		boolean checkPermission = loginService.checkPermission(group, SYS_F, "01000001");

		// Step3.建立回傳元素
		JSONObject r_allData = new JSONObject();
		JsonDataModel data = new JsonDataModel();
		JSONObject frontData = (JSONObject) data.frontToBack(ajaxJSON).get("data");

		// Step4.檢查許可權 & 輸入物件
		if (checkPermission) {
			// Step4-1 .DB 取出 正確 資料
			ArrayList<ProductionSnEntity> sn_list = snService.searchAll();
			JSONObject p_Obj = snService.entitiesToJson(sn_list);

			// Step4-2 .包裝資料
			r_allData = snService.ajaxRspJson(p_Obj, frontData, "訪問成功!!");
		} else {
			// Step4-1 .登出 && 包裝 錯誤 資料
			r_allData = snService.fail_ajaxRspJson(frontData, "你沒有權限!!");
		}

		// Step6.結果回傳
		System.out.println(r_allData);
		return r_allData.toString();
	}

	/**
	 * 
	 * 基本載入 /查詢
	 * 
	 * @param ajaxJSON 限定用JSON
	 */
	@ResponseBody
	@RequestMapping(value = "/production_sn_update", method = {
			RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String update_basic_sn(@RequestBody String ajaxJSON) {
		System.out.println("---controller - production_sn");
		// Step1.取出 session 訊息 & 檢查權限
		List<GroupEntity> group = loginService.getSessionGroupBean();
		boolean checkPermission = loginService.checkPermission(group, SYS_F, "01111111");

		// Step3.建立回傳元素
		JSONObject r_allData = new JSONObject();
		JsonDataModel data = new JsonDataModel();
		JSONObject frontData = (JSONObject) data.frontToBack(ajaxJSON).get("data");

		// Step4.檢查許可權 & 輸入物件
		if (checkPermission) {
			// Step4-1 .DB 取出 正確 資料
			ArrayList<ProductionSnEntity> sn_list_new =  snService.jsonToEntities(frontData.getJSONArray("content"));
			snService.deleteAllSn();
			snService.addedSn(sn_list_new);
			ArrayList<ProductionSnEntity> sn_list = snService.searchAll();
			JSONObject p_Obj = snService.entitiesToJson(sn_list);

			// Step4-2 .包裝資料
			r_allData = snService.ajaxRspJson(p_Obj, frontData, "訪問成功!!");
		} else {
			// Step4-1 .登出 && 包裝 錯誤 資料
			r_allData = snService.fail_ajaxRspJson(frontData, "你沒有權限!!");
		}

		// Step6.結果回傳
		System.out.println(r_allData);
		return r_allData.toString();
	}

}
