package dtri.com.models;

import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import dtri.com.bean.JsonObjBean;
import dtri.com.bean.JsonTemplateBean;
import dtri.com.db.entity.GroupEntity;
import dtri.com.db.entity.UserEntity;

/**
 * 基本共用+整體 回傳參數 包裝AJAX
 **/
public class JsonDataModel {
	// 基本 共用架構
	private JSONObject r_allJson;
	private JSONObject r_content;

	public JsonDataModel() {
		r_content = new JSONObject();
		r_allJson = new JSONObject();
		r_allJson.put("r_datetime", new Date());
		r_allJson.put("r_action", "");
		r_allJson.put("r_status", "");
		r_allJson.put("r_message", "");
		r_allJson.put("r_cellBackName", "");
		r_allJson.put("r_cellBackOrder", "");
	}

	/**
	 * 共用參數 <br>
	 * 父元素 JsonBean_Obj
	 **/
	public void setR_JsonData(JsonObjBean bean) {
		r_allJson.put("r_datetime", bean.getR_datetime());
		r_allJson.put("r_action", bean.getR_action());
		r_allJson.put("r_status", bean.getR_status());
		r_allJson.put("r_message", bean.getR_message());
		r_allJson.put("r_cellBackName", bean.getR_cellBackName());
		r_allJson.put("r_cellBackOrder", bean.getR_cellBackOrder());
	}

	/**
	 * 回傳 內容所有組合(JSON)
	 **/
	private Boolean setR_content(JSONObject templateInfo, JSONObject template, JSONObject longinInfo) {
		// 換功能單元用 or 回傳查詢資料
		if (templateInfo != null) {
			r_content.put("templateInfo", templateInfo);
		}
		if (template != null) {
			r_content.put("template", template);
		}
		if (longinInfo != null) {
			r_content.put("longinInfo", longinInfo);
		}
		return true;
	}

	// vvv-----------------[收發包裝]-----------------vvv
	/**
	 * JSON接收轉換 JSONObject && 檢查 <br>
	 * 接收JSON 時 共用 檢查內容
	 **/
	public JSONObject frontToBack(String ajaxJSON) {
		JSONObject jsonObject = new JSONObject(ajaxJSON);
		if (jsonObject.get("data") != null) {
			return jsonObject;
		}
		return null;
	}

	/**
	 * 所有資料組合回傳
	 **/
	public JSONObject getR_allData() {
		r_allJson.put("r_content", r_content);
		return r_allJson;
	}

	// ^^^-----------------[收發包裝]-----------------^^^
	// vvv-----------------[功能清單]-----------------vvv
	/**
	 * 第一次讀取時用<br>
	 * nav 導覽 清單建置 login 登入資料建置 換頁功能
	 **/
	public Boolean setR_templateList(List<GroupEntity> permissionEntity, UserEntity userEntity) {
		// 登入者資訊
		JSONObject longinInfo = new JSONObject();
		// 模組
		JSONObject template = new JSONObject();
		if (userEntity != null) {
			longinInfo.put("name", userEntity.getName());
			longinInfo.put("account", userEntity.getAccount());
			longinInfo.put("position", userEntity.getPosition());

			template.put("body", "html/body/index_body.html");
			template.put("footer", "html/footer/index_footer.html");
			template.put("nav", "html/nav/index_nav.html");
			template.put("header", "html/header/index_header.html");
			/*
			 * template.put("body", "index_body"); template.put("footer", "index_footer");
			 * template.put("nav", "index_nav"); template.put("header", "index_header");
			 */
		}

		// 功能群組清單
		JSONObject templateInfo = new JSONObject();
		JSONObject navGroup = new JSONObject();
		JSONArray navItem = new JSONArray();
		JSONArray navs = new JSONArray();
		String group_name = "";
		if (permissionEntity != null) {
			// body
			templateInfo.put("body", "");
			// footer
			templateInfo.put("footer", "");
			// header
			templateInfo.put("header", "");
			// nav
			for (GroupEntity one : permissionEntity) {
				// 建立每個功能群組不同
				if (!group_name.equals(one.getPermission_group())) {
					if (navItem.length() > 0) {
						navGroup.put("group_item", navItem);
						navItem = new JSONArray();
						navs.put(navGroup);
					}
					navGroup = new JSONObject();
					navGroup.put("group_name", one.getPermission_group());
					group_name = one.getPermission_group();
				}
				navItem.put(one.getPermission_control() + " " + one.getPermission_name());
			}
			navGroup.put("group_item", navItem);
			navs.put(navGroup);
			templateInfo.put("nav", navs);
		}
		setR_content(templateInfo, template, longinInfo);

		return true;
	}

	/**
	 * 
	 * 切換 (單元切換)功能<br>
	 * 子元素:JsonBean_Template
	 **/
	public Boolean setR_template(JsonTemplateBean templateBean) {
		// 模組
		JSONObject templateInfo = new JSONObject();
		JSONObject template = new JSONObject();

		templateInfo.put("body", templateBean.getBodyData());
		templateInfo.put("header", templateBean.getHeaderData());
		templateInfo.put("nav", templateBean.getNavData());
		templateInfo.put("footer", templateBean.getFooterData());

		template.put("body", templateBean.getWebPageBody());
		template.put("header", templateBean.getWebPageHeader());
		template.put("nav", templateBean.getWebPageNav());
		template.put("footer", templateBean.getWebPageFooter());

		setR_content(templateInfo, template, null);
		return true;
	}

	// ^^^-----------------[功能清單]-----------------^^^
}
