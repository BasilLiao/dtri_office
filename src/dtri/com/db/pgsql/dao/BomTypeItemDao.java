package dtri.com.db.pgsql.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import dtri.com.db.entity.BomTypeItemEntity;

public interface BomTypeItemDao {

	/**
	 * 
	 * 一般查詢
	 * 
	 * @param group_name 只有群組查詢
	 **/
	List<BomTypeItemEntity> queryAll(String group_name);
	List<BomTypeItemEntity> queryAllOrderi0102(String group_name);
	List<BomTypeItemEntity> queryAllByGroup_id();
	
	/**
	 * 添加 項目群組
	 * 
	 **/
	Integer addedOneGroup(BomTypeItemEntity entity);

	/**
	 * 添加使用帳號
	 * 
	 **/
	Integer updateOne(BomTypeItemEntity entity);

	/** 取得下筆ID **/
	Integer nextvalItemGroup();

	/**
	 * 群組 移除
	 * 
	 **/
	Integer deleteOne(BomTypeItemEntity entity);

	/**
	 * 指定群組內的 特定 項目移除(排除)
	 * 
	 **/
	Integer deleteOther(@Param("group_id") Integer group_id, @Param("others") String others);

	/** 更新 user 與 group名稱 **/
	Integer updateAll();

}