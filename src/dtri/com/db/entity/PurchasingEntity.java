package dtri.com.db.entity;

import java.util.Date;
/**
 *BOM 群組對照清單
 * 
 * **/
public class PurchasingEntity {
	//BOM 群組
	private Date sys_create_date;
	private String sys_create_user;
	private Date sys_modify_date;
	private String sys_modify_user;
	private Integer id;
	private String key_word;
	private String key_item_word;
	private Integer user_id;
	private String user_name;
	private String user_e_name;
	private String user_email;
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
	public String getKey_word() {
		return key_word;
	}
	public void setKey_word(String key_word) {
		this.key_word = key_word;
	}
	public String getKey_item_word() {
		return key_item_word;
	}
	public void setKey_item_word(String key_item_word) {
		this.key_item_word = key_item_word;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_email() {
		return user_email;
	}
	public void setUser_email(String user_email) {
		this.user_email = user_email;
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
	public String getUser_e_name() {
		return user_e_name;
	}
	public void setUser_e_name(String user_e_name) {
		this.user_e_name = user_e_name;
	}

}
