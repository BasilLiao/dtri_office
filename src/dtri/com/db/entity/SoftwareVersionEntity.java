package dtri.com.db.entity;

import java.util.Date;

/**
 * @author Basil
 * 
 *         產品之軟體版本對照表
 **/
public class SoftwareVersionEntity {
	private Date sys_create_date;
	private String sys_create_user;
	private Date sys_modify_date;
	private String sys_modify_user;
	private Integer id;
	private String client_name;
	private String product_model_in;
	private Integer bom_id;
	private String bom_product_id;
	private String mb_ver;
	private String mb_ver_ecn;
	private String bios;
	private String ec;
	private String nvram;
	private String os;
	private String note;
	private String note1;
	private String note2;

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

	public String getClient_name() {
		return client_name;
	}

	public void setClient_name(String client_name) {
		this.client_name = client_name;
	}



	public Integer getBom_id() {
		return bom_id;
	}

	public void setBom_id(Integer bom_id) {
		this.bom_id = bom_id;
	}

	public String getBom_product_id() {
		return bom_product_id;
	}

	public void setBom_product_id(String bom_product_id) {
		this.bom_product_id = bom_product_id;
	}

	public String getMb_ver() {
		return mb_ver;
	}

	public void setMb_ver(String mb_ver) {
		this.mb_ver = mb_ver;
	}

	public String getMb_ver_ecn() {
		return mb_ver_ecn;
	}

	public void setMb_ver_ecn(String mb_ver_ecn) {
		this.mb_ver_ecn = mb_ver_ecn;
	}

	public String getBios() {
		return bios;
	}

	public void setBios(String bios) {
		this.bios = bios;
	}

	public String getEc() {
		return ec;
	}

	public void setEc(String ec) {
		this.ec = ec;
	}

	public String getNvram() {
		return nvram;
	}

	public void setNvram(String nvram) {
		this.nvram = nvram;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getNote1() {
		return note1;
	}

	public void setNote1(String note1) {
		this.note1 = note1;
	}

	public String getNote2() {
		return note2;
	}

	public void setNote2(String note2) {
		this.note2 = note2;
	}

	public String getProduct_model_in() {
		return product_model_in;
	}

	public void setProduct_model_in(String product_model_in) {
		this.product_model_in = product_model_in;
	}

}
