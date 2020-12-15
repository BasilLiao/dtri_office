package dtri.com.bean;

import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;
/**
 * Description: 向FTP伺服器 參數容器
 * 
 * @param ftpHost     FTP伺服器hostname
 * @param ftpUserName 賬號
 * @param ftpPassword 密碼
 * @param ftpPort     埠
 * @param ftpPath     FTP伺服器中檔案所在路徑 格式： ftptest/aa
 * @param fileName    ftp檔名稱
 * @param input       檔案流
 * @param ftpClient	  FTP取得物件
 */
public class FtpUtilBean {
	private String ftpHost;
	private String ftpUserName; 
	private String ftpPassword; 
	private int ftpPort;
	private String ftpPath; 
	private String localPath;
	private String fileName;
	private InputStream input;
	private FTPClient ftpClient;
	
	public FtpUtilBean(String ftpHost,String ftpUserName,String ftpPassword,int ftpPort) {
		this.ftpHost = ftpHost;
		this.ftpUserName = ftpUserName;
		this.ftpPassword = ftpPassword;
		this.ftpPort = ftpPort;
	}
	
	public String getFtpHost() {
		return ftpHost;
	}
	public void setFtpHost(String ftpHost) {
		this.ftpHost = ftpHost;
	}
	public String getFtpUserName() {
		return ftpUserName;
	}
	public void setFtpUserName(String ftpUserName) {
		this.ftpUserName = ftpUserName;
	}
	public String getFtpPassword() {
		return ftpPassword;
	}
	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}
	public int getFtpPort() {
		return ftpPort;
	}
	public void setFtpPort(int ftpPort) {
		this.ftpPort = ftpPort;
	}
	public String getFtpPath() {
		return ftpPath;
	}
	public void setFtpPath(String ftpPath) {
		this.ftpPath = ftpPath;
	}
	public String getLocalPath() {
		return localPath;
	}
	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public InputStream getInput() {
		return input;
	}
	public void setInput(InputStream input) {
		this.input = input;
	}
	public FTPClient getFtpClient() {
		return ftpClient;
	}
	public void setFtpClient(FTPClient ftpClient) {
		this.ftpClient = ftpClient;
	}
}
