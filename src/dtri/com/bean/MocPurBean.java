package dtri.com.bean;

import java.util.ArrayList;

import org.json.JSONArray;

public class MocPurBean {
	private boolean checkMakeId;
	private boolean checkInside;
	private ArrayList<String> mocPurMakeIds;
	private JSONArray mocPurMakeIdJs;
	private String mocPurId;

	public boolean isCheckMakeId() {
		return checkMakeId;
	}

	public void setCheckMakeId(boolean checkMakeId) {
		this.checkMakeId = checkMakeId;
	}

	public boolean isCheckInside() {
		return checkInside;
	}

	public void setCheckInside(boolean checkInside) {
		this.checkInside = checkInside;
	}

	public ArrayList<String> getMocPurMakeIds() {
		return mocPurMakeIds;
	}

	public void setMocPurMakeIds(ArrayList<String> mocPurMakeIds) {
		this.mocPurMakeIds = mocPurMakeIds;
	}

	public JSONArray getMocPurMakeIdJs() {
		return mocPurMakeIdJs;
	}

	public void setMocPurMakeIdJs(JSONArray mocPurMakeIdJs) {
		this.mocPurMakeIdJs = mocPurMakeIdJs;
		// 複數查詢資料轉換
		if (mocPurMakeIdJs != null) {
			this.mocPurMakeIds = new ArrayList<String>();
			for (int i = 0; i < mocPurMakeIdJs.length(); i++) {
				this.mocPurMakeIds.add(mocPurMakeIdJs.getString(i));
			}
			if (this.mocPurMakeIds.get(0).equals("")) {
				this.mocPurMakeIds = null;
			}
		}
	}

	public String getMocPurId() {
		return mocPurId;
	}

	public void setMocPurId(String mocPurId) {
		this.mocPurId = mocPurId;
	}

}
