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
import dtri.com.db.entity.UserEntity;
import dtri.com.models.JsonDataModel;
import dtri.com.service.BoxBarcodeService;
import dtri.com.service.LoginService;

@Controller
public class BoxBarcodeController {

	@Autowired
	LoginService loginService;
	@Autowired
	BoxBarcodeService boxbarcodeService;
	// 功能
	final static String SYS_F = "box_barcode.do";

	/**
	 * 
	 * 基本載入 /查詢
	 * 
	 * @param ajaxJSON 限定用JSON
	 */
	@ResponseBody
	@RequestMapping(value = "/box_barcode", method = { RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String search_basic_user(@RequestBody String ajaxJSON) {
		System.out.println("---controller - box_barcode");
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
			UserEntity entity = new UserEntity();
			entity.setAccount(loginService.getSessionUserBean().getAccount());
			UserEntity p_Entities = boxbarcodeService.searchUser(entity);
			JSONObject p_Obj = boxbarcodeService.entitiesToJson(p_Entities);

			// Step4-2 .包裝資料
			r_allData = boxbarcodeService.ajaxRspJson(p_Obj, frontData, "訪問成功!!");
		} else {
			// Step4-1 .登出 && 包裝 錯誤 資料
			r_allData = boxbarcodeService.fail_ajaxRspJson(frontData, "你沒有權限!!");
		}

		// Step6.結果回傳
		System.out.println(r_allData);
		return r_allData.toString();
	}
}
