package com.iscas.quickframe.helper;
/**
*@auhor:zhuquanwen
*@date:2016年12月9日
*@desc:
*/


import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.shiro.subject.SimplePrincipalCollection;
import org.crazycake.shiro.SerializeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iscas.quickframe.config.CasConfig;
import com.iscas.quickframe.config.MyShiroCasRealm;
import com.iscas.quickframe.config.MyShiroOtherCheckFilter;
import com.iscas.quickframe.config.redis.MyRedisManager;
import com.iscas.quickframe.domain.p.Role;
import com.iscas.quickframe.domain.p.User;
import com.iscas.quickframe.service.UserService;
import com.iscas.quickframe.util.SpringRedisUtil;

  

  
/**
*@auhor:zhuquanwen
*@date:2016年12月5日
*@desc:权限清除帮助类
*/  
@Component
public class ShiroAuthorizationHelper {  
  
    /** 
     *  
     */  
//	@Autowired
//	@Qualifier("redisCacheManager")
//    private RedisCacheManager redisCacheManager; 
	@Resource
	private MyShiroCasRealm myShiroCasRealm;
	@Autowired
	private UserService userService;
      
    private static Logger log = LoggerFactory.getLogger(ShiroAuthorizationHelper.class);  
      
    
    /** 
     * 清除所有用户的授权信息 
     * 
     */  
    public  void clearAuthorizationInfo(){  
//        Cache<Object, Object> cache = redisCacheManager.getCache(myShiroCasRealm.getName() + ".authorizationCache");  
//        cache.clear();
    	//清除otherCheck中缓存的信息
        Set<byte[]> set1 = SpringRedisUtil.keys(MyShiroOtherCheckFilter.CACHE_USER_KEY + "*");
        Set<byte[]> set2 = SpringRedisUtil.keys(MyShiroOtherCheckFilter.CACHE_USER_NO_APP_KEY + "*");
        Set<byte[]> set3 = SpringRedisUtil.keys(MyShiroOtherCheckFilter.CACHE_USER_ROLE_KEY + "*");
        Set<byte[]> set4 = SpringRedisUtil.keys(MyShiroOtherCheckFilter.CACHE_USER_URLS_KEY + "*");
    	
        
        if(set1 != null && set1.size() > 0){
    		 for (byte[] bs : set1) {
    			SpringRedisUtil.deleteBytes(bs);
    		 }
    	}
    	if(set2 != null && set2.size() > 0){
	   		 for (byte[] bs : set2) {
	   			SpringRedisUtil.deleteBytes(bs);
	   		 }
    	}
    	if(set3 != null && set3.size() > 0){
	   		 for (byte[] bs : set3) {
	   			SpringRedisUtil.deleteBytes(bs);
	   		 }
    	}
    	if(set4 != null && set4.size() > 0){
	   		 for (byte[] bs : set4) {
	   			SpringRedisUtil.deleteBytes(bs);
	   		 }
    	}
    	//清除用户权限信息
    	Set<byte[]> set5 = SpringRedisUtil.keys("*org.apache.shiro.subject.SimplePrincipalCollection*");
    	if(set5 != null && set5.size() > 0){
	   		 for (byte[] bs : set5) {
	   			SpringRedisUtil.deleteBytes(bs);
	   	}
   	}
    } 
      
    /** 
     * 清除用户的授权信息 
     * @param username 
     */  
    public  void clearAuthorizationInfo(String username){  
        if(log.isDebugEnabled()){  
            log.debug("clear the " + username + " authorizationInfo");  
        }  
       
        String key1 = MyRedisManager.SimplePrincipalCollection_username +
				"org.apache.shiro.subject.SimplePrincipalCollection" +username;
        SimplePrincipalCollection spc = SpringRedisUtil.get(key1, SimplePrincipalCollection.class);
		SpringRedisUtil.delete(key1);	
		SpringRedisUtil.deleteBytes(SerializeUtils.serialize(spc));
        //清除自定义检查的一些缓存
        String pKey1 = MyShiroOtherCheckFilter.CACHE_USER_KEY+","+CasConfig.INSTANCE.getAppName()+","+username;
        String pKey2 = MyShiroOtherCheckFilter.CACHE_USER_URLS_KEY+","+CasConfig.INSTANCE.getAppName();
        String pKey3 = MyShiroOtherCheckFilter.CACHE_USER_NO_APP_KEY+","+username;
        //清除自定义的角色缓存
        User user = null;
        Object objUser = SpringRedisUtil.get(MyShiroOtherCheckFilter.CACHE_USER_KEY+","+CasConfig.INSTANCE.getAppName()+","+username, User.class);
		if(objUser != null){
			user = (User)objUser;
		}else{
			//没有的话读取数据库
			user= userService.findByUsernameAndAppName(CasConfig.INSTANCE.getAppName(), username);
		}
		if(user != null){
			 List<Role> roleList = user.getRoleList();
			 if(roleList != null){
				   for (int i = 0; i < roleList.size(); i++) {
					   SpringRedisUtil.delete(MyShiroOtherCheckFilter.CACHE_USER_ROLE_KEY+roleList.get(i).getId());
				   }
			 }
		}
        SpringRedisUtil.delete(pKey1);
        SpringRedisUtil.delete(pKey2);
        SpringRedisUtil.delete(pKey3);
    }  
      
}  