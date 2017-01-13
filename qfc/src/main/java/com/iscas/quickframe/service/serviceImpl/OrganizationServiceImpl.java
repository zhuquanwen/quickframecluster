package com.iscas.quickframe.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iscas.quickframe.domain.p.App;
import com.iscas.quickframe.domain.p.Organizational;
import com.iscas.quickframe.domain.p.OrganizationalDao;
import com.iscas.quickframe.service.OrganizationService;

/**
*@auhor:zhuquanwen
*@date:2016年12月6日
*@desc:
*/
@Service
public class OrganizationServiceImpl extends BasePServiceImpl<Organizational, OrganizationalDao> implements OrganizationService{
	@Autowired
	private OrganizationalDao organizationalDao;
	@Override
	public Organizational findByAppAndParentOrg(App app, Organizational parentOrg) {
		return organizationalDao.findByAppAndParentOrg(app, parentOrg);
	}
	
}
