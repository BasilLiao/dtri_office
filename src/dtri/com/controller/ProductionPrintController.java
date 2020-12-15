package dtri.com.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import dtri.com.bean.BomProductGroupBean;
import dtri.com.db.entity.BomProductEntity;
import dtri.com.db.entity.GroupEntity;
import dtri.com.db.entity.ProductionRecordsEntity;
import dtri.com.db.entity.SoftwareVersionEntity;
import dtri.com.models.JsonDataModel;
import dtri.com.service.LoginService;
import dtri.com.service.ProductionPrintService;
import dtri.com.service.ProductionRecordsService;
import dtri.com.service.SoftwareVersionService;

@Controller
public class ProductionPrintController {

	@Autowired
	LoginService loginService;
	@Autowired
	ProductionPrintService printService;
	@Autowired
	ProductionRecordsService recordsService;
	@Autowired
	SoftwareVersionService softwareVersionService;
	// 功能
	final static String SYS_F = "production_print.do";
	// log 訊息
	Logger logger = LogManager.getLogger();

	/**
	 * 
	 * 基本載入 /查詢 列印出檢查 流程卡
	 * 
	 * @param ajaxJSON 限定用JSON
	 */
	@ResponseBody
	@RequestMapping(value = "/production_print", method = {
			RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String search_bom_product(@RequestBody String ajaxJSON) {

		System.out.println("---controller - bom_print");
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
			List<BomProductEntity> entity = new ArrayList<BomProductEntity>();
			ProductionRecordsEntity entity2 = new ProductionRecordsEntity();
			BomProductGroupBean bpg = new BomProductGroupBean();
			List<ProductionRecordsEntity> bpg2 = new ArrayList<ProductionRecordsEntity>();
			List<SoftwareVersionEntity> bpg3 = new ArrayList<SoftwareVersionEntity>();
			entity2.setProduct_progress(2);// 要是生產流程的
			entity2.setProduct_status(1);// 不是作廢的

			if (frontData.get("content") != null && !frontData.get("content").equals("")) {
				entity = printService.jsonToEntities(frontData.getJSONObject("content"));
				entity2 = printService.jsonToEntities2(frontData.getJSONObject("content"));

				// 取得換頁碼 如果沒有 0
				page_nb = printService.jsonToPageNb(frontData.getJSONObject("content"));
				page_total = printService.jsonToPageTotal(frontData.getJSONObject("content"));
			}

			bpg = printService.search(entity, page_nb, page_total);
			bpg2 = printService.searchProduction(entity2, page_nb, page_total);
			bpg3 = softwareVersionService.searchSoftwareVersion(new SoftwareVersionEntity());
			JSONObject p_Obj = printService.entitiesToJson(bpg, bpg2,bpg3);

			// Step4-2 .包裝資料
			r_allData = printService.ajaxRspJson(p_Obj, frontData, "訪問成功!!");
		} else {
			// Step4-1 .登出 && 包裝 錯誤 資料
			r_allData = printService.fail_ajaxRspJson(frontData, "你沒有權限!!");
		}

		// Step6.結果回傳
		// System.out.println(r_allData);
		return r_allData.toString();
	}

	/**
	 * 
	 * 修改進度
	 * 
	 * @param ajaxJSON 限定用JSON
	 */
	@ResponseBody
	@RequestMapping(value = "/production_progress_update", method = {
			RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String production_progress(@RequestBody String ajaxJSON) {
		System.out.println("---controller - production_progress");
		// Step1.取出 session 訊息 & 檢查權限
		List<GroupEntity> group = loginService.getSessionGroupBean();
		boolean checkPermission = loginService.checkPermission(group, SYS_F, "01001101");

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
			List<SoftwareVersionEntity> bpg3 = new ArrayList<SoftwareVersionEntity>();
			ProductionRecordsEntity entity = new ProductionRecordsEntity();
			SoftwareVersionEntity entity_sv = new SoftwareVersionEntity();
			if (frontData.get("content") != null && !frontData.get("content").equals("")) {
				entity = printService.jsonToEntities2(frontData.getJSONObject("content"));
				entity_sv = softwareVersionService.jsonToEntities(frontData.getJSONObject("content"));
				page_nb = printService.jsonToPageNb(frontData.getJSONObject("content"));
				page_total = printService.jsonToPageTotal(frontData.getJSONObject("content"));
			}
			// 更新(如果更新不到該工單號 ->新的自動登記)
			boolean checked = recordsService.updateProgress(entity);
			if (!checked) {
				checked = recordsService.addRecords(frontData.getJSONObject("content"), 1);
			}

			// 登記 Or 更新 軟體版本
			if (checked) {
				SoftwareVersionEntity check_entity_sv = new SoftwareVersionEntity();
				check_entity_sv.setId(entity_sv.getId());
				check_entity_sv.setClient_name(entity_sv.getClient_name());
				check_entity_sv.setBom_product_id(entity_sv.getBom_product_id());
				if (softwareVersionService.searchSoftwareVersion(check_entity_sv).size() >= 1) {
					checked = softwareVersionService.updateSoftwareVers(entity_sv);
				} else {
					checked = softwareVersionService.addedSoftwareVers(entity_sv);
				}
			}

			// Step4-2 .DB 查詢 正確 資料
			// 取得換頁碼 如果沒有 0
			entity = new ProductionRecordsEntity();
			entity.setProduct_status(1);
			entity.setProduct_progress(2);
			List<ProductionRecordsEntity> p_Entities = printService.searchProduction(entity, page_nb, page_total);
			bpg3 = softwareVersionService.searchSoftwareVersion(new SoftwareVersionEntity());
			JSONObject p_Obj = printService.entitiesToJson(null, p_Entities,bpg3 );

			// Step4-2 .包裝資料(如果有登記過了)
			if (checked) {
				r_allData = printService.ajaxRspJson(p_Obj, frontData, "訪問成功!!");
			} else {
				r_allData = printService.fail_ajaxRspJson(frontData, "更新工單號失敗 異常!!");
			}
		} else {
			// Step4-1 .登出 && 包裝 錯誤 資料
			r_allData = printService.fail_ajaxRspJson(frontData, "你沒有權限!!");
		}

		// Step6.結果回傳
		System.out.println(r_allData);
		return r_allData.toString();
	}

}
