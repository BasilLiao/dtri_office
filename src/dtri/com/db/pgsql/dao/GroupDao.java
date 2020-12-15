package dtri.com.db.pgsql.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import dtri.com.db.entity.GroupEntity;

public interface GroupDao {
	/**
	 * 有效單元 與 權限
	 * 
	 * @return List<GroupEntity>
	 */
	List<GroupEntity> queryAll(@Param("checkName") String checkName, @Param("byName") String byName,@Param("useful") Integer useful);

	Integer addedOne(GroupEntity entity);

	Integer updateOne(GroupEntity entity);
	
	Integer updateAll();

	Integer deleteGroupOne(GroupEntity entity);
	
	Integer deletePermissionOne(GroupEntity entity);
	
	Integer nextvalGroup();

}