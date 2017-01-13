package com.iscas.quickframe.domain.p;

import org.springframework.data.jpa.repository.JpaRepository;

/**
*@auhor:zhuquanwen
*@date:2016年12月6日
*@desc:
*/
public interface OrganizationalDao extends JpaRepository<Organizational, Long>{
	public Organizational findByAppAndParentOrg(App app, Organizational parentOrg);
}
