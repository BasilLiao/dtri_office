package dtri.com.db.entity;

import java.util.List;

public class ERP_PurchasingEntity {
	private String TD003;// AS '項次',
	private String TD004;// AS '品號',
	private String TD005;// AS '品名',
	private String TD006;// AS '規格',
	private String TD012;// AS '預交日',
	private String TC004;// AS '供應商',
	private String MA002;// AS '廠商名稱',
	private String MA011;// AS '廠商MAIL',
	private String TD007;// AS '交貨庫別',
	private String MC002;// AS '庫別名稱',
	private Integer TD008;// AS '採購數量',
	private Integer TD015;// AS '已交數量',
	private Integer TD008_TD015;// AS'未交數量',
	private String TD016;// AS '結案',--要是 'N' 值
	private String TC001_TC002;// AS '採購單號'
	private String TC011;//採購人
	// 查詢條件用
	private String S_TD012;// AS '區間起始 預交日',
	private String E_TD012;// AS '區間結束 預交日',
	private List<String> E_list;// '排除清單',(TC001+'-'+TC002+'-'+TD003+'-'+TD004) 採購單號+項次+品號

	public String getTD004() {
		return TD004;
	}

	public void setTD004(String tD004) {
		TD004 = tD004;
	}

	public String getTD005() {
		return TD005;
	}

	public void setTD005(String tD005) {
		TD005 = tD005;
	}

	public String getTD006() {
		return TD006;
	}

	public void setTD006(String tD006) {
		TD006 = tD006;
	}

	public String getTD012() {
		return TD012;
	}

	public void setTD012(String tD012) {
		TD012 = tD012;
	}

	public String getTC004() {
		return TC004;
	}

	public void setTC004(String tC004) {
		TC004 = tC004;
	}

	public String getMA002() {
		return MA002;
	}

	public void setMA002(String mA002) {
		MA002 = mA002;
	}

	public String getMA011() {
		return MA011;
	}

	public void setMA011(String mA011) {
		MA011 = mA011;
	}

	public String getTD007() {
		return TD007;
	}

	public void setTD007(String tD007) {
		TD007 = tD007;
	}

	public String getMC002() {
		return MC002;
	}

	public void setMC002(String mC002) {
		MC002 = mC002;
	}

	public Integer getTD008() {
		return TD008;
	}

	public void setTD008(Integer tD008) {
		TD008 = tD008;
	}

	public Integer getTD015() {
		return TD015;
	}

	public void setTD015(Integer tD015) {
		TD015 = tD015;
	}

	public Integer getTD008_TD015() {
		return TD008_TD015;
	}

	public void setTD008_TD015(Integer tD008_TD015) {
		TD008_TD015 = tD008_TD015;
	}

	public String getTD016() {
		return TD016;
	}

	public void setTD016(String tD016) {
		TD016 = tD016;
	}

	public String getTC001_TC002() {
		return TC001_TC002;
	}

	public void setTC001_TC002(String tC001_TC002) {
		TC001_TC002 = tC001_TC002;
	}

	public String getS_TD012() {
		return S_TD012;
	}

	public void setS_TD012(String s_TD012) {
		S_TD012 = s_TD012;
	}

	public String getE_TD012() {
		return E_TD012;
	}

	public void setE_TD012(String e_TD012) {
		E_TD012 = e_TD012;
	}

	public List<String> getE_list() {
		return E_list;
	}

	public void setE_list(List<String> e_list) {
		E_list = e_list;
	}

	public String getTD003() {
		return TD003;
	}

	public void setTD003(String tD003) {
		TD003 = tD003;
	}

	public String getTC011() {
		return TC011;
	}

	public void setTC011(String tC011) {
		TC011 = tC011;
	}
}
