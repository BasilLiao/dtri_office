package dtri.com.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dtri.com.bean.JsonObjBean;
import dtri.com.bean.JsonTemplateBean;
import dtri.com.db.entity.ProductionRecordsEntity;
import dtri.com.db.pgsql.dao.ProductionRecordsDAO;
import dtri.com.models.Fm_Time_Model;
import dtri.com.models.JsonDataModel;

/**
 * @author Basil 登記生產紀錄
 * 
 * 
 **/
@Transactional
@Service
public class ProductionSnListService {

	@Autowired
	private LoginService loginService;
	@Autowired
	private ProductionRecordsDAO dao;

	/**
	 * @param entity
	 * @return 查詢後清單
	 * 
	 **/
	public List<ProductionRecordsEntity> searchAll(ProductionRecordsEntity entity, int offset, int page_total) {
		String all_limit = " OFFSET " + offset + " LIMIT " + page_total;
		List<ProductionRecordsEntity> list = dao.queryProductionRecords(entity, all_limit);
		return list;
	}

	/** 寫入 登入生產紀錄 **/
	public boolean addRecords(JSONObject json, int p_status) {
		boolean check = false;
		ProductionRecordsEntity recordsEntity = new ProductionRecordsEntity();

		recordsEntity.setSys_create_date(new Date());
		recordsEntity.setSys_create_user(loginService.getSessionUserBean().getAccount());
		recordsEntity.setSys_modify_date(new Date());
		recordsEntity.setSys_modify_user(loginService.getSessionUserBean().getAccount());

		recordsEntity.setId(json.getString("production_id"));
		recordsEntity.setOrder_id(json.getString("order_id"));
		recordsEntity.setClient_name(json.getString("client_name"));
		recordsEntity.setProduction_quantity(json.getInt("production_quantity"));
		recordsEntity.setProduct_model(json.getString("product_model"));
		recordsEntity.setBom_product_id(json.getString("bom_product_id"));
		recordsEntity.setBom_id(json.getInt("bom_id"));
		recordsEntity.setVersion_motherboard(json.getString("version_motherboard"));
		recordsEntity.setNote(json.getString("note"));
		recordsEntity.setCome_from(json.getString("come_from"));
		recordsEntity.setProduct_status(p_status);
		recordsEntity.setProduct_progress(json.getInt("product_progress"));

		if (dao.beforeCheckAddOne(json.getString("production_id")) == null) {
			dao.addOne(recordsEntity);
			check = true;
		}

		return check;
	}

	/** 寫入 登入生產紀錄(自訂義) **/
	public boolean addRecords(ArrayList<ProductionRecordsEntity> entitys) {
		boolean check = false;
		// 寫入紀錄
		for (ProductionRecordsEntity productionRecordsEntity : entitys) {
			dao.addOne(productionRecordsEntity);
			check = true;
		}
		// 更新SN
		for (ProductionRecordsEntity productionRecordsEntity : entitys) {
			dao.updateOneProgress(productionRecordsEntity);
		}
		return check;
	}

	/** 更新單據進度 **/
	public boolean updateProgress(ProductionRecordsEntity entity) {
		boolean check = false;
		entity.setSys_modify_date(new Date());
		entity.setSys_modify_user(loginService.getSessionUserBean().getAccount());
		if (dao.updateOneProgress(entity) == 1) {
			check = true;
		}
		return check;
	}

	/** 更新單據狀態 **/
	public boolean updateEntity(ProductionRecordsEntity entity) {
		boolean check = false;
		entity.setSys_modify_date(new Date());
		entity.setSys_modify_user(loginService.getSessionUserBean().getAccount());
		if (dao.updateOneStatus(entity) == 1) {
			check = true;
		}
		return check;
	}

	/** 限定移除 **/
	public boolean deleteEntitys(ArrayList<ProductionRecordsEntity> entitys) {
		boolean check = false;

		for (ProductionRecordsEntity entity : entitys) {
			if (dao.deleteOneProgress(entity) == 1) {
				check = true;
			}
		}
		return check;
	}

	/** JSON to 清單 **/
	public ProductionRecordsEntity jsonToEntities(JSONObject content) {
		ProductionRecordsEntity entity = new ProductionRecordsEntity();

		// 時間區間(如果沒有 就抓取今年)
		if (!content.isNull("s_s_d") && !content.getString("s_s_d").equals("")) {
			entity.setSys_create_date(Fm_Time_Model.toDate(content.getString("s_s_d")));
		}
		// 時間區間(如果沒有 就抓取今年)
		if (!content.isNull("s_e_d") && !content.getString("s_e_d").equals("")) {
			entity.setSys_modify_date(Fm_Time_Model.to_count(1, Fm_Time_Model.toDate(content.getString("s_e_d"))));
		}
		// 工單號?
		if (!content.isNull("s_p_id") && !content.getString("s_p_id").equals(""))
			entity.setId(content.getString("s_p_id"));
		// 類型? 產品SN序號
		if (!content.isNull("s_sn") && !content.getString("s_sn").equals(""))
			entity.setProduct_start_sn(content.getString("s_sn"));

		return entity;
	}

	/** JSON to 清單 內容(移除) **/
	public ArrayList<ProductionRecordsEntity> jsonToEntities_delete(JSONArray content) {
		ArrayList<ProductionRecordsEntity> entitys = new ArrayList<ProductionRecordsEntity>();
		content.forEach(s -> {
			String id = (String) s;
			ProductionRecordsEntity entity = new ProductionRecordsEntity();
			entity.setId(id);
			entitys.add(entity);
		});

		return entitys;
	}

	/** JSON to 清單 內容(添加) **/
	public ArrayList<ProductionRecordsEntity> jsonToEntities_added(JSONArray content) {
		ArrayList<ProductionRecordsEntity> entitys = new ArrayList<ProductionRecordsEntity>();
		content.forEach(s -> {
			JSONObject one = new JSONObject();
			ProductionRecordsEntity entity = new ProductionRecordsEntity();
			one = (JSONObject) s;
			entity.setSys_create_date(new Date());
			entity.setSys_create_user(loginService.getSessionUserBean().getAccount());
			entity.setSys_modify_date(Fm_Time_Model.toDate(one.getString("i_date")));
			entity.setSys_modify_user(loginService.getSessionUserBean().getAccount());
			entity.setId(one.getString("i_id"));
			entity.setClient_name(one.getString("i_client"));

			entity.setProduction_quantity(one.getInt("i_sn_number"));
			entity.setProduct_model(one.getString("i_sn_model"));
			entity.setProduct_start_sn(one.getString("i_sn_start"));
			entity.setProduct_end_sn(one.getString("i_sn_end"));
			entity.setCome_from("自訂紀錄");
			entity.setOrder_id("自訂紀錄");
			entity.setBom_id(0);
			entity.setBom_product_id("自訂紀錄");
			entity.setVersion_motherboard("自訂紀錄");
			entity.setProduct_status(2);
			entity.setProduct_progress(5);
			entity.setNote("自訂紀錄");

			entitys.add(entity);
		});

		return entitys;
	}

	/** 清單 to JSON **/
	public JSONObject entitiesToJson(List<ProductionRecordsEntity> bpg) {
		JSONObject list = new JSONObject();
		JSONArray jsonAll = new JSONArray();

		// 標題
		JSONArray jsonArray = new JSONArray();
		jsonArray.put("建立時間");
		jsonArray.put("建立者");
		jsonArray.put("修改時間");
		jsonArray.put("修改者");

		jsonArray.put("工單號碼");
		jsonArray.put("BOM料號");
		jsonArray.put("生產數量");
		jsonArray.put("進度");
		jsonArray.put("狀態");

		jsonAll.put(jsonArray);
		// 內容 產品清單
		for (ProductionRecordsEntity entity : bpg) {
			jsonArray = new JSONArray();
			jsonArray.put(Fm_Time_Model.to_yMd_Hms(entity.getSys_create_date()));
			jsonArray.put(entity.getSys_create_user());
			jsonArray.put(Fm_Time_Model.to_yMd_Hms(entity.getSys_modify_date()));
			jsonArray.put(entity.getSys_modify_user());

			jsonArray.put(entity.getId());
			jsonArray.put(entity.getBom_product_id());
			jsonArray.put(entity.getProduction_quantity());
			jsonArray.put(entity.getProduct_progress());

			jsonArray.put(entity.getOrder_id());
			jsonArray.put(entity.getClient_name());
			jsonArray.put(entity.getProduct_model());
			jsonArray.put(entity.getVersion_motherboard());
			jsonArray.put(entity.getNote());
			jsonArray.put(entity.getCome_from());
			jsonArray.put(entity.getProduct_status());
			jsonArray.put(entity.getProduct_start_sn());
			jsonArray.put(entity.getProduct_end_sn());

			jsonAll.put(jsonArray);
		}

		list.put("item_list", jsonAll);
		return list;
	}

	/**
	 * 統整回傳包裝
	 * 
	 * @param p_Obj     回傳查詢資料
	 * @param frontData 前端傳入的"控制"資訊 回傳 成功
	 **/
	public JSONObject ajaxRspJson(JSONObject p_Obj, JSONObject frontData, String r_Message) {
		JsonDataModel data = new JsonDataModel();
		JSONObject r_allData = new JSONObject();
		JsonObjBean objBean = new JsonObjBean();
		JsonTemplateBean templateBean = new JsonTemplateBean();

		templateBean.setWebPageBody("html/body/production_sn_list_body.html");
		templateBean.setBodyData(p_Obj);

		objBean.setR_cellBackName(frontData.getString("cellBackName"));
		objBean.setR_action(frontData.getString("action"));
		objBean.setR_status(true);
		objBean.setR_cellBackOrder(frontData.getString("cellBackOrder"));
		objBean.setR_message(r_Message);

		data.setR_JsonData(objBean);
		data.setR_template(templateBean);
		r_allData = data.getR_allData();
		return r_allData;
	}

	/**
	 * 統整回傳包裝
	 * 
	 * @param p_Obj     回傳查詢資料
	 * @param frontData 前端傳入的"控制"資訊 回傳 成功
	 **/
	public JSONObject fail_ajaxRspJson(JSONObject frontData, String r_Message) {
		JsonDataModel data = new JsonDataModel();
		JSONObject r_allData = new JSONObject();
		JsonObjBean objBean = new JsonObjBean();
		JsonTemplateBean templateBean = new JsonTemplateBean();

		objBean.setR_cellBackName(frontData.getString("cellBackName"));
		objBean.setR_action(frontData.getString("action"));
		objBean.setR_status(false);
		objBean.setR_cellBackOrder(frontData.getString("cellBackOrder"));
		objBean.setR_message(r_Message);
		// Step5.包裝
		data.setR_JsonData(objBean);
		data.setR_template(templateBean);
		r_allData = data.getR_allData();
		return r_allData;
	}

}
