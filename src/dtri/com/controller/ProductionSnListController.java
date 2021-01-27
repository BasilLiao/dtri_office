package dtri.com.controller;

import java.time.LocalDate;
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
import dtri.com.db.entity.ProductionRecordsEntity;
import dtri.com.models.Fm_Time_Model;
import dtri.com.models.JsonDataModel;
import dtri.com.service.LoginService;
import dtri.com.service.ProductionSnListService;

@Controller
public class ProductionSnListController {

	@Autowired
	LoginService loginService;

	@Autowired
	ProductionSnListService recordsListService;
	// 功能
	final static String SYS_F = "production_sn_list.do";

	/**
	 * 
	 * 基本載入 /查詢
	 * 
	 * @param ajaxJSON 限定用JSON
	 */
	@ResponseBody
	@RequestMapping(value = "/production_sn_list", method = {
			RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String search_production(@RequestBody String ajaxJSON) {
		System.out.println("---controller - production_sn_list");
		// Step1.取出 session 訊息 & 檢查權限
		List<GroupEntity> group = loginService.getSessionGroupBean();
		boolean checkPermission = loginService.checkPermission(group, SYS_F, "01000001");

		// Step2.解析內容-檢查 -> 取出內容物
		JsonDataModel data = new JsonDataModel();
		JSONObject frontData = (JSONObject) data.frontToBack(ajaxJSON).get("data");
		System.out.println(frontData);

		// Step3.建立回傳元素
		JSONObject r_allData = new JSONObject();
		int page_nb = 0, page_total = 9999;
		// Step4.檢查許可權 & 輸入物件
		if (checkPermission) {
			// Step4-1 .DB 取出 正確 資料
			ProductionRecordsEntity entity = new ProductionRecordsEntity();

			if (frontData.get("content") != null && !frontData.get("content").equals("")) {
				entity = recordsListService.jsonToEntities(frontData.getJSONObject("content"));

			} else {
				LocalDate currentdate = LocalDate.now();
				String d_s = currentdate.getYear() + "-01-01";
				entity.setSys_create_date(Fm_Time_Model.toDate(d_s));
				String d_e = currentdate.getYear() + "-12-31";
				entity.setSys_modify_date(Fm_Time_Model.toDate(d_e));
			}
			// Step4-2 .DB 查詢 正確 資料
			// 取得換頁碼 如果沒有 0

			List<ProductionRecordsEntity> p_Entities = recordsListService.searchAll(entity, page_nb, page_total);
			JSONObject p_Obj = recordsListService.entitiesToJson(p_Entities);

			// Step4-2 .包裝資料
			r_allData = recordsListService.ajaxRspJson(p_Obj, frontData, "訪問成功!!");
		} else {
			// Step4-1 .登出 && 包裝 錯誤 資料
			r_allData = recordsListService.fail_ajaxRspJson(frontData, "你沒有權限!!");
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
	@RequestMapping(value = "/production_sn_list_update", method = {
			RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String update_basic_sn(@RequestBody String ajaxJSON) {
		System.out.println("---controller - production_sn_list_update");
		// Step1.取出 session 訊息 & 檢查權限
		List<GroupEntity> group = loginService.getSessionGroupBean();
		boolean checkPermission = loginService.checkPermission(group, SYS_F, "01001111");

		// Step3.建立回傳元素
		int page_nb = 0, page_total = 9999;
		JSONObject r_allData = new JSONObject();
		JsonDataModel data = new JsonDataModel();
		JSONObject frontData = (JSONObject) data.frontToBack(ajaxJSON).get("data");

		// Step4.檢查許可權 & 輸入物件
		if (checkPermission) {
			// Step4-1 .DB 取出 正確 資料
			ProductionRecordsEntity entity = new ProductionRecordsEntity();
			ArrayList<ProductionRecordsEntity> entitys = new ArrayList<ProductionRecordsEntity>();
			if (frontData.get("content") != null && !frontData.get("content").equals("")) {
				entity = recordsListService.jsonToEntities(frontData.getJSONObject("content"));
				entitys = recordsListService
						.jsonToEntities_added(frontData.getJSONObject("content").getJSONArray("sn_add"));
				// 寫入 資料
				recordsListService.addRecords(entitys);
				entitys = recordsListService
						.jsonToEntities_delete(frontData.getJSONObject("content").getJSONArray("sn_delete"));
				recordsListService.deleteEntitys(entitys);
			} else {
				LocalDate currentdate = LocalDate.now();
				String d_s = currentdate.getYear() + "-01-01";
				entity.setSys_create_date(Fm_Time_Model.toDate(d_s));
				String d_e = currentdate.getYear() + "-12-31";
				entity.setSys_modify_date(Fm_Time_Model.toDate(d_e));
			}
			// Step4-2 .DB 查詢 正確 資料
			// 取得換頁碼 如果沒有 0

			List<ProductionRecordsEntity> p_Entities = recordsListService.searchAll(entity, page_nb, page_total);
			JSONObject p_Obj = recordsListService.entitiesToJson(p_Entities);

			// Step4-2 .包裝資料
			r_allData = recordsListService.ajaxRspJson(p_Obj, frontData, "訪問成功!!");
		} else {
			// Step4-1 .登出 && 包裝 錯誤 資料
			r_allData = recordsListService.fail_ajaxRspJson(frontData, "你沒有權限!!");
		}
		// Step6.結果回傳
		System.out.println(r_allData);
		return r_allData.toString();
	}

}
