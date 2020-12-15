package dtri.com.db.pgsql.dao;

import java.util.List;

import dtri.com.db.entity.SoftwareVersionEntity;

public interface SoftwareVersionDao {

	/**
	 * 
	 * 一般查詢
	 * @param name 浮動項目查詢
	 * **/
	List<SoftwareVersionEntity> queryAll(SoftwareVersionEntity entity);
	/**
	 * 添加 軟體控管
	 * 
	 * **/
	Integer addedOne(SoftwareVersionEntity entity);
	/**
	 * 更新 軟體控管
	 * 
	 * **/
	Integer updateOne(SoftwareVersionEntity entity);
	
	/**
	 * 移除 軟體控管
	 * 
	 * **/
	Integer deleteOne(SoftwareVersionEntity entity);

	
	
}