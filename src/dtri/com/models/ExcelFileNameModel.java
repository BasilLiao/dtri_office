package dtri.com.models;

import dtri.com.bean.MocPurBean;

public class ExcelFileNameModel {

	public static String PUR_MOC_INV_FileName(MocPurBean mocPurBean) {
		System.out.println("多筆 檔案名稱");
		String fileNameTerm = "";
		// 檔案名稱
		if (mocPurBean.getMocPurMakeIds() != null && !mocPurBean.getMocPurMakeIds().get(0).equals("")) {
			String like_h = "NOT LIKE ";
			if (mocPurBean.isCheckMakeId()) {
				like_h = "LIKE ";
			}
			fileNameTerm = fileNameTerm + like_h;
		}
		return fileNameTerm;
	}

	public static String PUR_MOC_INV_FileNameOnly(MocPurBean mocPurBean) {
		System.out.println("單筆 檔案名稱");
		// 檔案名稱
		String fileNameTerm = "N_" + mocPurBean.getMocPurId();
		if (mocPurBean.getMocPurMakeIds() != null && !mocPurBean.getMocPurMakeIds().get(0).equals("")) {
			String like_h = " NOT_LIKE_";
			if (mocPurBean.isCheckMakeId()) {
				like_h = " LIKE_";
			}
			fileNameTerm = fileNameTerm + like_h + mocPurBean.isCheckMakeId();
		}
		return fileNameTerm;
	}
}
