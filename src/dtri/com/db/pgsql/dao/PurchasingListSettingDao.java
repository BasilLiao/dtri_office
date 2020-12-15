package dtri.com.db.pgsql.dao;

import java.util.List;

import dtri.com.db.entity.PurchasingListEntity;
import dtri.com.db.entity.PurchasingMailEntity;
import dtri.com.db.entity.PurchasingSettingEntity;

public interface PurchasingListSettingDao {
	/**
	 * 採購 自動發信功能 與 設定 樣板
	 * 
	 * @return List<Purchasing>
	 */
	List<PurchasingListEntity> queryAll(PurchasingListEntity entity);
	
	Integer AddedOne(PurchasingListEntity entity);
	
	Integer deleteExpired(PurchasingListEntity entity);
	
	List<PurchasingSettingEntity> querySetting();

	PurchasingMailEntity queryMail();

	Integer update_m_One(PurchasingMailEntity entity);

	Integer update_s_One(PurchasingSettingEntity entity);

	


	
}