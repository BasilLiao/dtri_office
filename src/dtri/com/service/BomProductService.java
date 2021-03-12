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
import dtri.com.db.pgsql.dao.BomProductDao;
import dtri.com.db.pgsql.dao.BomTypeItemDao;
import dtri.com.models.Fm_Time_Model;
import dtri.com.models.JsonDataModel;

@Transactional
@Service
public class BomProductService {
	@Autowired
	private BomProductDao productDao;
	@Autowired
	private BomTypeItemDao itemDao;
	@Autowired
	private LoginService loginService;

	/** 查詢唯一值 **/
	public BomProductEntity searchById(Integer id) {
		BomProductEntity re = new BomProductEntity();
		re = productDao.queryProductbyId(id);
		return re;
	}

	/**
	 * @param entitys 查詢物件
	 * @param offset  分頁筆數(100筆為單位)
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
				// ID 條件
				all_where_product += "product_model !=''";
				// 產品-清單
				p_list = productDao.queryProduct(all_where_product, all_limit);
				for (BomProductEntity one : p_list) {
					group_limit_in.add(one.getId());
				}
				// 取得傭有條件的 群組 項目+產品
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

	/** 新增 **/
	public Boolean added(BomProductEntity entity) {
		Boolean check = false;
		if (productDao.queryProduct_bom_number(entity) != null) {
			entity.setBom_number(entity.getBom_number() + "_重複");
		}
		productDao.addedOne(entity);
		for (BomGroupEntity one : entity.getGroupEntitis()) {
			productDao.addedOneGroup(one);
		}

		return check;
	}

	/** 更新 **/
	public Boolean update(BomProductEntity entity) {
		Boolean check = false;
		productDao.updateProduct(entity);
		BomGroupEntity re_other = new BomGroupEntity();
		re_other.setNote("");
		for (BomGroupEntity one : entity.getGroupEntitis()) {
			// 沒這項目->新增
			if (productDao.updateGroup(one) != 1) {
				productDao.addedOneGroup(one);
			}
			re_other.setProduct_id(one.getProduct_id());
			re_other.setNote(re_other.getNote() + one.getId() + ",");
		}
		// 移除多餘
		re_other.setNote(re_other.getNote() + "0");
		productDao.deleteGroupOther(re_other);
		return check;
	}

	/** 移除 **/
	public Boolean delete(BomProductEntity entity) {
		Boolean check = false;
		productDao.deleteGroupByProduct_id(entity.getId());
		productDao.deleteProduct(entity.getId());
		return check;

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
	public List<BomProductEntity> jsonToEntitiesSearch(JSONObject content, String action) {
		List<BomProductEntity> entitys = new ArrayList<BomProductEntity>();

		// 查詢?
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

	/** JSON to 清單 **/
	public List<BomProductEntity> jsonToEntities(JSONObject content, String action) {
		List<BomProductEntity> entitys = new ArrayList<BomProductEntity>();
		// 更新 新增 移除用
		if (!content.isNull("product") && !content.get("product").equals("") && !content.isNull("group_item")
				&& !content.get("group_item").equals("")) {
			BomProductEntity p_entity = new BomProductEntity();
			BomGroupEntity g_entity = new BomGroupEntity();
			List<BomGroupEntity> groupEntitis = new ArrayList<BomGroupEntity>();
			JSONObject product = (JSONObject) content.get("product");
			JSONArray group_list = (JSONArray) content.get("group_item");
			JSONArray group_one = new JSONArray();

			p_entity.setSys_create_date(new Date());
			p_entity.setSys_create_user(loginService.getSessionUserBean().getAccount());
			p_entity.setSys_modify_date(new Date());
			p_entity.setSys_modify_user(loginService.getSessionUserBean().getAccount());
			p_entity.setBom_number(product.getString("bom_number").trim());
			p_entity.setProduct_model(product.getString("product_model"));
			p_entity.setVersion_motherboard(product.getString("version_motherboard"));
			p_entity.setUseful(product.getInt("useful"));
			p_entity.setKind(product.getInt("kind"));
			p_entity.setNote(product.getString("note"));

			// 取得ID
			p_entity.setId(product.getInt("id"));
			// 如果是新增或另存 (取下個ID)
			if (action.equals("C") || p_entity.getId() == 0) {
				int id = productDao.nextvalGroup();
				p_entity.setId(id);
			}
			for (Object object : group_list) {
				g_entity = new BomGroupEntity();
				group_one = (JSONArray) object;
				g_entity.setSys_create_date(p_entity.getSys_create_date());
				g_entity.setSys_create_user(p_entity.getSys_create_user());
				g_entity.setSys_modify_date(p_entity.getSys_modify_date());
				g_entity.setSys_modify_user(p_entity.getSys_modify_user());
				if (!action.equals("C")) {// 新增不需要ID
					g_entity.setId(group_one.getInt(0));
				}
				g_entity.setProduct_id(p_entity.getId());
				g_entity.setType_item_id(group_one.getInt(2));
				g_entity.setType_item_group_id(group_one.getInt(3));
				g_entity.setNumber(group_one.getInt(4));
				g_entity.setUseful(product.getInt("useful"));
				groupEntitis.add(g_entity);
			}
			p_entity.setGroupEntitis(groupEntitis);
			entitys.add(p_entity);
		}

		return entitys;
	}

	/** 清單 to JSON **/
	public JSONObject entitiesToJson(BomProductGroupBean bpg) {
		JSONObject list = new JSONObject();
		JSONArray jsonAll = new JSONArray();
		JSONArray item_listAll = new JSONArray();
		JSONArray groupList = new JSONArray();
		JSONArray groupOne = new JSONArray();
		// 標題
		JSONArray jsonArray = new JSONArray();
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
			jsonArray.put(entity.getCheckdef());
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

		templateBean.setWebPageBody("html/body/bom_product_body.html");
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
