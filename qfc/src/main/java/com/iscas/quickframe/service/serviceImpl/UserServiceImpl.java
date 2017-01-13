package com.iscas.quickframe.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iscas.quickframe.domain.p.User;
import com.iscas.quickframe.domain.p.UserDao;
import com.iscas.quickframe.service.UserService;

/**
*@auhor:zhuquanwen
*@date:2016年12月6日
*@desc:
*/
@Service
public class UserServiceImpl extends BasePServiceImpl<User,UserDao> implements UserService{
	@Autowired
	private UserDao userDao;
	@Override
	public User findByUsername(String username) {
		return userDao.findByUsername(username);
	}
	@Override
	public User findByUsernameAndAppName(String username, String appName) {
		return userDao.findByUsernameAndAppName(username, appName);
	}
	
	

}
