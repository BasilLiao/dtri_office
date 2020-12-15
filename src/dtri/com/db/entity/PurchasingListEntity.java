package dtri.com.db.entity;

import java.util.Date;

/**
 * 採購寄件紀錄
 * 
 **/
public class PurchasingListEntity {
	// 採購寄件紀錄
	private Date sys_create_date;
	private String sys_create_user;
	private Date sys_modify_date;
	private String sys_modify_user;
	private Integer id;
	private String erp_order_id;
	private String erp_item_ns;
	private String erp_item_no;
	private String erp_item_name;
	private Integer erp_item_nb;
	private String erp_in_date;
	private String erp_store_name;
	private String erp_store_email;
	private String user_name;
	private String user_mail;
	private boolean sys_type;
	private Integer content_id;
	private Date sys_send_time;
	// 時間範圍
	private String erp_s_date;
	private String erp_e_date;
	private String purchasing_limit;

	public Date getSys_create_date() {
		return sys_create_date;
	}

	public void setSys_create_date(Date sys_create_date) {
		this.sys_create_date = sys_create_date;
	}

	public String getSys_create_user() {
		return sys_create_user;
	}

	public void setSys_create_user(String sys_create_user) {
		this.sys_create_user = sys_create_user;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getErp_order_id() {
		return erp_order_id;
	}

	public void setErp_order_id(String erp_order_id) {
		this.erp_order_id = erp_order_id;
	}

	public String getErp_item_no() {
		return erp_item_no;
	}

	public void setErp_item_no(String erp_item_no) {
		this.erp_item_no = erp_item_no;
	}

	public String getErp_item_name() {
		return erp_item_name;
	}

	public void setErp_item_name(String erp_item_name) {
		this.erp_item_name = erp_item_name;
	}

	public Integer getErp_item_nb() {
		return erp_item_nb;
	}

	public void setErp_item_nb(Integer erp_item_nb) {
		this.erp_item_nb = erp_item_nb;
	}

	public String getErp_in_date() {
		return erp_in_date;
	}

	public void setErp_in_date(String erp_in_date) {
		this.erp_in_date = erp_in_date;
	}

	public String getErp_store_name() {
		return erp_store_name;
	}

	public void setErp_store_name(String erp_store_name) {
		this.erp_store_name = erp_store_name;
	}

	public String getErp_store_email() {
		return erp_store_email;
	}

	public void setErp_store_email(String erp_store_email) {
		this.erp_store_email = erp_store_email;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_mail() {
		return user_mail;
	}

	public void setUser_mail(String user_mail) {
		this.user_mail = user_mail;
	}

	public boolean getSys_type() {
		return sys_type;
	}

	public void setSys_type(boolean sys_type) {
		this.sys_type = sys_type;
	}

	public Integer getContent_id() {
		return content_id;
	}

	public void setContent_id(Integer content_id) {
		this.content_id = content_id;
	}

	public Date getSys_send_time() {
		return sys_send_time;
	}

	public void setSys_send_time(Date sys_send_time) {
		this.sys_send_time = sys_send_time;
	}

	public Date getSys_modify_date() {
		return sys_modify_date;
	}

	public void setSys_modify_date(Date sys_modify_date) {
		this.sys_modify_date = sys_modify_date;
	}

	public String getSys_modify_user() {
		return sys_modify_user;
	}

	public void setSys_modify_user(String sys_modify_user) {
		this.sys_modify_user = sys_modify_user;
	}

	public String getErp_s_date() {
		return erp_s_date;
	}

	public void setErp_s_date(String erp_s_date) {
		this.erp_s_date = erp_s_date;
	}

	public String getErp_e_date() {
		return erp_e_date;
	}

	public void setErp_e_date(String erp_e_date) {
		this.erp_e_date = erp_e_date;
	}

	public String getErp_item_ns() {
		return erp_item_ns;
	}

	public void setErp_item_ns(String erp_item_ns) {
		this.erp_item_ns = erp_item_ns;
	}

	public String getPurchasing_limit() {
		return purchasing_limit;
	}

	public void setPurchasing_limit(String purchasing_limit) {
		this.purchasing_limit = purchasing_limit;
	}
}
