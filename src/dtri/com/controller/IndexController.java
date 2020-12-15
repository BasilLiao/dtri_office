package dtri.com.controller;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import dtri.com.db.entity.GroupEntity;
import dtri.com.db.entity.UserEntity;
import dtri.com.models.JsonDataModel;
import dtri.com.service.IndexService;
import dtri.com.service.LoginService;

@Controller
public class IndexController {

	@Autowired
	LoginService loginService;
	@Autowired
	IndexService indexService;
	// 功能
	final static String SYS_F = "index.do";

	/**
	 * 第一次登入
	 */
	@RequestMapping(value = "/index", method = { RequestMethod.POST })
	public ModelAndView loginCheck() {
		System.out.println("---controller - loginCheck");
		// Step1.取出 session 訊息 & 檢查權限
		UserEntity user = loginService.getSessionUserBean();
		List<GroupEntity> group = loginService.getSessionGroupBean();
		boolean checkPermission = loginService.checkPermission(group, SYS_F, "01000000");

		// Step2.建立回傳物件
		JSONObject r_allData = new JSONObject();

		// Step3.檢查許可權 & 輸入物件
		if (checkPermission) {
			JSONObject p_Obj = indexService.entitiesToJson(group,user);
			r_allData = indexService.ajaxRspJson(p_Obj, null, group, user);
		
		} else {
			loginService.sessionLogout();
			// Step4.包裝
			r_allData = indexService.fail_ajaxRspJson(null);
		}
		// Step5.回傳
		System.out.println(r_allData);
		return new ModelAndView("main", "allData", r_allData);
	}

	/**
	 * 回首頁
	 * 
	 * @param ajaxJSON 限定用JSON
	 */
	@ResponseBody
	@RequestMapping(value = "/index", method = { RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String homeCheck(@RequestBody String ajaxJSON) {
		System.out.println("---controller - homeCheck");
		// Step1.解析內容-檢查 -> 取出內容物
		JsonDataModel data = new JsonDataModel();
		JSONObject frontData = (JSONObject) data.frontToBack(ajaxJSON).get("data");
		System.out.println(frontData);

		// Step2.取出 session 訊息 & 檢查權限
		List<GroupEntity> group = loginService.getSessionGroupBean();
		boolean checkPermission = loginService.checkPermission(group, SYS_F, "01000000");

		// Step3.建立回傳元素
		JSONObject r_allData = new JSONObject();

		// Step4.檢查許可權 & 輸入物件
		if (checkPermission) {
			// Step5.包裝
			r_allData = indexService.ajaxRspJson(null, frontData, group, null);
		} else {
			loginService.sessionLogout();
			r_allData = indexService.fail_ajaxRspJson(null);
		}
		// Step6.結果回傳
		System.out.println(r_allData);
		return r_allData.toString();
	}

	/**
	 * 登出
	 * 
	 * @param ajaxJSON 限定用JSON
	 */
	@ResponseBody
	@RequestMapping(value = "/logout", method = { RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String logoutCheck(@RequestBody String ajaxJSON) {
		System.out.println("---controller - logoutCheck");
		// Step1.解析內容-檢查 -> 取出內容物
		JsonDataModel data = new JsonDataModel();
		JSONObject frontData = (JSONObject) data.frontToBack(ajaxJSON).get("data");

		// Step2.取出 session 訊息 & 檢查權限
		boolean checkPermission = true;

		// Step3.建立回傳JSON
		JSONObject r_allData = new JSONObject();
		System.out.println(frontData);

		// Step4.檢查許可權 & 輸入物件
		if (checkPermission) {
			// Step5.包裝
			r_allData = indexService.ajaxRspJsonLogout(null, frontData);
		} else {
			r_allData = indexService.fail_ajaxRspJson(null);
		}
		loginService.sessionLogout();

		// Step6.結果回傳
		System.out.println(r_allData);
		return r_allData.toString();
	}
	/**
	 * 更新session
	 * 
	 * @param ajaxJSON 限定用JSON
	 */
	@ResponseBody
	@RequestMapping(value = "/sessionUpdate", method = { RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String sessionCheck(@RequestBody String ajaxJSON) {
		System.out.println("---controller - sessionUpdate");
		// Step1.響應一下
		JSONObject r_allData = new JSONObject();
		r_allData.put("r_message", "延長登入 600 秒 !!");
		return r_allData.toString();
	}
}
