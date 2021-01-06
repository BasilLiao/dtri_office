package dtri.com.db.pgsql.dao;

import java.util.List;

import dtri.com.db.entity.ProductionSnEntity;

/**
 * 產品身分序號 SN
 * 
 **/
public interface ProductionSnDao {
	// 查詢
	List<ProductionSnEntity> queryProductionSn();

	// 指查詢 最新內容
	ProductionSnEntity queryProductionSnContent();

	// 新增
	Integer addedProductionSn(ProductionSnEntity entity);

	// 移除 ALL
	Integer deleteProductionSn();

	// 更新 最新規則
	Integer updateProductionSn(ProductionSnEntity entity);

	// 更新 最新指定規則
	Integer updateProductionSnById(ProductionSnEntity entity);

}