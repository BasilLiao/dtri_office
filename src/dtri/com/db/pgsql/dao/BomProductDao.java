package dtri.com.db.pgsql.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import dtri.com.db.entity.BomGroupEntity;
import dtri.com.db.entity.BomProductEntity;
import dtri.com.db.entity.BomTypeItemEntity;

public interface BomProductDao {

	/** 依照ID查詢 **/
	BomProductEntity queryProductbyId(@Param("id") Integer id);

	/**
	 * 
	 * 一般查詢(清單查詢 )
	 * 
	 * @param name 浮動項目查詢
	 **/
	List<BomProductEntity> queryProduct(@Param("where_product") String where_product,
			@Param("product_limit") String limit);

	ArrayList<BomGroupEntity> queryGroup(@Param("where_group") String where_group,
			@Param("group_limit_in") List<Integer> limit);

	List<BomTypeItemEntity> queryTypeItem(@Param("where_item") String where_item);

	/**
	 * 
	 * 檢查 bom_number 名稱是否重複
	 * */
	BomProductEntity queryProduct_bom_number(BomProductEntity entity);

	/**
	 * 第一次檢查
	 * 
	 **/
	Integer checkedOne(@Param("id") Integer id, @Param("name") String name);

	/**
	 * 添加 產品 /項目群組
	 * 
	 **/
	Integer addedOne(BomProductEntity entity);

	Integer addedOneGroup(BomGroupEntity entity);

	/**
	 * 取得下個ID(BOM_Group)
	 * 
	 **/
	Integer nextvalGroup();

	/**
	 * 產品/群組項目 移除
	 * 
	 **/
	Integer deleteGroupByProduct_id(@Param("product_id") Integer product_id);

	Integer deleteProduct(@Param("id") Integer id);

	Integer deleteGroupOther(BomGroupEntity entity);

	/** 更新 product 與 group名稱 **/

	Integer updateProduct(BomProductEntity entity);

	Integer updateProductKind(@Param("id") Integer id, @Param("kind") Integer kind);

	Integer updateGroup(BomGroupEntity entity);

}