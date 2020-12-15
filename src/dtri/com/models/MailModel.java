package dtri.com.models;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import dtri.com.bean.SendMailBean;

/**
 * 範例寄信件
 * 
 * http://tw.gitbook.net/javamail_api/javamail_api_gmail_smtp_server.html
 * http://pclevin.blogspot.com/2014/11/java-mail-gmail-smtp-server.html
 */

public class MailModel {
	public static boolean sendMail(SendMailBean setting) {
		// 設定
		Properties props = new Properties();
		props.put("mail.smtp.host", setting.getHost());
		props.put("mail.smtp.auth", setting.getAuth());
		props.put("mail.smtp.starttls.enable", setting.getStarttls());
		props.put("mail.smtp.port", setting.getPort());
		Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(setting.getSmtp_acc(), setting.getSmtp_pass());
			}
		});
		// 寄件
		try {
			MimeMessage  message = new MimeMessage(session);
			message.setFrom(new InternetAddress(setting.getSmtp_acc()));
			message.setRecipients(Message.RecipientType.TO, setting.getMail_who());
			message.setSubject(setting.getMail_title(),"utf-8");
			//message.setText();
			message.setContent(setting.getMail_content(),"text/html; charset=UTF-8");
			Transport transport = session.getTransport("smtp");
			// "vdzwtmlqmmuvexsf";// your password
			transport.connect(setting.getHost(), setting.getPort(), setting.getSmtp_acc(), setting.getSmtp_pass());
			Transport.send(message);

			System.out.println("寄送email結束.");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		return true;
	}
}