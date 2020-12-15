package dtri.com.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
import dtri.com.db.entity.BomTypeItemEntity;
import dtri.com.db.pgsql.dao.BomTypeItemDao;
import dtri.com.models.Fm_Time_Model;
import dtri.com.models.JsonDataModel;

@Transactional
@Service
public class BomTypeItemService {
	@Autowired
	private BomTypeItemDao itemDao;
	@Autowired
	private LoginService loginService;

	/**
	 * @param entity 可能多個查詢條件
	 * 
	 * @return 查詢後清單
	 * 
	 **/
	public List<BomTypeItemEntity> search(BomTypeItemEntity entity) {

		// 必須要選擇群組條件(群組不可空直)
		String checkName = " = '";
		if (entity.getGroup_name() == null || entity.getGroup_name().equals("")) {
			checkName = " != '";
			entity.setGroup_name(" ");
		}
		List<BomTypeItemEntity> list = itemDao.queryAll(checkName + entity.getGroup_name() + "'");
		return list;
	}

	/** 新增 **/
	public Boolean addedTypeItem(BomTypeItemEntity entity, Boolean is_new) {
		if (is_new) {
			int group_id = itemDao.nextvalItemGroup();
			entity.setGroup_id(group_id);
		}
		Boolean check = false;
		itemDao.addedOneGroup(entity);
		return check;
	}

	/** 更新 **/
	public Boolean update(List<BomTypeItemEntity> entitys) {
		Boolean check = false;
		for (BomTypeItemEntity entity : entitys) {
			// 修改
			int update = itemDao.updateOne(entity);
			// 新增
			if (update == 0) {
				addedTypeItem(entity, false);
			}
		}
		// 移除 多餘的要移除
		deleteOther(entitys);
		return check;
	}

	/** 移除 **/
	public Boolean delete(BomTypeItemEntity entity) {
		Boolean check = false;
		itemDao.deleteOne(entity);
		return check;
	}

	/** 排除-不存在項目移除 **/
	public Boolean deleteOther(List<BomTypeItemEntity> entitys) {
		Boolean check = false;
		String inId = "";
		ArrayList<String> idlist = new ArrayList<String>();
		for (BomTypeItemEntity entity : entitys) {
			idlist.add(entity.getId() + "");
		}
		inId = String.join(",", idlist);
		itemDao.deleteOther(entitys.get(0).getGroup_id(), inId);
		return check;

	}

	/** JSON to 清單(複數) **/
	public List<BomTypeItemEntity> jsonToEntities(JSONObject content) {
		// MD5HashModel md5 = new MD5HashModel();
		BomTypeItemEntity entity = new BomTypeItemEntity();
		BomTypeItemEntity entityItem = new BomTypeItemEntity();
		entity.setSys_create_date(new Date());
		entity.setSys_create_user(loginService.getSessionUserBean().getAccount());
		entity.setSys_modify_date(new Date());
		entity.setSys_modify_user(loginService.getSessionUserBean().getAccount());
		// 一般 訊息
		if (!content.isNull("group_name") && !content.get("group_name").equals(""))
			entity.setGroup_name(content.getString("group_name"));
		if (!content.isNull("group_id") && !content.get("group_id").equals(""))
			entity.setGroup_id(content.getInt("group_id"));
		if (!content.isNull("id") && !content.get("id").equals(""))
			entity.setId(content.getInt("id"));
		if (!content.isNull("type") && !content.get("type").equals(""))
			entity.setType_item(content.getInt("type"));
		if (!content.isNull("note") && !content.get("note").equals(""))
			entity.setNote(content.getString("note"));
		if (!content.isNull("checkdf") && !content.get("checkdf").equals(""))
			entity.setCheckdef(content.getBoolean("checkdf"));
		if (!content.isNull("type_order") && !content.get("type_order").equals(""))
			entity.setType_order(content.getInt("type_order"));
				
		
		// 項目-群組
		if (!content.isNull("group_list") && !content.get("group_list").equals("")) {
			Method method;
			
			entity.setId(content.getJSONArray("group_list").getInt(0));
			entity.setNote((content.getJSONArray("group_list").getString(1)));
			for (int i = 2; i < content.getJSONArray("group_list").length(); i++) {
				String group_list = (String) content.getJSONArray("group_list").get(i);
				try {
					method = entity.getClass().getMethod("setI" + String.format("%02d", i - 1), String.class);
					method.invoke(entity, group_list);
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
		}
		List<BomTypeItemEntity> entitys = new ArrayList<BomTypeItemEntity>();
		entitys.add(entity);
		// 項目內容清單

		if (!content.isNull("item_list") && !content.get("item_list").equals("")) {
			JSONArray item_list = new JSONArray();
			JSONArray item_one = new JSONArray();
			Method method;
			item_list = content.getJSONArray("item_list");
			for (Object object : item_list) {
				item_one = (JSONArray) object;
				entityItem = new BomTypeItemEntity();
				entityItem.setSys_create_date(new Date());
				entityItem.setSys_create_user(loginService.getSessionUserBean().getAccount());
				entityItem.setSys_modify_date(new Date());
				entityItem.setSys_modify_user(loginService.getSessionUserBean().getAccount());

				entityItem.setId(item_one.getInt(0));
				entityItem.setNote(item_one.getString(1));
				entityItem.setGroup_id(entity.getGroup_id());
				entityItem.setGroup_name(entity.getGroup_name());
				entityItem.setType_item(entity.getType_item());
				entityItem.setCheckdef(entity.getCheckdef());
				entityItem.setType_order(entity.getType_order());
				
				for (int i = 2; i < item_one.length(); i++) {
					// 一般 訊息
					String item = (String) item_one.get(i);
					try {
						method = entityItem.getClass().getMethod("setI" + String.format("%02d", i - 1), String.class);
						method.invoke(entityItem, item);
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
				entitys.add(entityItem);
			}
		}
		return entitys;
	}

	/** 清單 to JSON **/
	public JSONObject entitiesToJson(List<BomTypeItemEntity> p_Entities) {
		JSONObject list = new JSONObject();
		JSONArray group_listAll = new JSONArray();
		JSONArray item_listAll = new JSONArray();
		JSONObject group_list = new JSONObject();
		JSONArray jsonArrayG = new JSONArray();
		// 標題 (群組)
		JSONArray jsonArray = new JSONArray();
		jsonArray.put("建立時間");
		jsonArray.put("建立者");
		jsonArray.put("修改時間");
		jsonArray.put("修改者");

		jsonArray.put("群組ID");
		jsonArray.put("群組名稱");
		jsonArray.put("群組類型");
		jsonArray.put("是否必需");
		jsonArray.put("排序");
		jsonArray.put("是否使用");
		group_listAll.put(jsonArray);

		// 內容
		for (BomTypeItemEntity entity : p_Entities) {
			jsonArray = new JSONArray();
			jsonArrayG = new JSONArray();

			// 群組(群組)
			if (entity.getId() == 1) {
				jsonArrayG.put(Fm_Time_Model.to_yMd_Hms(entity.getSys_create_date()));
				jsonArrayG.put(entity.getSys_create_user());
				jsonArrayG.put(Fm_Time_Model.to_yMd_Hms(entity.getSys_modify_date()));
				jsonArrayG.put(entity.getSys_modify_user());

				jsonArrayG.put(entity.getGroup_id());
				jsonArrayG.put(entity.getGroup_name());
				if (entity.getType_item() == 1) {
					jsonArrayG.put("1=checkbox");
				} else {
					jsonArrayG.put("2=select");
				}
				jsonArrayG.put(entity.getCheckdef());
				jsonArrayG.put(entity.getType_order());
				jsonArrayG.put(entity.getUseful());
				group_listAll.put(jsonArrayG);
			}
			// 項目資料+標題
			jsonArray.put(entity.getGroup_id());
			jsonArray.put(entity.getGroup_name());
			jsonArray.put(entity.getId());
			jsonArray.put(entity.getNote());
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
			item_listAll.put(jsonArray);
		}
		group_list.put("group_list", group_listAll);
		group_list.put("item_list", item_listAll);
		list.put("list", group_list);

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

		templateBean.setWebPageBody("html/body/bom_type_item_body.html");
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
