package com.iscas.quickframe.domain.p;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
*@auhor:zhuquanwen
*@date:2016年12月13日
*@desc:
*/
public interface MenuDao extends JpaRepository<Menu, Long>{
	@Query(value="select distinct t2.id as idx from t_menu_role t1 ,t_menu t2, "
			+ "t_role t3,t_org_role t4,t_organizational t5,t_app t6 "
			+ "where t2.id = t1.menu_id and t1.role_id = t3.id "
			+ "and t3.id = t4.role_id and t4.org_id = t5.id and "
			+ "t5.app_id = t6.id and t6.id = ?1 ",nativeQuery=true)
	public List<Integer> findByAppId(Integer id);

}
