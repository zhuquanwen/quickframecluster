package com.iscas.quickframe.domain.p;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RoleDao extends JpaRepository<Role, Long>{
	
	@Modifying
	@Query(nativeQuery = true, value = "delete from t_role where id in (select a.idx from (select distinct t1.id as idx "
		+ "from t_role t1,t_org_role t2 ,t_organizational t3,  "
		+ "t_app t4 where t1.id = t2.role_id and t2.org_id = t3.id "
		+ "and t3.app_id = t4.id and t4.id = ?1) a)")
	//暂时不使用
	public void deleteByAppId(Integer id);
	
	@Modifying
	@Query(nativeQuery = true, value = "delete from t_org_role where role_id in (select a.idx from (select distinct t2.role_id as idx "
		+ "from t_role t1,t_org_role t2 ,t_organizational t3,  "
		+ "t_app t4 where t1.id = t2.role_id and t2.org_id = t3.id "
		+ "and t3.app_id = t4.id and t4.id = ?1) a)")
	//暂时不使用
	public void deleteRoleOrgByAppId(Integer id);
	
	
	@Query(nativeQuery = true, value = "select distinct t1.id as idx "
			+ "from t_role t1,t_org_role t2 ,t_organizational t3,  "
			+ "t_app t4 where t1.id = t2.role_id and t2.org_id = t3.id "
			+ "and t3.app_id = t4.id and t4.id = ?1")
	public List<Integer> findByAppId(Integer id);
}
