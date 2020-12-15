package dtri.com.bean;

import org.json.JSONObject;

/**
 * 包裝 template / templateInfo 內容 
 * **/
public class JsonTemplateBean {
	private JSONObject bodyData;
	private JSONObject headerData;
	private JSONObject navData;
	private JSONObject footerData;
	private String webPageBody;
	private String webPageHeader;
	private String webPageNav;
	private String webPageFooter;

	public JsonTemplateBean() {
		this.bodyData = new JSONObject();
		this.headerData = new JSONObject();
		this.navData = new JSONObject();
		this.footerData = new JSONObject();
		this.webPageBody = "";
		this.webPageHeader =  "";
		this.webPageNav =  "";
		this.webPageFooter =  "";
	}
	public JSONObject getBodyData() {
		return bodyData;
	}
	public void setBodyData(JSONObject bodyData) {
		this.bodyData = bodyData;
	}
	public JSONObject getHeaderData() {
		return headerData;
	}
	public void setHeaderData(JSONObject headerData) {
		this.headerData = headerData;
	}
	public JSONObject getNavData() {
		return navData;
	}
	public void setNavData(JSONObject navData) {
		this.navData = navData;
	}
	public JSONObject getFooterData() {
		return footerData;
	}
	public void setFooterData(JSONObject footerData) {
		this.footerData = footerData;
	}
	public String getWebPageBody() {
		return webPageBody;
	}
	public void setWebPageBody(String webPageBody) {
		this.webPageBody = webPageBody;
	}
	public String getWebPageHeader() {
		return webPageHeader;
	}
	public void setWebPageHeader(String webPageHeader) {
		this.webPageHeader = webPageHeader;
	}
	public String getWebPageNav() {
		return webPageNav;
	}
	public void setWebPageNav(String webPageNav) {
		this.webPageNav = webPageNav;
	}
	public String getWebPageFooter() {
		return webPageFooter;
	}
	public void setWebPageFooter(String webPageFooter) {
		this.webPageFooter = webPageFooter;
	}
	
}
