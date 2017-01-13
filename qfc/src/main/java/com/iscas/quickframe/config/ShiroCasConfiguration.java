package com.iscas.quickframe.config;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cas.CasFilter;
import org.apache.shiro.cas.CasSubjectFactory;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.iscas.quickframe.config.redis.MyRedisManager;
import com.iscas.quickframe.config.redis.RedisCacheSessionDao;
import com.iscas.quickframe.domain.p.UrlAddr;
import com.iscas.quickframe.service.UrlAddrService;


/**
*@auhor:zhuquanwen
*@date:2016年12月1日
*@desc:shiro cas 客户端配置
*/
@SuppressWarnings("deprecation")
@Configuration
@EnableConfigurationProperties(CasConfig.class)
public class ShiroCasConfiguration {
	
	
    //private  String casFilterUrlPattern = "/shiro-cas";
	 private static Logger log = LoggerFactory.getLogger(ShiroCasConfiguration.class);  
    //权限信息缓存
//    @Bean(name="ehCacheManager")
//    @Qualifier("ehCacheManager")
//    public EhCacheManager getEhCacheManager() {  
//    	if(log.isInfoEnabled()){
//    		log.info("-----注册权限缓存-------");
//    	}
//        EhCacheManager em = new EhCacheManager();  
//        em.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");  
//        return em;  
//    }  
	 

    @Bean
    public RedisManager getRedisManager(){
    	//这里暂时注册了一个redisManager,应该与springboot注册的不同，处理权限缓存的
    	if(log.isInfoEnabled()){
    		log.info("-----注册redisManager-------");
    	}
    	
    	RedisManager rm = new MyRedisManager();
    	
    	rm.setExpire(CasConfig.INSTANCE.getPermissionCacheTime().intValue()*60);
    	return rm;
    }
	 
	 
    @Bean(name="redisCacheManager")
    @Qualifier("redisCacheManager")
    public RedisCacheManager getRedisCacheManager(){
    	if(log.isInfoEnabled()){
    		log.info("-----注册redis权限缓存-------");
    	}
    	RedisCacheManager redisCacheManager = new RedisCacheManager();
    	redisCacheManager.setRedisManager(getRedisManager());
    	redisCacheManager.setKeyPrefix("ShiroCasRealm.authorizationCache");
    	return redisCacheManager;
    }
    //权限认证
    @Bean(name = "myShiroCasRealm")
    public MyShiroCasRealm myShiroCasRealm(/*RedisCacheManager cacheManager*/) {  
    	if(log.isInfoEnabled()){
    		log.info("-----注册权限认证-------");
    	}
        MyShiroCasRealm realm = new MyShiroCasRealm(); 
        realm.setCacheManager(getRedisCacheManager());
        
        return realm;
    } 
  
    //过滤器注册
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        //  该值缺省为false,表示生命周期由SpringApplicationContext管理,设置为true则表示由ServletContainer管理  
        filterRegistration.addInitParameter("targetFilterLifecycle", "true");
        filterRegistration.setEnabled(true);
        filterRegistration.addUrlPatterns("/*");
        return filterRegistration;
    }

    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);
        return daap;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(/*MyShiroCasRealm myShiroCasRealm*/) {
    	if(log.isInfoEnabled()){
    		log.info("---注册securityManager---");
    	}
    	DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();
        dwsm.setRealm(myShiroCasRealm());
//      <!-- 用户授权/认证信息Cache, 采用redis 缓存 --> 
        dwsm.setCacheManager(getRedisCacheManager());
        // 指定 SubjectFactory
        dwsm.setSubjectFactory(new CasSubjectFactory());
        // 设置session管理器
        // redis管理session，有问题暂时先不使用
        dwsm.setSessionManager(defaultWebSessionManager());
        SecurityUtils.setSecurityManager(dwsm);
        return dwsm;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(/*DefaultWebSecurityManager securityManager*/) {
        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
        aasa.setSecurityManager(getDefaultWebSecurityManager());
        return aasa;
    }



    private void loadShiroFilterChain(ShiroFilterFactoryBean shiroFilterFactoryBean,UrlAddrService urlAddrService){
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        
        filterChainDefinitionMap.put("/shiro-cas", "casFilter");// shiro集成cas后，首先添加该规则
       
        // authc：该过滤器下的页面必须验证后才能访问，它是Shiro内置的一个拦截器org.apache.shiro.web.filter.authc.FormAuthenticationFilter
        // anon：它对应的过滤器里面是空的,什么都没做
        List<UrlAddr>  uas = urlAddrService.findByAppName(CasConfig.INSTANCE.getAppName());
        for (UrlAddr ua : uas) {
        	if(ua.getResourcex() != null){
        		String perm = "authc,perms[" + ua.getResourcex().getPermissionname() + "]";
            	filterChainDefinitionMap.put(ua.getUrl(),perm);
        	}
        	
		}
        //filterChainDefinitionMap.put("/user/edit/**", "authc,perms[user:edit]");// 这里为了测试，固定写死的值，也可以从数据库或其他配置中读取
        filterChainDefinitionMap.put("/user", "authc");
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/toLogin", "anon");
        filterChainDefinitionMap.put("/logout", "anon");
        filterChainDefinitionMap.put("/toLogout", "anon");
        filterChainDefinitionMap.put("/**", "anon");//anon 可以理解为不拦截
       
       //filterChainDefinitionMap.put("/**", "authc");
        
        filterChainDefinitionMap.put("/**", "myShiroOtherCheckFilter");//添加自定义的其他过滤规则
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        
    }
    
    @Bean
    public MySingleSignOutFilter singleSignOutFilter(){
    	return new MySingleSignOutFilter();
    }
   
    //注册单点登录退出的过滤器 让其优先启动
    @Bean
    @Order(1)
    public FilterRegistrationBean singleSignOutFilterRegistrationBean(){
    	if(log.isInfoEnabled()){
    		log.info("-----注册单点登出过滤器-------");
    	}
    	FilterRegistrationBean frb = new FilterRegistrationBean();
    	frb.setOrder(1);
    	frb.setFilter(singleSignOutFilter());
    	frb.addUrlPatterns("/*");
    	frb.setName("singleSignOutFilter");
    	return frb;
    }
    //注册监听
    @Bean
	public ServletListenerRegistrationBean<SingleSignOutHttpSessionListener>  servletListenerRegistrationBean () {
    	if(log.isInfoEnabled()){
    		log.info("-----注册单点登出监听-------");
    	}
		ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> registration = 
			new ServletListenerRegistrationBean<SingleSignOutHttpSessionListener>(new SingleSignOutHttpSessionListener());
		return registration;
	}
//    //注册监听
//    @Bean
//	public ServletListenerRegistrationBean<SessionListener>  servletListenerRegistrationBean1 () {
//    	if(log.isInfoEnabled()){
//    		log.info("-----注册Sessio监听-------");
//    	}
//		ServletListenerRegistrationBean<SessionListener> registration = 
//			new ServletListenerRegistrationBean<SessionListener>(new SessionListener());
//		return registration;
//	}
    
    /**
     * CAS过滤器
     */
    @Bean(name = "casFilter")
    public CasFilter getCasFilter() {
    	if(log.isInfoEnabled()){
    		log.info("-----注册cas过滤器-------");
    	}
        CasFilter casFilter = new MyCasFilter();
        casFilter.setName("casFilter");
        casFilter.setEnabled(true);
        
        String loginUrl = CasConfig.INSTANCE.getShiroPrefix() + CasConfig.INSTANCE.getLocalLoginURI() +
        		"?service=" + CasConfig.INSTANCE.getShiroPrefix() + CasConfig.INSTANCE.getCasFilterUrlPattern();
        casFilter.setFailureUrl(loginUrl);// 我们选择认证失败后再打开登录页面
        return casFilter;
    }
    
   
    //配置自定义的过滤器
    @Bean(name = "myShiroOtherCheckFilter")
    public MyShiroOtherCheckFilter getMyShiroOtherCheckFilter() {
    	if(log.isInfoEnabled()){
    		log.info("-----注册自定义的其他权限检测过滤器-------");
    	}
    	MyShiroOtherCheckFilter filter = new MyShiroOtherCheckFilter();
    	List<String> ignoreUrls =filter.getIgnoreUrls();
    	ignoreUrls.add("login");
    	ignoreUrls.add("toLogin");
    	ignoreUrls.add("403");
    	ignoreUrls.add("logout");
    	ignoreUrls.add("toLogout");
        return filter;
    }

   
    //shiro过滤器
    @Bean(name = "shiroFilter")
    @Order(2)
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(/*DefaultWebSecurityManager securityManager,*/ /*CasFilter casFilter,*/ UrlAddrService urlAddrService) {
    	if(log.isInfoEnabled()){
    		log.info("-----注册shiro过滤器-------");
    	}
    	ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 必须设置 SecurityManager  
        shiroFilterFactoryBean.setSecurityManager(getDefaultWebSecurityManager());
        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        String loginUrl =  CasConfig.INSTANCE.getCasPrefix() + "/login" + 
        		"?service=" + CasConfig.INSTANCE.getShiroPrefix() + CasConfig.INSTANCE.getCasFilterUrlPattern();
        shiroFilterFactoryBean.setLoginUrl(loginUrl);
//        // 登录成功后要跳转的连接
        shiroFilterFactoryBean.setSuccessUrl("/user");
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        // 添加casFilter到shiroFilter中   
        Map<String, Filter> filters = new LinkedHashMap<>();
       
        filters.put("casFilter", getCasFilter());
       
		
        filters.put("myShiroOtherCheckFilter", getMyShiroOtherCheckFilter());
        shiroFilterFactoryBean.setFilters(filters);
        
        loadShiroFilterChain(shiroFilterFactoryBean, urlAddrService);
        return shiroFilterFactoryBean;
    }
  //注册sessionId
    @Bean
    public SessionIdGenerator sessionIdGenerator(){
    	if(log.isInfoEnabled()){
    		log.info("-----注册sessionId生成器-------");
    	}
    	return new JavaUuidSessionIdGenerator();
    }
    //注册 RedisCacheSessionDao
    @Bean
    public RedisCacheSessionDao redisCacheSessionDao(){
    	if(log.isInfoEnabled()){
    		log.info("-----注册RedisCacheSessionDao-------");
    	}
    	RedisCacheSessionDao rcsd = new RedisCacheSessionDao();
    	rcsd.setActiveSessionsCacheName("shiro-activeSessionCache");
    	rcsd.setSessionIdGenerator(sessionIdGenerator());
    	rcsd.setTimeToLive(CasConfig.INSTANCE.getRedisSessionStoreTime().intValue());
    	return rcsd;
    }
    //注册 Quartzsession校验
//    @Bean
//    public QuartzSessionValidationScheduler sessionValidationScheduler(){
//    	if(log.isInfoEnabled()){
//    		log.info("-----注册QuartzSession校验-------");
//    	}
//    	QuartzSessionValidationScheduler svs = new QuartzSessionValidationScheduler();
//    	svs.setSessionValidationInterval(30000);//session校验时间间隔 先写在这，后面做到配置文件里
////    	svs.setSessionManager(defaultWebSessionManager());
//    	return svs;
//    }
    //定义session管理器
    @Bean
    public DefaultWebSessionManager defaultWebSessionManager(){
    	if(log.isInfoEnabled()){
    		log.info("-----注册session管理器-------");
    	}
    	DefaultWebSessionManager dwsm = new DefaultWebSessionManager();
    	dwsm.setGlobalSessionTimeout(CasConfig.INSTANCE.getShiroSessionTimeout()*60*1000); //session超时时间
    	dwsm.setDeleteInvalidSessions(true); //删除没用的session
    	dwsm.setSessionValidationSchedulerEnabled(true); //启用session定义检查
    	//dwsm.setSessionValidationScheduler(sessionValidationScheduler());
    	dwsm.setSessionValidationInterval(CasConfig.INSTANCE.getSessionValidationInterval()*60*1000);
    	dwsm.setSessionIdCookieEnabled(true);
    	dwsm.setSessionDAO(redisCacheSessionDao());
    	dwsm.setSessionIdCookie(simpleCookie());
    	return dwsm;
    }
    //会话cookie模板
	@Bean
	public SimpleCookie simpleCookie(){
		if(log.isInfoEnabled()){
    		log.info("-----注册会话cookie模板-------");
    	}
		SimpleCookie sc = new SimpleCookie("pid");
		sc.setHttpOnly(true);
		sc.setMaxAge(-1);
		sc.setPath("/");
		return sc;
	}
}