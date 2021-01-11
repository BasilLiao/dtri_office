package dtri.com.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dtri.com.bean.JsonObjBean;
import dtri.com.bean.JsonTemplateBean;
import dtri.com.db.entity.ProductionSnEntity;
import dtri.com.db.pgsql.dao.ProductionSnDao;
import dtri.com.models.JsonDataModel;

@Transactional
@Service
public class ProductionSnService {
	@Autowired
	private ProductionSnDao snDao;
	@Autowired
	private LoginService loginService;

	/**
	 * @param entity 產品身分 SN 資料
	 * @return 查詢後清單
	 * 
	 **/
	public ArrayList<ProductionSnEntity> searchAll() {
		ArrayList<ProductionSnEntity> list = (ArrayList<ProductionSnEntity>) snDao.queryProductionSn();
		return list;
	}

	/** 添加 SN序列 **/
	public Boolean addedSn(ArrayList<ProductionSnEntity> entity) {
		Boolean check = false;
		for (ProductionSnEntity productionSnEntity : entity) {
			snDao.addedProductionSn(productionSnEntity);
			check = true;
		}
		return check;
	}

	/** 移除 ALL SN序列 **/
	public Boolean deleteAllSn() {
		Boolean check = false;
		snDao.deleteProductionSn();
		return check;
	}

	/** 更新 最新SN序列 **/
	public Boolean updateSn(ProductionSnEntity entity) {
		Boolean check = false;
		if (snDao.updateProductionSnById(entity) > 0) {
			snDao.updateProductionSn(entity);
			check = true;
		}
		return check;
	}

	/** 更新 最新SN 項目 **/
	public Boolean updateSnById(ProductionSnEntity entity) {
		Boolean check = false;
		if (snDao.updateProductionSnById(entity) > 0) {
			check = true;
		}
		return check;
	}

	// queryProductionSnContent 取得最新序列
	public ProductionSnEntity search_sn_content() {
		ProductionSnEntity one_Content = snDao.queryProductionSnContent();
		return one_Content;
	}

	/**
	 * 解析
	 * 
	 * @param entity 規則
	 * @param total  總數
	 **/
	public JSONObject analyze_Sn(ArrayList<ProductionSnEntity> entity, int total) {
		JSONObject obj = new JSONObject();
		String sn_item = "";
		ArrayList<String> sn_list = new ArrayList<String>();
		// 可辨識規則
		ProductionSnEntity sn_YYWW = new ProductionSnEntity();
		ProductionSnEntity sn_000 = new ProductionSnEntity();

		// 取出 現在(動態部分)序列規則
		for (ProductionSnEntity s : entity) {
			sn_item += s.getSn_value();
		}

		entity.stream().forEach(s -> {
			if (s.getSn_value().equals("[YYWW]")) {
				sn_YYWW.setSn_id(s.getSn_id());
				sn_YYWW.setSn_name(s.getSn_name());// 起始值
				sn_YYWW.setSn_value(s.getSn_value());// 規則
			}
			if (s.getSn_value().equals("[000]")) {
				sn_000.setSn_id(s.getSn_id());
				sn_000.setSn_name(s.getSn_name());// 起始值
				sn_000.setSn_value(s.getSn_value());// 規則
			}
		});

		// 生產產品序號
		String sn_for_item = sn_item;
		String sn_000_new = "";
		String sn_YYWW_new = "";
		for (int i = 0; i < total; i++) {

			// 流水號
			sn_000_new = get000(sn_000.getSn_name());
			// 如果數字為0 則在 年週期 加1(進位)
			if (sn_000_new.equals("000")) {
				sn_YYWW.setSn_name("" + (Integer.parseInt(sn_YYWW.getSn_name()) + 1));
				sn_000_new = String.format("%0" + sn_000_new.length() + "d", 1);
			}
			sn_for_item = sn_item.replace(sn_000.getSn_value(), sn_000_new);
			sn_000.setSn_name(sn_000_new);

			// 年周
			sn_YYWW_new = getYYWW(sn_YYWW.getSn_name());
			sn_for_item = sn_for_item.replace(sn_YYWW.getSn_value(), sn_YYWW_new);
			sn_YYWW.setSn_name(sn_YYWW_new);

			sn_list.add(sn_for_item);
		}
		obj.put("sn_list", sn_list);
		obj.put("sn_YYWW", sn_YYWW);
		obj.put("sn_000", sn_000);
		return obj;
	}

	// 規則 流水號+1
	public String get000(String nb) {
		int l = nb.length();
		int s = Integer.parseInt(nb);
		s += 1;
		String nb_new =  String.format("%0" + l + "d", s);
		if(nb_new.length()>l) {
			nb_new = nb_new.substring(1, 4);
		}
		return nb_new;
	}

	// 規則 取得年周
	public String getYYWW(String old_YYWW) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		// 如果發現 舊的大於 新的 年週期 ,則保持舊的周期數 ,反之在使用新的周期數
		String week = String.format("%02d", (cal.get(Calendar.WEEK_OF_YEAR)-1));
		String year = (cal.get(Calendar.YEAR) + "").substring(2, 4);
		int new_YYWW = Integer.parseInt(year + week);
		if (new_YYWW < Integer.parseInt(old_YYWW)) {
			return old_YYWW + "";
		}
		return new_YYWW + "";
	}

	/** JSON to 清單 **/
	public ArrayList<ProductionSnEntity> jsonToEntities(JSONArray content) {
		ArrayList<ProductionSnEntity> entitys = new ArrayList<ProductionSnEntity>();
		content.forEach(i -> {
			JSONObject one = new JSONObject();
			ProductionSnEntity entity = new ProductionSnEntity();
			one = (JSONObject) i;
			entity.setSys_create_date(new Date());
			entity.setSys_create_user(loginService.getSessionUserBean().getAccount());
			entity.setSys_modify_date(new Date());
			entity.setSys_modify_user(loginService.getSessionUserBean().getAccount());
			if (!one.isNull("sn_group_sort") && one.getInt("sn_group_sort") >= 0) {
				entity.setSn_id(one.getInt("sn_id"));
				entity.setSn_group(one.getInt("sn_group_sort"));
				entity.setSn_group_name(one.getString("sn_group_name"));
				entity.setSn_group_sort(one.getInt("sn_group_sort"));
			}
			if (!one.isNull("sn_name") && !one.get("sn_name").equals("")) {
				entity.setSn_name(one.getString("sn_name"));
				entity.setSn_value(one.getString("sn_value"));
			}
			entitys.add(entity);
		});
		return entitys;
	}

	/**
	 * 清單 to JSON
	 * 
	 * @param p_Entities 使用者清單資料
	 **/
	public JSONObject entitiesToJson(ArrayList<ProductionSnEntity> sn_list) {
		// 全部
		JSONObject all = new JSONObject();
		// 目前SN序列
		JSONArray r_list = new JSONArray();
		sn_list.stream().forEach(i -> {
			// 單個
			JSONObject one = new JSONObject();
			one.put("sn_group", i.getSn_group());
			one.put("sn_group_name", i.getSn_group_name());
			one.put("sn_group_sort", i.getSn_group_sort());
			one.put("sn_id", i.getSn_id());
			one.put("sn_name", i.getSn_name());
			one.put("sn_value", i.getSn_value());
			r_list.put(one);
		});
		all.put("all_list", r_list);
		return all;
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

		templateBean.setWebPageBody("html/body/production_sn_body.html");
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
