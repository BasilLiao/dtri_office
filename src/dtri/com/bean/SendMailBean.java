package dtri.com.bean;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/*權限資料 
 * 
 * 
 * 
 * **/
public class SendMailBean {

	// 對象與內容
	private boolean mail_cc;
	private String[] mail_cc_who;
	private Address[]  mail_who;
	private String mail_title;
	private String mail_content;
	// 跳板帳密
	private String smtp_acc;
	private String smtp_pass;
	// 設定組態
	private String host;
	private Integer port;
	private String auth;
	private String starttls;
	
	public SendMailBean() {
		this.mail_cc = false;
		this.mail_title = "測試通知";
		this.mail_content = "測試內容";
		this.mail_cc = false;
		this.smtp_acc = "dtrpur@gmail.com";
		this.smtp_pass = "vdzwtmlqmmuvexsf";
		
		this.host = "smtp.gmail.com";
		this.port = 587;
		this.auth = "true";
		this.starttls = "true";
	}

	public boolean isMail_cc() {
		return mail_cc;
	}

	public void setMail_cc(boolean mail_cc) {
		this.mail_cc = mail_cc;
	}

	public String[] getMail_cc_who() {
		return mail_cc_who;
	}

	public void setMail_cc_who(String[] mail_cc_who) {
		this.mail_cc_who = mail_cc_who;
	}

	public Address[] getMail_who() {
		return mail_who;
	}

	public void setMail_who(String mail_who) {
		try {
			this.mail_who = InternetAddress.parse(mail_who);
		} catch (AddressException e) {
			e.printStackTrace();
		}
	}

	public String getMail_title() {
		return mail_title;
	}

	public void setMail_title(String mail_title) {
		this.mail_title = mail_title;
	}

	public String getMail_content() {
		return mail_content;
	}

	public void setMail_content(String mail_content) {
		this.mail_content = mail_content;
	}

	public String getSmtp_acc() {
		return smtp_acc;
	}

	public void setSmtp_acc(String smtp_acc) {
		this.smtp_acc = smtp_acc;
	}

	public String getSmtp_pass() {
		return smtp_pass;
	}

	public void setSmtp_pass(String smtp_pass) {
		this.smtp_pass = smtp_pass;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public String getStarttls() {
		return starttls;
	}

	public void setStarttls(String starttls) {
		this.starttls = starttls;
	}

}
