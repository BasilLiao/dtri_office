package dtri.com.db.entity;

import java.util.Date;

/**
 * 產品 SN 規則清單
 * 
 **/
public class ProductionSnEntity {
	// 產品 SN 規則清單
	private Date sys_create_date;
	private String sys_create_user;
	private Date sys_modify_date;
	private String sys_modify_user;
	private Integer sn_group;
	private String sn_group_name;
	private Integer sn_group_sort;
	private Integer sn_id;
	private String sn_name;
	private String sn_value;

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

	public Integer getSn_group() {
		return sn_group;
	}

	public void setSn_group(Integer sn_group) {
		this.sn_group = sn_group;
	}

	public String getSn_group_name() {
		return sn_group_name;
	}

	public void setSn_group_name(String sn_group_name) {
		this.sn_group_name = sn_group_name;
	}

	public Integer getSn_group_sort() {
		return sn_group_sort;
	}

	public void setSn_group_sort(Integer sn_group_sort) {
		this.sn_group_sort = sn_group_sort;
	}

	public Integer getSn_id() {
		return sn_id;
	}

	public void setSn_id(Integer sn_id) {
		this.sn_id = sn_id;
	}

	public String getSn_name() {
		return sn_name;
	}

	public void setSn_name(String sn_name) {
		this.sn_name = sn_name;
	}

	public String getSn_value() {
		return sn_value;
	}

	public void setSn_value(String sn_value) {
		this.sn_value = sn_value;
	}


}
