package com.iscas.quickframe.aop;



import javax.annotation.Resource;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.iscas.quickframe.config.MyShiroCasRealm;
import com.iscas.quickframe.helper.ShiroAuthorizationHelper;

/**
 * 刷新权限AOP
 * */
@Aspect
@Component
public class RefreshPermissionAop {
	@Resource
	private MyShiroCasRealm myShiroCasRealm;
	@Resource
	private ShiroAuthorizationHelper shiroAuthorizationHelper;
	@Pointcut(("@annotation(com.iscas.quickframe.aop.RefreshPemission)"))
	public void refresh(){
		
	}
	@After("refresh()")
    public void afterExec(){  
		//刷新用户所有用户的权限
		shiroAuthorizationHelper.clearAuthorizationInfo();
    }
	
}
