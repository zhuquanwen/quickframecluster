package com.iscas.quickframe.domain.p;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
public interface RequestDataDao extends JpaRepository<RequestData, Long>{
	@Query(value="select t2.type from t_request_data_role t1 ,t_request_data t2 where t2.id = t1.request_data_id and t1.role_id = ?1 ",nativeQuery=true)
	public List<String> findByRoleId(Integer roleId);
	@Modifying
	@Query(value="delete from t_request_data where id in (select a.idx from (select distinct t2.id as idx from t_request_data_role t1 ,t_request_data t2, "
			+ "t_role t3,t_org_role t4,t_organizational t5,t_app t6 "
			+ "where t2.id = t1.request_data_id and t1.role_id = t3.id "
			+ "and t3.id = t4.role_id and t4.org_id = t5.id and "
			+ "t5.app_id = t6.id and t6.id = ?1) a) ",nativeQuery=true)
	//暂时不使用
	public void deleteByAppId(Integer id);
	@Modifying
	@Query(value="delete from t_request_data_role where request_data_id in (select a.idx from (select distinct t1.request_data_id as idx from t_request_data_role t1 ,t_request_data t2, "
			+ "t_role t3,t_org_role t4,t_organizational t5,t_app t6 "
			+ "where t2.id = t1.request_data_id and t1.role_id = t3.id "
			+ "and t3.id = t4.role_id and t4.org_id = t5.id and "
			+ "t5.app_id = t6.id and t6.id = ?1) a) ",nativeQuery=true)
	//暂时不使用
	public void deleteRequestRoleByAppId(Integer id);
	
	@Query(value="select distinct t2.id as idx from t_request_data_role t1 ,t_request_data t2, "
			+ "t_role t3,t_org_role t4,t_organizational t5,t_app t6 "
			+ "where t2.id = t1.request_data_id and t1.role_id = t3.id "
			+ "and t3.id = t4.role_id and t4.org_id = t5.id and "
			+ "t5.app_id = t6.id and t6.id = ?1 ",nativeQuery=true)
	public List<Integer> findByAppId(Integer id);
}
