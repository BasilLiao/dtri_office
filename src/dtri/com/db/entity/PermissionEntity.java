package dtri.com.db.entity;

import java.util.Date;

/**
 * 存入單元&權限列表
 * 
 **/
public class PermissionEntity {
	private Date sys_create_date;
	private String sys_create_user;
	private Date sys_modify_date;
	private String sys_modify_user;
	private Integer id;
	private String name;
	private Integer group_id;
	private String group_name;
	private String control;
	private String permission;
	private Integer nbdesc;
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

	public Integer getGroup_id() {
		return group_id;
	}

	public void setGroup_id(Integer group_id) {
		this.group_id = group_id;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public String getControl() {
		return control;
	}

	public void setControl(String control) {
		this.control = control;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
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

	public Integer getNbdesc() {
		return nbdesc;
	}

	public void setNbdesc(Integer nbdesc) {
		this.nbdesc = nbdesc;
	}
}
