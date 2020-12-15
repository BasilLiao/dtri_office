package dtri.com.bean;

import java.util.Date;

/**
 * 回傳 共用參數Bean物件 (Ajax)
 * 
 **/
public class JsonObjBean {
	private Date r_datetime;
	private String r_action;
	private boolean r_status;
	private String r_message;
	private String r_cellBackName;
	private String r_cellBackOrder;
	private String r_cellBackWebPage;

	public JsonObjBean() {
		this.r_action = "";
		this.r_cellBackName = "";
		this.r_cellBackOrder = "";
		this.r_datetime = new Date();
		this.r_message = "";
		this.r_status = false;

	}

	public Date getR_datetime() {
		return r_datetime;
	}

	public void setR_datetime(Date r_datetime) {
		this.r_datetime = r_datetime;
	}

	public String getR_action() {
		return r_action;
	}

	public void setR_action(String r_action) {
		this.r_action = r_action;
	}

	public boolean getR_status() {
		return r_status;
	}

	public void setR_status(boolean r_status) {
		this.r_status = r_status;
	}

	public String getR_message() {
		return r_message;
	}

	public void setR_message(String r_message) {
		this.r_message = r_message;
	}

	public String getR_cellBackName() {
		return r_cellBackName;
	}

	public void setR_cellBackName(String r_cellBackName) {
		this.r_cellBackName = r_cellBackName;
	}

	public String getR_cellBackOrder() {
		return r_cellBackOrder;
	}

	public void setR_cellBackOrder(String r_cellBackOrder) {
		this.r_cellBackOrder = r_cellBackOrder;
	}

	public String getR_cellBackWebPage() {
		return r_cellBackWebPage;
	}

	public void setR_cellBackWebPage(String r_cellBackWebPage) {
		this.r_cellBackWebPage = r_cellBackWebPage;
	}
}
