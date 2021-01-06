package dtri.com.db.entity;

import java.util.Date;

/**
 * @author Basil
 * 
 *         生產紀錄
 **/
public class ProductionRecordsEntity {

	private Date sys_create_date;
	private String sys_create_user;
	private Date sys_modify_date;
	private String sys_modify_user;
	private String new_id;// 廢止 生產紀錄會用到
	private String id;
	private String order_id;
	private String client_name;
	private Integer production_quantity;
	private String product_model;
	private Integer bom_id;
	private String bom_product_id;
	private String version_motherboard;
	private String note;
	private String come_from;
	private Integer product_progress;
	private Integer product_status;
	private String product_start_sn;
	private String product_end_sn;

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getClient_name() {
		return client_name;
	}

	public void setClient_name(String client_name) {
		this.client_name = client_name;
	}

	public Integer getProduction_quantity() {
		return production_quantity;
	}

	public void setProduction_quantity(Integer production_quantity) {
		this.production_quantity = production_quantity;
	}

	public String getProduct_model() {
		return product_model;
	}

	public void setProduct_model(String product_model) {
		this.product_model = product_model;
	}

	public String getBom_product_id() {
		return bom_product_id;
	}

	public void setBom_product_id(String bom_product_id) {
		this.bom_product_id = bom_product_id;
	}

	public String getVersion_motherboard() {
		return version_motherboard;
	}

	public void setVersion_motherboard(String version_motherboard) {
		this.version_motherboard = version_motherboard;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getCome_from() {
		return come_from;
	}

	public void setCome_from(String come_from) {
		this.come_from = come_from;
	}

	public Integer getProduct_status() {
		return product_status;
	}

	public void setProduct_status(Integer product_status) {
		this.product_status = product_status;
	}

	public Integer getProduct_progress() {
		return product_progress;
	}

	public void setProduct_progress(Integer product_progress) {
		this.product_progress = product_progress;
	}

	public Integer getBom_id() {
		return bom_id;
	}

	public void setBom_id(Integer bom_id) {
		this.bom_id = bom_id;
	}

	public String getNew_id() {
		return new_id;
	}

	public void setNew_id(String new_id) {
		this.new_id = new_id;
	}

	public String getProduct_start_sn() {
		return product_start_sn;
	}

	public void setProduct_start_sn(String product_start_sn) {
		this.product_start_sn = product_start_sn;
	}

	public String getProduct_end_sn() {
		return product_end_sn;
	}

	public void setProduct_end_sn(String product_end_sn) {
		this.product_end_sn = product_end_sn;
	}

}
