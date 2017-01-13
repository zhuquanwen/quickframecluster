package com.iscas.quickframe.config;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.crazycake.shiro.RedisCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import com.iscas.quickframe.domain.p.RequestDataDao;
import com.iscas.quickframe.domain.p.Role;
import com.iscas.quickframe.domain.p.UrlAddrDao;
import com.iscas.quickframe.domain.p.User;
import com.iscas.quickframe.service.UserService;
import com.iscas.quickframe.util.SpringRedisUtil;



/**
*@auhor:zhuquanwen
*@date:2016年12月9日
*@desc:自定义的一个在shiro最后的一个过滤器验证用户系统权限和请求数据权限
*/
public class MyShiroOtherCheckFilter extends  AuthorizationFilter {
	
	@Autowired
	private UserService userService;
	@Autowired
	private RequestDataDao requestDataDao;
	@Autowired
	private UrlAddrDao urlAddrDao;
	@Value("${appName}")
	private String appName; //从配置文件读取appName
	@Value("${server.context-path}") //从配置文件读取contextPath
	private String ctx;
	@Autowired
	@Qualifier("redisCacheManager")
    private RedisCacheManager redisCacheManager; 
	@Resource
	private MyShiroCasRealm myShiroCasRealm;
	private List<String> ignoreUrls = new ArrayList<String>();
	
	public static String CACHE_USER_KEY ="cache_user_key";
	public static String CACHE_USER_ROLE_KEY ="cache_user_role_key";
	public static String CACHE_USER_URLS_KEY ="CACHE_USER_URLS_KEY";
	public static String CACHE_USER_NO_APP_KEY = "CACHE_USER_NO_APP_KEY";
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		
		
		HttpServletRequest req = (HttpServletRequest)request;
		String url = req.getRequestURI();
		for (int i = 0; i < ignoreUrls.size(); i++) {
			if(url.equals(ctx+ignoreUrls.get(i))){
				return true;
			}
		}
		
		
		return this.checkUserAppPermission(req) && this.checkRequestData(req);
	}
	 //验证当前用户是否有该系统的权限
  	@SuppressWarnings("unchecked")
	private boolean checkUserAppPermission(HttpServletRequest request) throws Exception{
  		 Subject currentUser = SecurityUtils.getSubject();
  		 Object obj1 = currentUser.getPrincipal();
  		 if(obj1 != null){
  			 String username = obj1.toString();
  			// Cache<Object, Object> cache = redisCacheManager.getCache(myShiroCasRealm.getName() + ".authorizationCache");  
  			 User user =null;
//  			 if(cache.get(CACHE_USER_KEY+","+appName+","+username) != null){
//  				 //如果ehcache中有此缓存直接拿
//  				 Object obj   = cache.get(CACHE_USER_KEY+","+appName+","+username);
//  				 System.out.println(obj);   
//  				 System.out.println(obj instanceof com.iscas.quickframe.domain.p.User);
//  				 System.out.println();
//  				 user = (User)cache.get(CACHE_USER_KEY+","+appName+","+username);
//  			 }else{
//  				 //没有的话读取数据库
//  				user= userService.findByUsernameAndAppName(appName, username);
//  				if(user != null){
//  					cache.put(CACHE_USER_KEY+","+appName+","+username, user);
//  				}
//  			 }
  			 Object objUser = SpringRedisUtil.get(CACHE_USER_KEY+","+appName+","+username, User.class);
  			 if(objUser != null){
  				 //如果redis中有此缓存直接拿
  				
  				 user = (User)objUser;
  			 }else{
  				 //没有的话读取数据库
  				user= userService.findByUsernameAndAppName(appName, username);
  				//if(user != null){
  					SpringRedisUtil.save(CACHE_USER_KEY+","+appName+","+username, user, CasConfig.INSTANCE.getPermissionCacheTime());
  				//}
  			 }
  			  
  			 
  			if(user == null){
  				// 处理用户登录了，但是没有此APP权限，导致 URL没有加入权限管理，也不放行的问题
  				User userx = null;
//  				 if(cache.get(CACHE_USER_NO_APP_KEY+","+username) != null){
//  	  				 //如果ehcache中有此缓存直接拿
//  	  				 userx = (User)cache.get(CACHE_USER_NO_APP_KEY+","+username);
//  	  			 }else{
//  	  				 //没有的话读取数据库
//  	  				userx = userService.findByUsername(username);
//  	  				if(userx != null){
//  	  					cache.put(CACHE_USER_NO_APP_KEY+","+username, userx);
//  	  				}
//  	  			 }
  				Object objAppKey = SpringRedisUtil.get(CACHE_USER_NO_APP_KEY+","+username, User.class);
				 if(objAppKey != null){
					 //如果redis中有此缓存直接拿
					 userx = (User)objAppKey;
				 }else{
					 //没有的话读取数据库
					userx = userService.findByUsername(username);
					//if(userx != null){
						SpringRedisUtil.save(CACHE_USER_NO_APP_KEY+","+username, userx, CasConfig.INSTANCE.getPermissionCacheTime());
					//}
				 }
 				if(userx != null){
 					//读取用户已经配置的urls
 					List<String> urls = null;
// 					 if(cache.get(CACHE_USER_URLS_KEY) != null){
// 	  	  				 //如果ehcache中有此缓存直接拿
// 						urls = (List<String>)cache.get(CACHE_USER_URLS_KEY);
// 	  	  			 }else{
// 	  	  				 //没有的话读取数据库
// 	  	  				 List<UrlAddr> uas = urlAddrDao.findByAppName(CasConfig.INSTANCE.getAppName());
// 	  	  				 
// 	  	  				 if(uas != null){
// 	  	  					 urls = new ArrayList<String>();
// 	  	  					 String ctxx = "";
// 	  	  					 if(ctx != null && !"".equals(ctx)){
// 	  	  						 ctxx = ctx.substring(0, ctx.lastIndexOf("/"));
// 	  	  					 }
// 	  	  					for (int i = 0; i < uas.size(); i++) {
//								urls.add(ctxx + uas.get(i).getUrl());
//							} 
// 	  	  					if(urls != null && urls.size() >0){
// 	  	  						cache.put(CACHE_USER_URLS_KEY,urls);
// 	  	  					}
// 	  	  					
// 	  	  				}
// 	  	  			 }
 					Object objUrlKey = SpringRedisUtil.get(CACHE_USER_URLS_KEY+","+CasConfig.INSTANCE.getAppName(), List.class);
 					if(objUrlKey != null){
	  	  				 //如果redis中有此缓存直接拿
						urls = (List<String>)objUrlKey;
	  	  			 }else{
	  	  				 //没有的话读取数据库
	  	  				 
	  	  				 List<String> uas = urlAddrDao.findUrlByAppName(CasConfig.INSTANCE.getAppName());
	  	  				 
	  	  				 
	  	  				 
	  	  				 if(uas != null){
	  	  					 urls = new ArrayList<String>();
	  	  					 String ctxx = "";
	  	  					 if(ctx != null && !"".equals(ctx)){
	  	  						 ctxx = ctx.substring(0, ctx.lastIndexOf("/"));
	  	  					 }
	  	  					for (int i = 0; i < uas.size(); i++) {
								urls.add(ctxx + uas.get(i));
							} 
	  	  					//if(urls != null && urls.size() >0){
	  	  						SpringRedisUtil.save(CACHE_USER_URLS_KEY+","+CasConfig.INSTANCE.getAppName(), urls, CasConfig.INSTANCE.getPermissionCacheTime());
	  	  					//}
	  	  					
	  	  				}
	  	  			 }
 					if(urls != null){
 						String uri = request.getRequestURI();
 						//遍历urls,如果此uri已经加入权限列表了，
 						for (String urlx : urls) {
							String url = urlx;
							if(url.endsWith("/*")){
								url = url.substring(0, url.lastIndexOf("/*"));
								if(uri.startsWith(url)){
									return false;
								}
							}else if(url.endsWith("/**")){
								url = url.substring(0, url.lastIndexOf("/**"));
								if(uri.startsWith(url)){
									return false;
								}
							}else{
								if(uri.equals(url)){
									return false;
								}
							}
						}
 						return true;
 					}
 				}
 				
 				 
  				 return false;
  			 }
  		 }
  		 return true;
  	}
      
      //验证请求数据的权限
      @SuppressWarnings("unchecked")
	private boolean checkRequestData(HttpServletRequest request ) throws Exception{
   	   boolean flag = true;
      	Object obj = request.getParameter("dataType");
   	   String dataType = null;
   	   if(obj != null){
   		   Subject currentUser = SecurityUtils.getSubject();
   		   Object obj1 = currentUser.getPrincipal();
   		   if(obj1 != null){
   			  flag = false;
   			   dataType = obj.toString();
   			   String username = obj1.toString();
   			   //Cache<Object, Object> cache = redisCacheManager.getCache(myShiroCasRealm.getName() + ".authorizationCache");  
   			   User user =null;
//   			   if(cache.get(CACHE_USER_KEY+","+appName+","+username) != null){
//   				   //如果ehcache中有此缓存直接拿
//   				   user = (User)cache.get(CACHE_USER_KEY+","+appName+","+username);
//   			   }else{
//   				   //没有的话读取数据库
//   				   user= userService.findByUsernameAndAppName(appName, username);
//   				   if(user != null){
//   					   cache.put(CACHE_USER_KEY+","+appName+","+username, user);
//   				   }
//   			   }
   			 Object objUser = SpringRedisUtil.get(CACHE_USER_KEY+","+appName+","+username, User.class);
  			 if(objUser != null){
  				 //如果redis中有此缓存直接拿
  				
  				 user = (User)objUser;
  			 }else{
  				 //没有的话读取数据库
  				user= userService.findByUsernameAndAppName(appName, username);
  				//if(user != null){
  					SpringRedisUtil.save(CACHE_USER_KEY+","+appName+","+username, user, CasConfig.INSTANCE.getPermissionCacheTime());
  				//}
  			 }
   			   
   			  if(user != null){
   				 List<Role> roleList = user.getRoleList();
     			   if(roleList != null){
     				   for (Role role : roleList) {
     					   List<String> rds =null;
//  	   	   			   if(cache.get(CACHE_USER_ROLE_KEY+role.getId()) != null){
//  	   	   				   //如果ehcache中有此缓存直接拿
//  	   	   				   rds = (List<String>)cache.get(CACHE_USER_ROLE_KEY+role.getId());
//  	   	   			   }else{
//  	   	   				   //没有的话读取数据库
//  	   	   				   rds= requestDataDao.findByRoleId(role.getId());
//  	   	   				   if(rds != null){
//  	   	   					   cache.put(CACHE_USER_ROLE_KEY+role.getId(), rds);
//  	   	   				   }
//  	   	   			   }
     					   Object roleKeyObj = SpringRedisUtil.get(CACHE_USER_ROLE_KEY+role.getId(), List.class);
     					   if(roleKeyObj  != null){
     						   //如果redis中有直接拿						   
     						   rds = (List<String>)roleKeyObj;
     					   }else{
     						   rds= requestDataDao.findByRoleId(role.getId());
     						   SpringRedisUtil.save(CACHE_USER_ROLE_KEY+role.getId(), rds, CasConfig.INSTANCE.getPermissionCacheTime());
     					   }
     					   if(rds != null){
     						   for (String type : rds) {
     							   if(dataType.equals(type)){
     								   return true;
     							   }
     						   }
     					   }
     				   }
     			   }
   			  }
   			  
       		   if(!flag){
//       			   RequestDispatcher rd = request.getRequestDispatcher("/403");
//       			   rd.forward(request, response);
       			   return false;
       		   }else{
       			   return true;
       		   }
   		   }
   		 
   	   }
   	   return true;
      }
	public List<String> getIgnoreUrls() {
		return ignoreUrls;
	}
	public void setIgnoreUrls(List<String> ignoreUrls) {
		this.ignoreUrls = ignoreUrls;
	}
	
      
      
}
