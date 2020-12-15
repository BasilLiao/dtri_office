package dtri.com.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dtri.com.db.entity.ERP_PurchasingEntity;
import dtri.com.db.mssql.dao.ERP_PurchasingListDao;

@Transactional
@Service
public class ERP_PurchasingListService {
	/**
	 * 取得
	 * 
	 * @return 查詢後清單
	 * 
	 **/
	@Autowired
	ERP_PurchasingListDao listDao;
	
	public List<ERP_PurchasingEntity> searchERP_PurchasingList(ERP_PurchasingEntity event) {
		List<ERP_PurchasingEntity> entities= new ArrayList<ERP_PurchasingEntity>(); 
		entities = listDao.queryERP_PurchasingList(event);
	return entities;
	}

}
