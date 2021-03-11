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

import dtri.com.bean.BomProductGroupBean;
import dtri.com.db.entity.BomProductEntity;
import dtri.com.db.entity.GroupEntity;
import dtri.com.db.entity.UserEntity;
import dtri.com.models.JsonDataModel;
import dtri.com.service.BomProductService;
import dtri.com.service.LoginService;

@Controller
public class BomProductController {

	@Autowired
	LoginService loginService;
	@Autowired
	BomProductService productService;
	// 功能
	final static String SYS_F = "bom_product.do";

	/**
	 * 
	 * 基本載入 /查詢
	 * 
	 * @param ajaxJSON 限定用JSON
	 */
	@ResponseBody
	@RequestMapping(value = "/bom_product", method = {
			RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String search_bom_product(@RequestBody String ajaxJSON) {
		System.out.println("---controller - bom_product");
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
			BomProductGroupBean bpg = new BomProductGroupBean();
			if (frontData.get("content") != null && !frontData.get("content").equals("")) {
				entity = productService.jsonToEntitiesSearch(frontData.getJSONObject("content"),
						frontData.getString("action"));
				// 取得換頁碼 如果沒有 0
				page_nb = productService.jsonToPageNb(frontData.getJSONObject("content"));
				page_total = productService.jsonToPageTotal(frontData.getJSONObject("content"));
			}
			bpg = productService.search(entity, page_nb, page_total);
			
			// 查詢過後 沒資料
			if (bpg.getBomProductEntities().size() < 1) {
				r_allData = productService.fail_ajaxRspJson(frontData, "無資料!!");
			} else {
				// Step4-2 .包裝資料
				JSONObject p_Obj = productService.entitiesToJson(bpg);
				r_allData = productService.ajaxRspJson(p_Obj, frontData, "訪問成功!!");

			}
		} else {
			// Step4-1 .登出 && 包裝 錯誤 資料
			r_allData = productService.fail_ajaxRspJson(frontData, "你沒有權限!!");
		}

		// Step6.結果回傳
		// System.out.println(r_allData);
		return r_allData.toString();
	}

	/**
	 * 新增
	 * 
	 * @param ajaxJSON 限定用JSON
	 */
	@ResponseBody
	@RequestMapping(value = "/added_bom_product", method = {
			RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String added_bom_type_item(@RequestBody String ajaxJSON) {
		System.out.println("---controller - added_bom_product");
		// Step2.取出 session 訊息 & 檢查權限( 新增 更新 )
		List<GroupEntity> group = loginService.getSessionGroupBean();
		boolean checkPermission = loginService.checkPermission(group, SYS_F, "01001001");

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
			BomProductEntity entity = new BomProductEntity();

			if (frontData.get("content") != null && !frontData.get("content").equals("")) {
				entity = productService
						.jsonToEntities(frontData.getJSONObject("content"), frontData.getString("action")).get(0);
				// 取得換頁碼 如果沒有 0
				page_nb = productService.jsonToPageNb(frontData.getJSONObject("content"));
				page_total = productService.jsonToPageTotal(frontData.getJSONObject("content"));
				// Step4-1 .DB 新增(項目群組) 正確 資料
				productService.added(entity);

				// Step4-2 .DB 查詢 正確 資料
				List<BomProductEntity> entitys = new ArrayList<BomProductEntity>();
				entitys = productService.jsonToEntitiesSearch(frontData.getJSONObject("content"),
						frontData.getString("action"));
				BomProductGroupBean bpg = new BomProductGroupBean();
				bpg = productService.search(entitys, page_nb, page_total);
				JSONObject p_Obj = productService.entitiesToJson(bpg);

				// Step4-2 .包裝資料
				r_allData = productService.ajaxRspJson(p_Obj, frontData, "新增成功!!");
			}
		} else {
			// Step4-1 .登出 && 包裝 錯誤 資料
			r_allData = productService.fail_ajaxRspJson(frontData, "你沒有權限!!");
		}

		// Step6.結果回傳
		// System.out.println(r_allData);
		return r_allData.toString();
	}

	/**
	 * 更新
	 * 
	 * @param ajaxJSON 限定用JSON
	 */
	@ResponseBody
	@RequestMapping(value = "/update_bom_product", method = {
			RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String update_bom_type_item(@RequestBody String ajaxJSON) {
		System.out.println("---controller - update_bom_product");
		// Step2.取出 session 訊息 & 檢查權限( 新增 更新 )
		List<GroupEntity> group = loginService.getSessionGroupBean();
		boolean checkPermission = loginService.checkPermission(group, SYS_F, "01000101");
		UserEntity user = loginService.getSessionUserBean();

		// Step1.解析內容-檢查 -> 取出內容物
		JsonDataModel data = new JsonDataModel();
		JSONObject frontData = (JSONObject) data.frontToBack(ajaxJSON).get("data");
		System.out.println(frontData);

		// Step3.建立回傳元素
		JSONObject r_allData = new JSONObject();
		int page_nb = 0, page_total = 100;

		// Step3-1 .DB 取出 正確 資料
		List<BomProductEntity> entitys = new ArrayList<BomProductEntity>();
		if (frontData.get("content") != null && !frontData.get("content").equals("")) {

			// Step3-2 .DB 修改類只有本人 建立 可改(如果:不是 全部權限 或 不是本人)=沒修改 權限
			entitys = productService.jsonToEntities(frontData.getJSONObject("content"), frontData.getString("action"));
			BomProductEntity one = productService.searchById(entitys.get(0).getId());
			System.out.println(entitys.get(0).getSys_modify_user());
			if (loginService.checkPermission(group, SYS_F, "11111111")
					|| user.getAccount().equals(one.getSys_create_user())) {
			} else {
				checkPermission = false;
			}
		} else {
			checkPermission = false;
		}

		// Step4.檢查許可權 & 輸入物件
		if (checkPermission) {
			// 取得換頁碼 如果沒有 0
			page_nb = productService.jsonToPageNb(frontData.getJSONObject("content"));
			page_total = productService.jsonToPageTotal(frontData.getJSONObject("content"));
			// Step4-1 .DB 更新 正確 資料
			productService.update(entitys.get(0));

			// Step4-2 .DB 查詢 正確 資料
			entitys = new ArrayList<BomProductEntity>();
			BomProductGroupBean bpg = new BomProductGroupBean();
			entitys = productService.jsonToEntitiesSearch(frontData.getJSONObject("content"),
					frontData.getString("action"));
			bpg = productService.search(entitys, page_nb, page_total);
			JSONObject p_Obj = productService.entitiesToJson(bpg);

			// Step4-3 .包裝資料
			r_allData = productService.ajaxRspJson(p_Obj, frontData, "更新成功!!");
		} else {

			// Step4-1 .登出 && 包裝 錯誤 資料
			r_allData = productService.fail_ajaxRspJson(frontData, "你沒有權限!!");
		}

		// Step5.結果回傳
		// System.out.println(r_allData);
		return r_allData.toString();
	}

	/**
	 * 移除
	 * 
	 * @param ajaxJSON 限定用JSON
	 */
	@ResponseBody
	@RequestMapping(value = "/delete_bom_product", method = {
			RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String delete_bom_type_item(@RequestBody String ajaxJSON) {
		System.out.println("---controller - delete_bom_product");
		// Step2.取出 session 訊息 & 檢查權限( 新增 更新 )
		List<GroupEntity> group = loginService.getSessionGroupBean();
		boolean checkPermission = loginService.checkPermission(group, SYS_F, "01000011");
		UserEntity user = loginService.getSessionUserBean();

		// Step1.解析內容-檢查 -> 取出內容物
		JsonDataModel data = new JsonDataModel();
		JSONObject frontData = (JSONObject) data.frontToBack(ajaxJSON).get("data");
		System.out.println(frontData);

		// Step3.建立回傳元素
		JSONObject r_allData = new JSONObject();
		int page_nb = 0, page_total = 0;

		// Step3-1 .DB 取出 正確 資料
		List<BomProductEntity> entitys = new ArrayList<BomProductEntity>();
		if (frontData.get("content") != null && !frontData.get("content").equals("")) {

			// Step3-2 .DB 移除類只有本人 建立 可改(如果:不是 全部權限 或 不是本人)=沒移除 權限
			entitys = productService.jsonToEntities(frontData.getJSONObject("content"), frontData.getString("action"));
			BomProductEntity one = productService.searchById(entitys.get(0).getId());
			System.out.println(entitys.get(0).getSys_modify_user());
			if (loginService.checkPermission(group, SYS_F, "11111111")
					|| user.getAccount().equals(one.getSys_create_user())) {
			} else {
				checkPermission = false;
			}
		} else {
			checkPermission = false;
		}

		// Step4.檢查許可權 & 輸入物件
		if (checkPermission) {

			// Step4-1 .DB 移除 正確 資料
			productService.delete(entitys.get(0));
			// 取得換頁碼 如果沒有 0
			page_nb = productService.jsonToPageNb(frontData.getJSONObject("content"));
			page_total = productService.jsonToPageTotal(frontData.getJSONObject("content"));

			// Step4-2 .DB 查詢 正確 資料
			entitys = new ArrayList<BomProductEntity>();
			entitys = productService.jsonToEntitiesSearch(frontData.getJSONObject("content"),
					frontData.getString("action"));
			BomProductGroupBean bpg = new BomProductGroupBean();
			bpg = productService.search(entitys, page_nb, page_total);
			JSONObject p_Obj = productService.entitiesToJson(bpg);
			// Step4-3 .包裝資料
			r_allData = productService.ajaxRspJson(p_Obj, frontData, "移除成功!!");
		} else {

			// Step4-1 .登出 && 包裝 錯誤 資料
			r_allData = productService.fail_ajaxRspJson(frontData, "你沒有權限!!");
		}

		// Step5.結果回傳
		// System.out.println(r_allData);
		return r_allData.toString();
	}
}
