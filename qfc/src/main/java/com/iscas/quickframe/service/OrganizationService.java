package com.iscas.quickframe.service;

import com.iscas.quickframe.domain.p.App;
import com.iscas.quickframe.domain.p.Organizational;
import com.iscas.quickframe.domain.p.OrganizationalDao;

/**
*@auhor:zhuquanwen
*@date:2016年12月6日
*@desc:
*/
public interface OrganizationService extends BaseService<Organizational,OrganizationalDao>{
	public Organizational findByAppAndParentOrg(App app, Organizational parentOrg);
}
