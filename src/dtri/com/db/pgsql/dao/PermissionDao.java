package dtri.com.db.pgsql.dao;

import java.util.List;

import dtri.com.db.entity.PermissionEntity;

public interface PermissionDao {
	/**
	 * 有效單元 與 權限
	 * 
	 * @return List<PermissionEntity>
	 */
	List<PermissionEntity> queryAll(PermissionEntity entity);

	Boolean addedOne(PermissionEntity entity);

	Boolean addedRepeatGroupOne(PermissionEntity entity);

	Boolean updateOne(PermissionEntity entity);

	Boolean deleteOne(PermissionEntity entity);

	Boolean break_system_permission_group_id_seq();

}