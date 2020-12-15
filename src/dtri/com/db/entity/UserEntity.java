package dtri.com.db.entity;

import java.util.Date;
/**
 * 帳號使用者資料
 * 
 * **/
public class UserEntity {
	private Date sys_create_date;
	private String sys_create_user;
	private Date sys_modify_date;
	private String sys_modify_user;
	private Integer id;
	private String name;
	private String e_name;
	private String position;
	private String account;
	private String password;
	private String email;
	private Integer group_id;
	private String group_name;
	private Integer permission_granted;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public Integer getPermission_granted() {
		return permission_granted;
	}
	public void setPermission_granted(Integer permission_granted) {
		this.permission_granted = permission_granted;
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
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
	public Integer getGroup_id() {
		return group_id;
	}
	public void setGroup_id(Integer group_id) {
		this.group_id = group_id;
	}
	public String getE_name() {
		return e_name;
	}
	public void setE_name(String e_name) {
		this.e_name = e_name;
	}
	
}
