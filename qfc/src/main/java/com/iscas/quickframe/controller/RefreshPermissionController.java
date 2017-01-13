package com.iscas.quickframe.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iscas.quickframe.aop.RefreshPemission;
import com.iscas.quickframe.helper.ShiroAuthorizationHelper;

/**
*@auhor:zhuquanwen
*@date:2016年12月15日
*@desc:刷新权限控制器
*/
@Controller
public class RefreshPermissionController {
	@Autowired
	private ShiroAuthorizationHelper shiroAuthorizationHelper;
	/**
	 * 删除所有权限缓存
	 * */
	 @RequestMapping(value="/refreshPermission")
	 @ResponseBody
	 @RefreshPemission
	 public Map<String,Boolean> refreshPermission(){
		 Map<String,Boolean> map = new HashMap<String,Boolean>();
		 map.put("status", true);
		 return map;
	 }
	 /**
	  * 删除对应用户的缓存
	  * */
	 @RequestMapping(value="/refreshPermissionByUser")
	 @ResponseBody
	 public Map<String,Boolean> refreshPermissionByUser(String users){
		 Map<String,Boolean> map = new HashMap<String,Boolean>();
		 map.put("status", true);
		 if(users != null){
			 String[] userStrs = users.split(";");
			 for (String username : userStrs) {
				 shiroAuthorizationHelper.clearAuthorizationInfo(username);
			}
		 }
		 return map;
		
		 
	 }
}
