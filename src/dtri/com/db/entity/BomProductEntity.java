package dtri.com.db.entity;

import java.util.Date;
import java.util.List;

/**
 * BOM 清單
 * 
 **/
public class BomProductEntity {
	private Date sys_create_date;
	private String sys_create_user;
	private Date sys_modify_date;
	private String sys_modify_user;
	private Integer id;
	private String product_model;
	private String version_motherboard;
	private String bom_number;
	private String note;
	private Integer useful;
	private String checked;
	private Integer kind;

	// 例外處理 非SQL 物件(查詢條件)
	private BomGroupEntity groupEntity;
	// SQL 回傳條件
	private List<BomGroupEntity> groupEntitis;

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

	public String getProduct_model() {
		return product_model;
	}

	public void setProduct_model(String product_model) {
		this.product_model = product_model;
	}

	public String getVersion_motherboard() {
		return version_motherboard;
	}

	public void setVersion_motherboard(String version_motherboard) {
		this.version_motherboard = version_motherboard;
	}

	public String getBom_number() {
		return bom_number;
	}

	public void setBom_number(String bom_number) {
		this.bom_number = bom_number;
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

	public BomGroupEntity getGroupEntity() {
		return groupEntity;
	}

	public void setGroupEntity(BomGroupEntity groupEntity) {
		this.groupEntity = groupEntity;
	}

	public List<BomGroupEntity> getGroupEntitis() {
		return groupEntitis;
	}

	public void setGroupEntitis(List<BomGroupEntity> groupEntitis) {
		this.groupEntitis = groupEntitis;
	}

	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public Integer getKind() {
		return kind;
	}

	public void setKind(Integer kind) {
		this.kind = kind;
	}
}
