package com.iscas.quickframe.domain.p;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserDao extends JpaRepository<User, Long>{
	public User findByUsername(String name);
	@Query(nativeQuery=true,value="select  t1.* from t_user t1, "
			+ "t_organizational t2, t_app t3, t_org_user t4 "
			+ "where t1.id = t4.user_id and t2.id = t4.org_id and "
			+ "t3.id = t2.app_id and t3.name = ?1 and t1.username = ?2")
	public User findByUsernameAndAppName(String appName, String username);
	
}
