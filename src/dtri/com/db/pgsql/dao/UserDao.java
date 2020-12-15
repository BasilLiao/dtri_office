package dtri.com.db.pgsql.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import dtri.com.db.entity.GroupEntity;
import dtri.com.db.entity.UserEntity;

public interface UserDao {
	/**
	 * 通過 帳號密碼 查詢單一
	 * @param account 帳號
	 * @param password 密碼
	 * @return UsersEntity
	 */
	UserEntity queryByAccountAndPassword(@Param("account")String account, @Param("password")String password);
	/**
	 * 通過 帳號 查詢單一
	 * @param account 帳號
	 * @return UsersEntity
	 */	
	UserEntity queryByAccount(@Param("account")String account);
	/**
	 * 通過 群組ID 查詢 權限清單
	 * @param account 帳號
	 * @return UsersEntity
	 */	
	List<GroupEntity> queryByPermission_id(@Param("group_id")Integer group_id);
	/**
	 * 
	 * 一般查詢
	 * @param name 浮動項目查詢
	 * **/
	List<UserEntity> queryAll(UserEntity entity);
	/**
	 * 添加使用帳號
	 * 
	 * **/
	Integer addedOne(UserEntity entity);
	/**
	 * 添加使用帳號
	 * 
	 * **/
	Integer updateOne(UserEntity entity);
	
	/**
	 * 帳號 移除
	 * 
	 * **/
	Integer deleteOne(UserEntity entity);
	/**更新 user 與 group名稱**/
	Integer updateAll();
	
	
}