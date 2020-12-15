package dtri.com.db.entity;

import java.util.Date;
/**
 *BOM 群組對照清單
 * 
 * **/
public class PurchasingMailEntity {
	//BOM 群組
	private Date sys_modify_date;
	private String sys_modify_user;
	private Integer id;
	private String mail_content;
	private String mail_title;
	
	public Date getSys_modify_date() {
		return sys_modify_date;
	}
	public void setSys_modify_date(Date sys_modify_date) {
		this.sys_modify_date = sys_modify_date;
	}
	public String getSys_modify_user() {
		return sys_modify_user;
	}
	public void setSys_modify_user(String sys_modify_date) {
		this.sys_modify_user = sys_modify_date;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMail_content() {
		return mail_content;
	}
	public void setMail_content(String mail_content) {
		this.mail_content = mail_content;
	}
	public String getMail_title() {
		return mail_title;
	}
	public void setMail_title(String mail_title) {
		this.mail_title = mail_title;
	}
}
