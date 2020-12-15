package dtri.com.db.pgsql.dao;

import java.util.List;

import dtri.com.db.entity.PurchasingEntity;

public interface PurchasingDao {
	/**
	 * 採購 自動發信功能
	 * 
	 * @return List<Purchasing>
	 */
	List<PurchasingEntity> queryAll(PurchasingEntity entity);

	Integer addedOne(PurchasingEntity entity);

	Integer updateOne(PurchasingEntity entity);

	Integer deleteOne(PurchasingEntity entity);
	
}