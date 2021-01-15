package dtri.com.service;

import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dtri.com.bean.JsonObjBean;
import dtri.com.bean.JsonTemplateBean;
import dtri.com.db.entity.SoftwareVersionEntity;
import dtri.com.db.pgsql.dao.SoftwareVersionDao;
import dtri.com.models.Fm_Time_Model;
import dtri.com.models.JsonDataModel;

@Transactional
@Service
public class SoftwareVersionService {
	@Autowired
	private SoftwareVersionDao svDao;
	@Autowired
	private LoginService loginService;

	/**
	 * @param entity 查詢使用者資料
	 * @return 查詢後清單
	 * 
	 **/
	public List<SoftwareVersionEntity> searchSoftwareVersion(SoftwareVersionEntity entity,int offset,int page_total) {
		String all_limit = " OFFSET " + offset + " LIMIT " + page_total;
		List<SoftwareVersionEntity> list = svDao.queryAll(entity,all_limit);
		return list;
	}

	/** 新增 **/
	public Boolean addedSoftwareVers(SoftwareVersionEntity entity) {
		Boolean check = false;
		if (svDao.addedOne(entity) > 0)
			check = true;
		return check;
	}

	/** 更新 **/
	public Boolean updateSoftwareVers(SoftwareVersionEntity entity) {
		Boolean check = true;
		if (svDao.updateOne(entity) < 1) {
			check = false;
		}
		return check;
	}

	/** 移除 **/
	public Boolean deleteSoftwareVers(SoftwareVersionEntity entity) {
		Boolean check = false;
		svDao.deleteOne(entity);
		return check;

	}

	/** JSON to 清單 **/
	public SoftwareVersionEntity jsonToEntities(JSONObject content) {
		SoftwareVersionEntity entity = new SoftwareVersionEntity();
		entity.setSys_create_date(new Date());
		entity.setSys_create_user(loginService.getSessionUserBean().getAccount());
		entity.setSys_modify_date(new Date());
		entity.setSys_modify_user(loginService.getSessionUserBean().getAccount());

		if (!content.isNull("id") && !content.get("id").equals(""))
			entity.setId(content.getInt("id"));

		if (!content.isNull("client_name"))
			entity.setClient_name(content.getString("client_name"));
		if (!content.isNull("product_model_in"))
			entity.setProduct_model_in(content.getString("product_model_in"));
		if (!content.isNull("bom_id"))
			entity.setBom_id(content.getInt("bom_id"));
		if (!content.isNull("bom_product_id"))
			entity.setBom_product_id(content.getString("bom_product_id"));
		if (!content.isNull("mb_ver"))
			entity.setMb_ver(content.getString("mb_ver"));

		if (!content.isNull("mb_ver_ecn"))
			entity.setMb_ver_ecn(content.getString("mb_ver_ecn"));
		if (!content.isNull("bios"))
			entity.setBios(content.getString("bios"));
		if (!content.isNull("ec"))
			entity.setEc(content.getString("ec"));
		if (!content.isNull("nvram"))
			entity.setNvram(content.getString("nvram"));
		if (!content.isNull("os"))
			entity.setOs(content.getString("os"));

		if (!content.isNull("note"))
			entity.setNote(content.getString("note"));
		if (!content.isNull("note1"))
			entity.setNote1(content.getString("note1"));
		if (!content.isNull("note2"))
			entity.setNote2(content.getString("note2"));

		return entity;
	}

	/**
	 * 清單 to JSON
	 * 
	 * @param p_Entities 使用者清單資料
	 **/
	public JSONObject entitiesToJson(List<SoftwareVersionEntity> p_Entities) {
		JSONObject list = new JSONObject();
		JSONArray jsonAll = new JSONArray();
		// 標題
		JSONArray jsonArray = new JSONArray();
		jsonArray.put("建立時間");
		jsonArray.put("建立者");
		jsonArray.put("修改時間");
		jsonArray.put("修改者");

		jsonArray.put("BOM料號");
		jsonArray.put("客戶名稱");
		// 內容
		jsonArray.put("M/B版本");
		jsonArray.put("ECN");
		jsonArray.put("BIOS");
		jsonArray.put("EC");
		jsonArray.put("NVRAM");
		jsonArray.put("MODEL");
		jsonArray.put("OS");
		// ID
		jsonArray.put("id");
		jsonArray.put("bom_id");
		// 備註
		jsonArray.put("note");
		jsonArray.put("note1");
		jsonArray.put("note2");
		jsonAll.put(jsonArray);
		// 內容
		for (SoftwareVersionEntity entity : p_Entities) {
			jsonArray = new JSONArray();
			jsonArray.put(Fm_Time_Model.to_yMd_Hms(entity.getSys_create_date()));
			jsonArray.put(entity.getSys_create_user());
			jsonArray.put(Fm_Time_Model.to_yMd_Hms(entity.getSys_modify_date()));
			jsonArray.put(entity.getSys_modify_user());

			jsonArray.put(entity.getBom_product_id());
			jsonArray.put(entity.getClient_name());
			// 內容
			jsonArray.put(entity.getMb_ver());
			jsonArray.put(entity.getMb_ver_ecn());
			jsonArray.put(entity.getBios());
			jsonArray.put(entity.getEc());
			jsonArray.put(entity.getNvram());
			jsonArray.put(entity.getProduct_model_in());
			jsonArray.put(entity.getOs());
			// ID
			jsonArray.put(entity.getId());
			jsonArray.put(entity.getBom_id());
			// 備註
			jsonArray.put(entity.getNote());
			jsonArray.put(entity.getNote1());
			jsonArray.put(entity.getNote2());
			jsonAll.put(jsonArray);
		}
		list.put("list", jsonAll);

		return list;
	}

	/**
	 * 統整回傳包裝
	 * 
	 * @param p_Obj     回傳查詢資料
	 * @param frontData 前端傳入的"控制"資訊 回傳 成功
	 * @param r_Message 回傳訊息
	 **/
	public JSONObject ajaxRspJson(JSONObject p_Obj, JSONObject frontData, String r_Message) {
		JsonDataModel data = new JsonDataModel();
		JSONObject r_allData = new JSONObject();
		JsonObjBean objBean = new JsonObjBean();
		JsonTemplateBean templateBean = new JsonTemplateBean();

		templateBean.setWebPageBody("html/body/software_version_body.html");
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

	/** 檢查換頁碼 如果沒有固定為從0開始 **/
	public int jsonToPageNb(JSONObject content) {
		if (!content.isNull("page_nb") && !content.get("page_nb").equals("")) {
			return content.getInt("page_nb");
		}
		return 0;
	}

	/** 檢查換頁碼 如果沒有固定為100筆資料 **/
	public int jsonToPageTotal(JSONObject content) {
		if (!content.isNull("page_total") && !content.get("page_total").equals("")) {
			return content.getInt("page_total");
		}
		return 100;
	}
}
