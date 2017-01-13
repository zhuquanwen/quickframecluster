package com.iscas.quickframe.service;

import com.iscas.quickframe.domain.p.App;
import com.iscas.quickframe.domain.p.AppDao;

/**
*@auhor:zhuquanwen
*@date:2016年12月6日
*@desc:
*/
public interface AppService extends BaseService<App, AppDao>{
	//新增一个系统
	public App newOneApp(String appName,String desc);
	//删除一个系统
	public void removeOneApp(Integer id);
	//按名字查询
	public App findByName(String name);
}
