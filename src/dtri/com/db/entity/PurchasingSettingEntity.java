package dtri.com.db.entity;

import java.util.Date;

/**
 * 設定
 * 
 **/
public class PurchasingSettingEntity {
	private Date sys_create_date;
	private String sys_create_user;
	private Date sys_modify_date;
	private String sys_modify_user;
	private Integer id;
	private String set_name;
	private String set_value;
	private Integer set_type;
	private String note;
	private Integer useful;
	
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
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSet_name() {
		return set_name;
	}
	public void setSet_name(String set_name) {
		this.set_name = set_name;
	}
	public String getSet_value() {
		return set_value;
	}
	public void setSet_value(String set_value) {
		this.set_value = set_value;
	}
	public Integer getSet_type() {
		return set_type;
	}
	public void setSet_type(Integer set_type) {
		this.set_type = set_type;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Integer getUseful() {
		return useful;
	}
	public void setUseful(Integer useful) {
		this.useful = useful;
	}

}
