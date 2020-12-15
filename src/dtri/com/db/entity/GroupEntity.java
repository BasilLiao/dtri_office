package dtri.com.db.entity;

import java.util.Date;

/**
 * 使用者群組
 * 
 **/
public class GroupEntity {
	private Date sys_create_date;
	private String sys_create_user;
	private Date sys_modify_date;
	private String sys_modify_user;
	private Integer id;
	private String name;
	private String permission_type;
	private Integer permission_id;
	private String permission_group;
	private String permission_name;
	private String permission_control;
	private String permission;
	private String note;
	private Integer useful;
	private Integer nbdesc;

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

	public String getPermission_type() {
		return permission_type;
	}

	public void setPermission_type(String permission_type) {
		this.permission_type = permission_type;
	}

	public Integer getPermission_id() {
		return permission_id;
	}

	public void setPermission_id(Integer permission_id) {
		this.permission_id = permission_id;
	}

	public String getPermission_control() {
		return permission_control;
	}

	public void setPermission_control(String permission_control) {
		this.permission_control = permission_control;
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

	public String getPermission_group() {
		return permission_group;
	}

	public void setPermission_group(String permission_group) {
		this.permission_group = permission_group;
	}

	public String getPermission_name() {
		return permission_name;
	}

	public void setPermission_name(String permission_name) {
		this.permission_name = permission_name;
	}

	public Integer getNbdesc() {
		return nbdesc;
	}

	public void setNbdesc(Integer nbdesc) {
		this.nbdesc = nbdesc;
	}

}
