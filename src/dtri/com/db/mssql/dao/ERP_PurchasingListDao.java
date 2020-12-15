package dtri.com.db.mssql.dao;

import java.util.List;

import dtri.com.db.entity.ERP_PurchasingEntity;

public interface ERP_PurchasingListDao {

	/**
	 * 
	 * 一般查詢(清單查詢 )
	 * 
	 * @param 時間區間/清單排除
	 **/
	List<ERP_PurchasingEntity> queryERP_PurchasingList(ERP_PurchasingEntity list);

}