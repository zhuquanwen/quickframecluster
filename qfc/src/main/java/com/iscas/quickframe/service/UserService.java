package com.iscas.quickframe.service;

import com.iscas.quickframe.domain.p.User;
import com.iscas.quickframe.domain.p.UserDao;

/**
*@auhor:zhuquanwen
*@date:2016年12月6日
*@desc:
*/
public interface UserService extends BaseService<User,UserDao>{
	public User findByUsername(String username);
	public User findByUsernameAndAppName(String username,String appName);
}
