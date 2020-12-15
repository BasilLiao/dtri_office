package dtri.com.models;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import dtri.com.bean.FtpUtilBean;

public class FtpUtilModel {

	/**
	 * https://www.itread01.com/content/1541594827.html
	 * 獲取FTPClient物件
	 *
	 * @param ftpHost     FTP主機伺服器
	 * @param ftpPassword FTP 登入密碼
	 * @param ftpUserName FTP登入使用者名稱
	 * @param ftpPort     FTP埠 預設為21
	 * @return
	 */
	private static FTPClient getFTPClient(String ftpHost, String ftpUserName, String ftpPassword, int ftpPort) {
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient = new FTPClient();
			ftpClient.connect(ftpHost, ftpPort);// 連線FTP伺服器
			ftpClient.login(ftpUserName, ftpPassword);// 登陸FTP伺服器
			if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
				System.out.println("未連線到FTP，使用者名稱或密碼錯誤。");
				ftpClient.disconnect();
			} else {
				System.out.println("FTP連線成功。");
			}
		} catch (SocketException e) {
			e.printStackTrace();
			System.out.println("FTP的IP地址可能錯誤，請正確配置。");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("FTP的埠錯誤,請正確配置。");
		}
		return ftpClient;
	}

	/***
	 * 從FTP伺服器下載檔案
	 * 
	 * @param ftpHost     FTP IP地址
	 * @param ftpUserName FTP 使用者名稱
	 * @param ftpPassword FTP使用者名稱密碼
	 * @param ftpPort     FTP埠
	 * @param ftpPath     FTP伺服器中檔案所在路徑 格式： ftptest/aa
	 * @param localPath   下載到本地的位置 格式：H:/download
	 * @param fileName    檔名稱
	 */
	public static void downloadFtpFile(FtpUtilBean ftp) {

		FTPClient ftpClient = null;

		try {
			ftpClient = getFTPClient(ftp.getFtpHost(), ftp.getFtpUserName(), ftp.getFtpPassword(), ftp.getFtpPort());
			ftpClient.setControlEncoding("UTF-8"); // 中文支援
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();
			ftpClient.changeWorkingDirectory(ftp.getFtpPath());

			File localFile = new File(ftp.getLocalPath() + File.separatorChar + ftp.getFileName());
			OutputStream os = new FileOutputStream(localFile);
			ftpClient.retrieveFile(ftp.getFileName(), os);
			os.close();
			ftpClient.logout();

		} catch (FileNotFoundException e) {
			System.out.println("沒有找到" + ftp.getFtpPath() + "檔案");
			e.printStackTrace();
		} catch (SocketException e) {
			System.out.println("連線FTP失敗.");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("檔案讀取錯誤。");
			e.printStackTrace();
		}

	}

	/**
	 * Description: 向FTP伺服器上傳檔案
	 * 
	 * @param ftpHost     FTP伺服器hostname
	 * @param ftpUserName 賬號
	 * @param ftpPassword 密碼
	 * @param ftpPort     埠
	 * @param ftpPath     FTP伺服器中檔案所在路徑 格式： ftptest/aa
	 * @param fileName    ftp檔名稱
	 * @param input       檔案流
	 * @return 成功返回true，否則返回false
	 */
	public static boolean uploadFile(FtpUtilBean ftp) {
		boolean success = false;
		FTPClient ftpClient = null;
		try {
			int reply;
			ftpClient = getFTPClient(ftp.getFtpHost(), ftp.getFtpUserName(), ftp.getFtpPassword(), ftp.getFtpPort());
			reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				return success;
			}
			ftpClient.setControlEncoding("UTF-8"); // 中文支援
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();
			ftpClient.changeWorkingDirectory(ftp.getFtpPath());

			ftpClient.storeFile(ftp.getFileName(), ftp.getInput());

			ftp.getInput().close();
			ftpClient.logout();
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
		return success;
	}

}