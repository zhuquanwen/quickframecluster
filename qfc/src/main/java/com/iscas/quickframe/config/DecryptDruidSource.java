package com.iscas.quickframe.config;

import com.alibaba.druid.filter.config.ConfigTools;
import com.alibaba.druid.pool.DruidDataSource;

/**
*@auhor:zhuquanwen
*@date:2016年12月1日
*@desc:定义一个druidDataSource子类，自动装配加密后的用户名密码
*/
@SuppressWarnings("serial")
public class DecryptDruidSource extends DruidDataSource{
	@Override
	public void setUsername(String username){
		try {
			this.username=ConfigTools.decrypt(username);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void setPassword(String password){
		try {
			this.password=ConfigTools.decrypt(password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
