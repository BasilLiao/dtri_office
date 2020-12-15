package dtri.com.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dtri.com.db.entity.GroupEntity;
import dtri.com.db.entity.UserEntity;
import dtri.com.db.pgsql.dao.UserDao;
import dtri.com.models.MD5HashModel;

@Transactional
@Service
public class LoginService {
	@Autowired
	private UserDao userDao;
	@Autowired
	private HttpSession session;
	// log 訊息
	private static Logger logger = LogManager.getLogger();

	private static UserEntity testusersEntity;
	static {
		// 如果沒有DB 帳密資訊 則
		testusersEntity = new UserEntity();
		testusersEntity.setId(1);
		testusersEntity.setName("Test");
		testusersEntity.setAccount("test@dtri.com");
		testusersEntity.setPassword("123456");
	}

	/** DB 檢查帳密 */
	public UserEntity checkAccountAndPassword(String account, String password) {
		boolean check = false;
		MD5HashModel md5 = new MD5HashModel();
		UserEntity userEntity = userDao.queryByAccountAndPassword(account, md5.md5(password));
		// userEntity = testusersEntity;
		if (userEntity != null) {
			check = true;
		}
		System.out.println("DB 檢查帳密 :" + account + " 結果:" + check);
		return userEntity;
	}

	/** session 檢查 */
	public boolean checkLogin() {
		boolean check = false;
		try {
			System.out.println(session.getId());
			UserEntity userEntity = (UserEntity) session.getAttribute("UserEntity");
			if (userEntity != null) {
				check = true;
			} else {
				// 清除session
				sessionLogout();
			}
			System.out.println("是否 已經登入 :" + check);
			
		} catch (Exception e) {
			logger.warn(e);
			System.out.println(e);
		}
		return check;
	}

	/** session 取出 */
	public UserEntity getSessionUserBean() {
		return (UserEntity) session.getAttribute("UserEntity");
	}

	@SuppressWarnings("unchecked")
	public List<GroupEntity> getSessionGroupBean() {
		return (List<GroupEntity>) session.getAttribute("GroupEntity");
	}

	/** 檢查session完整性 **/
	public Boolean checkUser(UserEntity user, List<GroupEntity> group) {
		// 檢查物件內是否有值
		if (user.getId() != null && user.getName() != null && user.getAccount() != null
				&& (user.getUseful() == 3 || user.getUseful() == 2) && group.size() > 0) {
			return true;
		}
		return false;
	}

	/** session 寫入 */
	public boolean sessionLogin(UserEntity user, List<GroupEntity> group) {
		session.setAttribute("UserEntity", user);
		session.setAttribute("GroupEntity", group);
		return true;
	}

	/** session 清空 */
	public boolean sessionLogout() {
		session.removeAttribute("UserEntity");
		session.removeAttribute("GroupEntity");
		session.invalidate();
		return true;

	}

	/** 取得 個人 群組權限 **/
	public List<GroupEntity> getUserGroup(UserEntity userEntity) {
		List<GroupEntity> entity = userDao.queryByPermission_id(userEntity.getGroup_id());
		return entity;
	}

	// 檢查權限
	public boolean checkPermission(List<GroupEntity> group, String webpag, String lvPermission) {
		boolean checkPermission = false;
		int aInt = Integer.parseInt(lvPermission, 2);
		int bInt = 0;
		for (GroupEntity one : group) {
			if (one.getPermission_control().equals(webpag)) {
				bInt = Integer.parseInt(one.getPermission(), 2);
				if (aInt == (aInt & bInt)) {
					// 有權限
					checkPermission = true;
					break;
				}
			}
		}
		return checkPermission;
	}
}
