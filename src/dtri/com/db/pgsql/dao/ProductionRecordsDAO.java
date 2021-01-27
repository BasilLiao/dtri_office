package dtri.com.db.pgsql.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import dtri.com.db.entity.ProductionRecordsEntity;

/**
 * 紀錄-需生產單
 * 
 **/
public interface ProductionRecordsDAO {

	// 新添加 1筆資料前 查一下
	ProductionRecordsEntity beforeCheckAddOne(@Param("id") String id);

	// 新添加 1筆資料
	Integer addOne(ProductionRecordsEntity entity);

	// 更新單據狀態
	Integer updateOneStatus(ProductionRecordsEntity entity);

	// 更新單據特定內容
	Integer updateOneContent(ProductionRecordsEntity entity);

	// 取得全部資料
	List<ProductionRecordsEntity> queryProductionRecords(@Param("where_product") ProductionRecordsEntity entity,
			@Param("product_limit") String limit);

	// 更新進度
	Integer updateOneProgress(ProductionRecordsEntity entity);
	
	//限定移除
	Integer deleteOneProgress(ProductionRecordsEntity entity);
	

}
