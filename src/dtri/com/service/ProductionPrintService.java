package dtri.com.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dtri.com.bean.BomProductGroupBean;
import dtri.com.bean.JsonObjBean;
import dtri.com.bean.JsonTemplateBean;
import dtri.com.db.entity.BomGroupEntity;
import dtri.com.db.entity.BomProductEntity;
import dtri.com.db.entity.BomTypeItemEntity;
import dtri.com.db.entity.ProductionRecordsEntity;
import dtri.com.db.entity.ProductionSnEntity;
import dtri.com.db.entity.SoftwareVersionEntity;
import dtri.com.db.pgsql.dao.BomProductDao;
import dtri.com.db.pgsql.dao.BomTypeItemDao;
import dtri.com.db.pgsql.dao.ProductionRecordsDAO;
import dtri.com.models.Fm_Time_Model;
import dtri.com.models.JsonDataModel;

@Transactional
@Service
public class ProductionPrintService {
	@Autowired
	private BomProductDao productDao;
	@Autowired
	private ProductionRecordsDAO productionDao;
	@Autowired
	private BomTypeItemDao itemDao;

	@Autowired
	private LoginService loginService;

	/**
	 * @param entitys    查詢條件
	 * @param offset     幾頁
	 * @param page_total 數量
	 * @return 查詢後清單
	 * 
	 **/
	public BomProductGroupBean search(List<BomProductEntity> entitys, int offset, int page_total) {
		// 防止輸入為空 或 null
		BomProductGroupBean bpg = new BomProductGroupBean();
		String product_model = "  ";// 產品 型號
		String type_item_group_id = "  ";// 群組ID
		String type_item_id = " ";// 項目ID
		String product_bom_nb = " ";// 對應的BOM
		String all_where_product = " ";// SQL BOM 條件
		String all_where_group = " ";// SQL 群組 條件
		String all_limit = " OFFSET " + offset + " LIMIT " + page_total;
		List<Integer> group_limit_in = new ArrayList<Integer>();

		ArrayList<BomGroupEntity> g_list = new ArrayList<BomGroupEntity>();
		List<BomProductEntity> p_list = new ArrayList<BomProductEntity>();
		List<BomTypeItemEntity> i_list = new ArrayList<BomTypeItemEntity>();
		int select_nb = 0;// 查詢條件
		// ----------項目----------
		if (entitys.size() > 0 && (entitys.get(0).getBom_number() != null || entitys.get(0).getProduct_model() != null
				|| entitys.get(0).getGroupEntity().getType_item_group_id() != null)) {
			for (BomProductEntity entity : entitys) {
				// 組
				if (entity.getGroupEntity().getType_item_group_id() != null
						&& entity.getGroupEntity().getType_item_group_id() != 0) {
					select_nb++;
					all_where_group += "(";
					type_item_group_id = " type_item_group_id = " + entity.getGroupEntity().getType_item_group_id();
					all_where_group += type_item_group_id + " AND ";
				} else {
					continue;
				}
				// 子
				if (entity.getGroupEntity().getType_item_id() != null) {
					type_item_id = " type_item_id = " + entity.getGroupEntity().getType_item_id();
					all_where_group += type_item_id + ") OR ";
				} else {
					all_where_group += "type_item_id !=0 ) OR ";
				}
			}
			// ----------產品----------
			for (BomProductEntity entity : entitys) {
				// 查詢名稱 條件不能與 項目衝突
				if (entity.getProduct_model() != null && !entity.getProduct_model().equals("")) {
					product_model = " product_model LIKE '%" + entity.getProduct_model() + "%' ";
					all_where_product += "(" + product_model + ") AND ";
				}
				if (entity.getBom_number() != null && !entity.getBom_number().equals("")) {
					product_bom_nb = " bom_number LIKE '%" + entity.getBom_number() + "%' ";
					all_where_product += "(" + product_bom_nb + ") AND ";
				}

			}
		
			// 如果(項目有值) / 如果只有(產品有值)
			if (!all_where_group.equals(" ")) {
				// 除對多餘的 OR
				all_where_group = all_where_group.substring(0, all_where_group.length() - 3);
				// 取得傭有條件的 群組 項目
				g_list = productDao.queryGroup(all_where_group, null);
				// 過濾 重複 產品清單
				Map<Integer, Integer> g_m_list = new HashMap<Integer, Integer>();
				if (entitys.size() >= 0) {
					for (BomGroupEntity bomG : g_list) {
						if (g_m_list.containsKey(bomG.getProduct_id())) {
							g_m_list.put(bomG.getProduct_id(), g_m_list.get(bomG.getProduct_id()) + 1);
						} else {
							g_m_list.put(bomG.getProduct_id(), 1);
						}
					}
				}
				// 取得產品條件後 再次取得資料 項目-群組
				if (entitys.size() > 0) {
					all_where_group = " product_id in(";
					for (Entry<Integer, Integer> item : g_m_list.entrySet()) {
						if (select_nb <= item.getValue()) {
							all_where_group += item.getKey() + ",";
						}
					}
					all_where_group += "0)";
					g_list = productDao.queryGroup(all_where_group, null);
				}

				// ID 條件
				all_where_product += "product_model !=''";
				all_where_product += " AND id in(";
				for (Entry<Integer, Integer> item : g_m_list.entrySet()) {
					if (select_nb <= item.getValue()) {
						all_where_product += item.getKey() + ",";
					}
				}
				all_where_product += "0)";
				// 產品-清單
				p_list = productDao.queryProduct(all_where_product, all_limit);
			} else {
				all_where_group += "type_item_id !=0";
				for (BomProductEntity one : p_list) {
					group_limit_in.add(one.getId());
				}
				// 取得傭有條件的 群組 項目+產品
				// ID 條件
				all_where_product += "product_model !=''";
				// 產品-清單
				p_list = productDao.queryProduct(all_where_product, all_limit);
				g_list = productDao.queryGroup(all_where_group, group_limit_in);
			}
		} else {
			// 無條件
			// 產品-清單
			p_list = productDao.queryProduct("product_model !='' ", all_limit);
			// 取得傭有條件的 群組 項目 (限制)
			for (BomProductEntity one : p_list) {
				group_limit_in.add(one.getId());
			}
			g_list = productDao.queryGroup("type_item_id !=0", group_limit_in);
		}

		// 項目-清單
		i_list = itemDao.queryAll("!= ' '");
		bpg.setBomGroupEntities(g_list);
		bpg.setBomProductEntities(p_list);
		bpg.setBomTypeItemEntities(i_list);
		return bpg;
	}

	public List<ProductionRecordsEntity> searchProduction(ProductionRecordsEntity entity, int offset, int page_total) {
		String all_limit = " OFFSET " + offset + " LIMIT " + page_total;
		List<ProductionRecordsEntity> entitys = productionDao.queryProductionRecords(entity, all_limit);
		return entitys;
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

	/** JSON to 清單 **/
	public List<BomProductEntity> jsonToEntities(JSONObject content) {
		List<BomProductEntity> entitys = new ArrayList<BomProductEntity>();

		// 查詢?(BOM 表單)
		if (!content.isNull("search_list") && !content.get("search_list").equals("")) {
			JSONArray search = content.getJSONArray("search_list");
			for (Object object : search) {
				BomProductEntity p_entity = new BomProductEntity();
				BomGroupEntity g_entity = new BomGroupEntity();
				g_entity.setSys_create_date(new Date());
				g_entity.setSys_create_user(loginService.getSessionUserBean().getAccount());
				g_entity.setSys_modify_date(new Date());
				g_entity.setSys_modify_user(loginService.getSessionUserBean().getAccount());
				JSONObject one = new JSONObject(object.toString());
				// 產品?
				if (!one.isNull("p_model") && !one.get("p_model").equals(""))
					p_entity.setProduct_model(one.getString("p_model"));
				// 項目組?
				if (!one.isNull("g_item") && !one.get("g_item").equals(""))
					g_entity.setType_item_group_id(one.getInt("g_item"));
				// 項目?->複數 站存在note內
				if (!one.isNull("i_item") && !one.get("i_item").equals(""))
					g_entity.setType_item_id(one.getInt("i_item"));
				// 項目?->BOM料號
				if (!one.isNull("p_bom_number") && !one.get("p_bom_number").equals(""))
					p_entity.setBom_number(one.getString("p_bom_number"));

				p_entity.setGroupEntity(g_entity);
				entitys.add(p_entity);
			}
		}
		return entitys;
	}

	/** JSON to 清單(解析 製令單) **/
	public ProductionRecordsEntity jsonToEntities2(JSONObject content) {
		ProductionRecordsEntity entity = new ProductionRecordsEntity();

		// 查詢?(BOM 表單)
		// 產品型號?
		if (!content.isNull("product_model") && !content.get("product_model").equals(""))
			entity.setProduct_model(content.getString("product_model"));
		// 總數?
		if (!content.isNull("production_quantity") && !content.get("production_quantity").equals(""))
			entity.setProduction_quantity(content.getInt("production_quantity"));
		// 工單號?
		if (!content.isNull("production_id") && !content.get("production_id").equals(""))
			entity.setId(content.getString("production_id"));
		// 產品料號
		if (!content.isNull("bom_product_id") && !content.get("bom_product_id").equals(""))
			entity.setBom_product_id(content.getString("bom_product_id"));
		// 進度? 狀態類型 完成ERP工單(準備物料)=1/完成注意事項(預約生產)=2/完成->流程卡(準備生產)=3/=4/ =5
		if (!content.isNull("product_progress") && content.getInt("product_progress") >= 0)
			entity.setProduct_progress(content.getInt("product_progress"));
		// 類型? 單據類型
		if (!content.isNull("product_status") && content.getInt("product_status") >= 0) {
			entity.setProduct_status(content.getInt("product_status"));
		} else {
			entity.setProduct_status(1);
		}

		return entity;

	}

	/** JSON to 清單(解析 SN 序號版本) **/
	public ArrayList<ProductionSnEntity> jsonToEntities3(JSONObject content) {
		ArrayList<ProductionSnEntity> p_s_e = new ArrayList<ProductionSnEntity>();
		JSONArray sn_j_a = content.getJSONArray("sn");

		for (int i = 0; i < sn_j_a.length(); i++) {
			ProductionSnEntity p_s_n = new ProductionSnEntity();
			p_s_n.setSn_group(sn_j_a.getJSONObject(i).getInt("sn_group"));
			p_s_n.setSn_name(sn_j_a.getJSONObject(i).getString("sn_name"));
			p_s_n.setSn_value(sn_j_a.getJSONObject(i).getString("sn_value"));
			p_s_n.setSn_id(sn_j_a.getJSONObject(i).getInt("sn_id"));
			p_s_e.add(p_s_n);
		}

		return p_s_e;
	}

	/** 清單 to JSON **/
	public JSONObject entitiesToJson(BomProductGroupBean bpg, List<ProductionRecordsEntity> bpg2,
			List<SoftwareVersionEntity> bpg3, ArrayList<ProductionSnEntity> bpg4, JSONObject sn_obj) {
		JSONObject list = new JSONObject();
		JSONObject item_All = new JSONObject();
		JSONArray jsonAll = new JSONArray();
		JSONArray item_listAll = new JSONArray();
		JSONArray groupList = new JSONArray();
		JSONArray groupOne = new JSONArray();
		JSONArray jsonArray = new JSONArray();
		if (bpg != null) {
			// 標題
			jsonArray.put("建立時間");
			jsonArray.put("建立者");
			jsonArray.put("修改時間");
			jsonArray.put("修改者");

			jsonArray.put("產品ID");
			jsonArray.put("產品型號");
			jsonArray.put("主機板(硬體)-版本");

			jsonArray.put("對應BOM表 單號");
			jsonArray.put("使用狀態");
			jsonArray.put("備註");
			jsonArray.put("上次建單人");
			jsonArray.put("類型");
			jsonAll.put(jsonArray);
			// 內容 產品清單
			for (BomProductEntity entity : bpg.getBomProductEntities()) {
				jsonArray = new JSONArray();
				jsonArray.put(Fm_Time_Model.to_yMd_Hms(entity.getSys_create_date()));
				jsonArray.put(entity.getSys_create_user());
				jsonArray.put(Fm_Time_Model.to_yMd_Hms(entity.getSys_modify_date()));
				jsonArray.put(entity.getSys_modify_user());

				jsonArray.put(entity.getId());
				jsonArray.put(entity.getProduct_model());
				jsonArray.put(entity.getVersion_motherboard());

				jsonArray.put(entity.getBom_number());
				jsonArray.put(entity.getUseful());
				jsonArray.put(entity.getNote());
				jsonArray.put(entity.getChecked());
				jsonArray.put(entity.getKind());
				jsonAll.put(jsonArray);
			}
			list.put("list", jsonAll);
			// 細項 群組清單
			boolean checkf = false;
			for (BomGroupEntity entity : bpg.getBomGroupEntities()) {

				// 第一次不算
				if (entity.getType_item_group_id() == 1 && checkf) {
					groupList.put(groupOne);
				}
				jsonArray = new JSONArray();

				jsonArray.put(Fm_Time_Model.to_yMd_Hms(entity.getSys_create_date()));
				jsonArray.put(entity.getSys_create_user());
				jsonArray.put(Fm_Time_Model.to_yMd_Hms(entity.getSys_modify_date()));
				jsonArray.put(entity.getSys_modify_user());

				jsonArray.put(entity.getId());
				jsonArray.put(entity.getProduct_id());
				jsonArray.put(entity.getType_item_id());
				jsonArray.put(entity.getType_item_group_id());
				jsonArray.put(entity.getNumber());
				jsonArray.put(entity.getType_order());
				jsonArray.put(entity.getUseful());

				jsonArray.put(entity.getGroup_name());
				for (int i = 1; i <= 25; i++) {
					Method method;
					try {
						method = entity.getClass().getMethod("getI" + String.format("%02d", i));
						String value = (String) method.invoke(entity);
						if (value == null || value.equals("")) {
							continue;
						}
						jsonArray.put(value);
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}

				groupOne.put(jsonArray);
				checkf = true;
			}
			// groupList.put(groupOne);
			list.put("group_list", groupOne);
			// 項目清單
			// 項目資料+標題
			for (BomTypeItemEntity entity : bpg.getBomTypeItemEntities()) {
				jsonArray = new JSONArray();
				jsonArray.put(entity.getGroup_id());
				jsonArray.put(entity.getGroup_name());
				jsonArray.put(entity.getId());
				jsonArray.put(entity.getType_order());

				for (int i = 1; i <= 25; i++) {
					Method method;
					try {
						method = entity.getClass().getMethod("getI" + String.format("%02d", i));
						String value = (String) method.invoke(entity);
						if (value == null || value.equals("")) {
							continue;
						}
						jsonArray.put(value);
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
				jsonArray.put(entity.getNote());
				item_listAll.put(jsonArray);
			}
			list.put("item_list", item_listAll);
		}
		// 需生產製令單
		// 標題
		jsonArray = new JSONArray();
		jsonAll = new JSONArray();

		jsonArray.put("建立時間");
		jsonArray.put("建立者");
		jsonArray.put("修改時間");
		jsonArray.put("修改者");

		jsonArray.put("工單號碼");
		jsonArray.put("BOM ID");
		jsonArray.put("BOM料號");
		jsonArray.put("生產數量");
		jsonArray.put("訂單編號");
		jsonArray.put("客戶名稱");
		jsonArray.put("產品型號");

		jsonArray.put("主機板(硬體)-版本");
		jsonArray.put("單據來源");

		jsonAll.put(jsonArray);
		// 內容 產品清單
		for (ProductionRecordsEntity entity : bpg2) {
			jsonArray = new JSONArray();
			jsonArray.put(Fm_Time_Model.to_yMd_Hms(entity.getSys_create_date()));
			jsonArray.put(entity.getSys_create_user());
			jsonArray.put(Fm_Time_Model.to_yMd_Hms(entity.getSys_modify_date()));
			jsonArray.put(entity.getSys_modify_user());

			jsonArray.put(entity.getId());
			jsonArray.put(entity.getBom_id());
			jsonArray.put(entity.getBom_product_id());
			jsonArray.put(entity.getProduction_quantity());
			jsonArray.put(entity.getOrder_id());
			jsonArray.put(entity.getClient_name());
			jsonArray.put(entity.getProduct_model());

			jsonArray.put(entity.getVersion_motherboard());
			jsonArray.put(entity.getCome_from());

			jsonAll.put(jsonArray);
		}

		list.put("production_list", jsonAll);
		// 軟體定義
		if (bpg3 != null) {
			jsonArray = new JSONArray();
			// 內容 軟體清單
			for (SoftwareVersionEntity entity : bpg3) {
				jsonArray = new JSONArray();
				jsonArray.put(entity.getId());
				jsonArray.put(entity.getBom_id());

				jsonArray.put(entity.getBom_product_id());
				jsonArray.put(entity.getClient_name());

				jsonArray.put(entity.getMb_ver());
				jsonArray.put(entity.getMb_ver_ecn());
				jsonArray.put(entity.getNvram());

				jsonArray.put(entity.getBios());
				jsonArray.put(entity.getProduct_model_in());
				jsonArray.put(entity.getEc());
				jsonArray.put(entity.getOs());

				jsonArray.put(entity.getNote());
				jsonArray.put(entity.getNote1());
				jsonArray.put(entity.getNote2());
				item_All.put(entity.getClient_name() + '-' + entity.getBom_product_id(), jsonArray);
			}
			list.put("softwareVer_list", item_All);

		}
		// 產品規則SN條碼-標題 目前SN序列
		JSONArray r_list = new JSONArray();

		// 內容 產品清單
		bpg4.stream().forEach(i -> {
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
		list.put("sn_all_list", r_list);
		list.put("sn_all_nb", sn_obj);

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

		templateBean.setWebPageBody("html/body/production_print_body.html");
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
