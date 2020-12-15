package dtri.com.service;

import java.util.List;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dtri.com.bean.JsonObjBean;
import dtri.com.bean.JsonTemplateBean;
import dtri.com.db.entity.GroupEntity;
import dtri.com.db.entity.UserEntity;
import dtri.com.models.JsonDataModel;

@Transactional
@Service
public class IndexService {

	/** 權限回傳包裝 
	 * @param user **/
	public JSONObject entitiesToJson(List<GroupEntity> group, UserEntity user ) {
		JSONObject jsonObject = new JSONObject();
		for (GroupEntity groupEntity : group) {
			jsonObject.put(groupEntity.getPermission_name(), groupEntity.getPermission());
		}
		return jsonObject;
	}

	/**
	 * 統整回傳包裝
	 * 
	 * @param p_Obj     回傳查詢資料
	 * @param frontData 前端傳入的"控制"資訊 回傳 成功
	 **/
	public JSONObject ajaxRspJson(JSONObject p_Obj, JSONObject frontData,List<GroupEntity> group, UserEntity user ) {
		JsonDataModel data = new JsonDataModel();
		JSONObject r_allData = new JSONObject();
		JsonObjBean objBean = new JsonObjBean();
		JsonTemplateBean templateBean = new JsonTemplateBean();

		templateBean.setWebPageBody("html/body/index_body.html");
		templateBean.setWebPageHeader("html/header/index_header.html");
		templateBean.setWebPageFooter("html/footer/index_footer.html");
		templateBean.setWebPageNav("html/nav/index_nav.html");
		if (p_Obj != null) {
			templateBean.setBodyData(p_Obj);
		}
		if (frontData != null) {
			objBean.setR_cellBackName(frontData.getString("cellBackName"));
			objBean.setR_action(frontData.getString("action"));
			objBean.setR_cellBackOrder(frontData.getString("cellBackOrder"));
		}
		objBean.setR_status(true);
		objBean.setR_message("登入成功,歡迎使用 ERP 延展系統");
		data.setR_JsonData(objBean);
		data.setR_template(templateBean);
		if(group!=null && user!=null) {
			data.setR_templateList(group, user);			
		}
		r_allData = data.getR_allData();
		return r_allData;
	}

	public JSONObject ajaxRspJsonLogout(JSONObject p_Obj, JSONObject frontData) {

		JsonDataModel data = new JsonDataModel();
		JSONObject r_allData = new JSONObject();
		JsonObjBean objBean = new JsonObjBean();
		JsonTemplateBean templateBean = new JsonTemplateBean();

		templateBean.setWebPageBody("html/body/logout_body.html");
		templateBean.setWebPageNav("html/nav/logout_nav.html");
		if (p_Obj != null) {
			templateBean.setBodyData(p_Obj);
		}
		if (frontData != null) {
			objBean.setR_cellBackName(frontData.getString("cellBackName"));
			objBean.setR_action(frontData.getString("action"));
			objBean.setR_cellBackOrder(frontData.getString("cellBackOrder"));
		}
		objBean.setR_status(true);
		objBean.setR_message("登出成功 - > 謝謝光臨 歡迎您下次光臨");

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
	public JSONObject fail_ajaxRspJson(JSONObject frontData) {
		JsonDataModel data = new JsonDataModel();
		JSONObject r_allData = new JSONObject();
		JsonObjBean objBean = new JsonObjBean();
		JsonTemplateBean templateBean = new JsonTemplateBean();
		if (frontData != null) {
			objBean.setR_cellBackName(frontData.getString("cellBackName"));
			objBean.setR_action(frontData.getString("action"));
			objBean.setR_cellBackOrder(frontData.getString("cellBackOrder"));

		}

		objBean.setR_status(false);
		objBean.setR_message("你沒有權限!!");
		// Step5.包裝
		data.setR_JsonData(objBean);
		data.setR_template(templateBean);
		r_allData = data.getR_allData();
		return r_allData;
	}
}
