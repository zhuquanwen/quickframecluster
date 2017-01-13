package com.iscas.quickframe.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
*@auhor:zhuquanwen
*@date:2016年12月15日
*@desc: Cas配置类
*		casPrefix：cas服务器地址
*		shiroPrefix:当前应用对外发布地址
*		casFilterUrlPattern：casFilter UrlPattern
*/
@Component
@ConfigurationProperties(prefix="casConfig")
public class CasConfig {
	public static CasConfig INSTANCE = null;
	private String casPrefix;
	private String shiroPrefix;
	private  String casFilterUrlPattern = "/shiro-cas";
	private String localLoginURI;
	private String appName;
	
	private Long shiroSessionTimeout;
	private Long sessionValidationInterval;
	private Long redisSessionStoreTime;
	private Long permissionCacheTime;
	public CasConfig(){
		if(INSTANCE != null){
			casPrefix = INSTANCE.getCasPrefix();
			shiroPrefix = INSTANCE.getShiroPrefix();
			localLoginURI = INSTANCE.getLocalLoginURI();
			appName = INSTANCE.getAppName();
			shiroSessionTimeout = INSTANCE.getShiroSessionTimeout();
			sessionValidationInterval = INSTANCE.getSessionValidationInterval();
			redisSessionStoreTime = INSTANCE.getRedisSessionStoreTime();
			permissionCacheTime = INSTANCE.getPermissionCacheTime();
		}
		
	}
	
	public CasConfig(String casPrefix, String shiroPrefix) {
		super();
		this.casPrefix = casPrefix;
		this.shiroPrefix = shiroPrefix;
	}
	
	public CasConfig(String casPrefix, String shiroPrefix, String localLoginURI) {
		super();
		this.casPrefix = casPrefix;
		this.shiroPrefix = shiroPrefix;
		this.localLoginURI = localLoginURI;
	}

	
	public CasConfig(String casPrefix, String shiroPrefix, String localLoginURI, String appName) {
		super();
		this.casPrefix = casPrefix;
		this.shiroPrefix = shiroPrefix;
		this.localLoginURI = localLoginURI;
		this.appName = appName;
	}
	
	public CasConfig(String casPrefix, String shiroPrefix,  String localLoginURI,
			String appName, Long shiroSessionTimeout, Long sessionValidationInterval, Long redisSessionStoreTime) {
		super();
		this.casPrefix = casPrefix;
		this.shiroPrefix = shiroPrefix;
		this.localLoginURI = localLoginURI;
		this.appName = appName;
		this.shiroSessionTimeout = shiroSessionTimeout;
		this.sessionValidationInterval = sessionValidationInterval;
		this.redisSessionStoreTime = redisSessionStoreTime;
	}

	public CasConfig(String casPrefix, String shiroPrefix,  String localLoginURI,
			String appName, Long shiroSessionTimeout, Long sessionValidationInterval, 
			Long redisSessionStoreTime, Long permissionCacheTime) {
		super();
		this.casPrefix = casPrefix;
		this.shiroPrefix = shiroPrefix;
		this.localLoginURI = localLoginURI;
		this.appName = appName;
		this.shiroSessionTimeout = shiroSessionTimeout;
		this.sessionValidationInterval = sessionValidationInterval;
		this.redisSessionStoreTime = redisSessionStoreTime;
		this.permissionCacheTime = permissionCacheTime;
	}
	
	public String getCasPrefix() {
		
		return casPrefix;
	}
	public void setCasPrefix(String casPrefix) {
		this.casPrefix = casPrefix;
	}
	public String getShiroPrefix() {
		return shiroPrefix;
	}
	public void setShiroPrefix(String shiroPrefix) {
		this.shiroPrefix = shiroPrefix;
	}
	public String getCasFilterUrlPattern() {
		return casFilterUrlPattern;
	}
	public void setCasFilterUrlPattern(String casFilterUrlPattern) {
		this.casFilterUrlPattern = casFilterUrlPattern;
	}

	public String getLocalLoginURI() {
		return localLoginURI;
	}

	public void setLocalLoginURI(String localLoginURI) {
		this.localLoginURI = localLoginURI;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Long getShiroSessionTimeout() {
		return shiroSessionTimeout;
	}

	public void setShiroSessionTimeout(Long shiroSessionTimeout) {
		this.shiroSessionTimeout = shiroSessionTimeout;
	}

	public Long getSessionValidationInterval() {
		return sessionValidationInterval;
	}

	public void setSessionValidationInterval(Long sessionValidationInterval) {
		this.sessionValidationInterval = sessionValidationInterval;
	}

	public Long getRedisSessionStoreTime() {
		return redisSessionStoreTime;
	}

	public void setRedisSessionStoreTime(Long redisSessionStoreTime) {
		this.redisSessionStoreTime = redisSessionStoreTime;
	}

	public Long getPermissionCacheTime() {
		return permissionCacheTime;
	}

	public void setPermissionCacheTime(Long permissionCacheTime) {
		this.permissionCacheTime = permissionCacheTime;
	}
	
}
