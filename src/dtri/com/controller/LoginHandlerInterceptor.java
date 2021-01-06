package dtri.com.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import dtri.com.bean.JsonObjBean;
import dtri.com.db.entity.GroupEntity;
import dtri.com.db.entity.UserEntity;
import dtri.com.models.JsonDataModel;
import dtri.com.service.LoginService;

/**
 * LoginHandlerInterceptor 登入檢查
 */
public class LoginHandlerInterceptor implements HandlerInterceptor {
	@Autowired
	LoginService loginService;

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object hander, Exception ex)
			throws Exception {

	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object hander, ModelAndView ex)
			throws Exception {

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object hander) throws Exception {
		System.out.println("LoginHandlerInterceptor : 登入檢查");
		String account = request.getParameter("inputAccount");
		String password = request.getParameter("inputPassword");
		// Step1.是否 先前 有登入?
		if (account==null && password == null && loginService.checkLogin()) {
			// Step1-2.檢查資料完整性
			UserEntity user = loginService.getSessionUserBean();
			List<GroupEntity> groupEntity = loginService.getSessionGroupBean();
			//行政人員-檢查
			Boolean check = loginService.checkUser(user, groupEntity);
			//作業員-檢查
			
			if (check) {
				// Step1-3.通過
				return true;
			}
		} else if (account != null && password != null) {
			// Step2.帳密是否正確
			UserEntity userEntity = loginService.checkAccountAndPassword(account, password);
			if (userEntity != null) {
				// Step1-2.寫入session登入訊息
				List<GroupEntity> groupEntity = loginService.getUserGroup(userEntity);
				loginService.sessionLogin(userEntity, groupEntity);
				UserEntity user = loginService.getSessionUserBean();
				Boolean check = loginService.checkUser(user, groupEntity);
				if (check) {
					// Step1-3.通過
					return true;
				}
			}
		}
		// Step1-4.不通過
		JSONObject r_allData = new JSONObject();
		JsonObjBean bean = new JsonObjBean();
		JsonDataModel data = new JsonDataModel();
		bean.setR_status(false);
		bean.setR_message("登入失敗 或 逾時,請重新登入");
		data.setR_JsonData(bean);
		r_allData = data.getR_allData();
		loginService.sessionLogout();
		
		request.setAttribute("allData", r_allData);
		request.getRequestDispatcher("login.jsp").forward(request, response);
		return false;
	}
}
