package com.iscas.quickframe.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.alibaba.druid.pool.DruidDataSource;
/**
*@auhor:zhuquanwen
*@date:2016年12月1日
*@desc:使用druid连接数据库
*/
@Configuration
public class DataSourceConfig {
	@Value("${spring.datasource.permission.url}")  
	private String url; 
	@Value("${spring.datasource.permission.driver-class-name}")  
	private String driver_class_name;  
	@Value("${spring.datasource.permission.username}")  
	private String username;  
	@Value("${spring.datasource.permission.password}")  
	private String password;  
	@Value("${spring.datasource.permission.maxActive}")  
	private Integer maxActive;  
	@Value("${spring.datasource.permission.initialSize}")  
	private Integer initialSize;  
	@Value("${spring.datasource.permission.maxWait}")  
	private Long maxWait;  
	@Value("${spring.datasource.permission.minIdle}")  
	private Integer minIdle;  
	@Value("${spring.datasource.permission.timeBetweenEvictionRunsMillis}")  
	private Long timeBetweenEvictionRunsMillis;  
	@Value("${spring.datasource.permission.minEvictableIdleTimeMillis}")  
	private Long minEvictableIdleTimeMillis;  
	@Value("${spring.datasource.permission.validationQuery}")  
	private String validationQuery; 
	@Value("${spring.datasource.permission.testWhileIdle}")  
	private Boolean testWhileIdle; 
	@Value("${spring.datasource.permission.testOnBorrow}")  
	private Boolean testOnBorrow; 
	@Value("${spring.datasource.permission.testOnReturn}")  
	private Boolean testOnReturn; 
	@Value("${spring.datasource.permission.poolPreparedStatements}")  
	private Boolean poolPreparedStatements; 
	@Value("${spring.datasource.permission.maxOpenPreparedStatements}")  
	private Integer maxOpenPreparedStatements; 
	@Value("${spring.datasource.permission.maxPoolPreparedStatementPerConnectionSize}")  
	private Integer maxPoolPreparedStatementPerConnectionSize; 
	@Value("${spring.datasource.permission.filters}")  
	private String filters; 
	@Value("${spring.datasource.permission.connectionProperties}")  
	private String connectionProperties; 
	@Value("${spring.datasource.permission.useGlobalDataSourceStat}")  
	private Boolean useGlobalDataSourceStat; 
	
	@Value("${spring.datasource.app.url}")  
	private String url1; 
	@Value("${spring.datasource.app.driver-class-name}")  
	private String driver_class_name1;  
	@Value("${spring.datasource.app.username}")  
	private String username1;  
	@Value("${spring.datasource.app.password}")  
	private String password1;  
	@Value("${spring.datasource.app.maxActive}")  
	private Integer maxActive1;  
	@Value("${spring.datasource.app.initialSize}")  
	private Integer initialSize1;  
	@Value("${spring.datasource.app.maxWait}")  
	private Long maxWait1;  
	@Value("${spring.datasource.app.minIdle}")  
	private Integer minIdle1;  
	@Value("${spring.datasource.app.timeBetweenEvictionRunsMillis}")  
	private Long timeBetweenEvictionRunsMillis1;  
	@Value("${spring.datasource.app.minEvictableIdleTimeMillis}")  
	private Long minEvictableIdleTimeMillis1;  
	@Value("${spring.datasource.app.validationQuery}")  
	private String validationQuery1; 
	@Value("${spring.datasource.app.testWhileIdle}")  
	private Boolean testWhileIdle1; 
	@Value("${spring.datasource.app.testOnBorrow}")  
	private Boolean testOnBorrow1; 
	@Value("${spring.datasource.app.testOnReturn}")  
	private Boolean testOnReturn1; 
	@Value("${spring.datasource.app.poolPreparedStatements}")  
	private Boolean poolPreparedStatements1; 
	@Value("${spring.datasource.app.maxOpenPreparedStatements}")  
	private Integer maxOpenPreparedStatements1; 
	@Value("${spring.datasource.app.maxPoolPreparedStatementPerConnectionSize}")  
	private Integer maxPoolPreparedStatementPerConnectionSize1; 
	@Value("${spring.datasource.app.filters}")  
	private String filters1; 
	@Value("${spring.datasource.app.connectionProperties}")  
	private String connectionProperties1; 
	@Value("${spring.datasource.app.useGlobalDataSourceStat}")  
	private Boolean useGlobalDataSourceStat1; 
	
	@Value("${casConfig.casPrefix}")
    private String casPrefix;
    @Value("${casConfig.shiroPrefix}")
    private String shiroPrefix ;
    @Value("${casConfig.localLoginURI}")
    private String localLoginURI ;
    @Value("${casConfig.shiroSessionTimeout}")
    private Long shiroSessionTimeout ;
    @Value("${casConfig.sessionValidationInterval}")
    private Long sessionValidationInterval ;
    @Value("${casConfig.redisSessionStoreTime}")
    private Long redisSessionStoreTime ;
    @Value("${casConfig.permissionCacheTime}")
    private Long permissionCacheTime ;
    
    @Value("${appName}")
    private String appName ;
	
	@Bean(name="permissionDataSource")
	@Qualifier("permissionDataSource")
	@Primary
	@ConfigurationProperties(prefix="spring.datasource.permission")
	public DataSource primaryDataSource() throws Exception{
		
		//因为ShiroCasConfiguration类无法autowired到配置类CasConfig
		//下面的代码将强制初始化CasConfig的静态变量INSTANCE 供给ShiroCasConfiguration使用
		//配置文件内的Cas配置(可能为加载顺序问题，暂时先这样解决)
		if(CasConfig.INSTANCE == null){
    		CasConfig.INSTANCE = new CasConfig(casPrefix,shiroPrefix,localLoginURI,appName,
    				shiroSessionTimeout,sessionValidationInterval,redisSessionStoreTime,
    				permissionCacheTime*60);
    		
    	}
		CasConfig.INSTANCE.setCasPrefix(casPrefix);
		CasConfig.INSTANCE.setShiroPrefix(shiroPrefix);
		CasConfig.INSTANCE.setLocalLoginURI(localLoginURI);
		CasConfig.INSTANCE.setAppName(appName);
		CasConfig.INSTANCE.setShiroSessionTimeout(shiroSessionTimeout);
		CasConfig.INSTANCE.setSessionValidationInterval(sessionValidationInterval);
		CasConfig.INSTANCE.setRedisSessionStoreTime(redisSessionStoreTime);
		CasConfig.INSTANCE.setPermissionCacheTime(permissionCacheTime*60);
		
		/*DataSource ds= DataSourceBuilder.create().build();
		return ds;*/
		//上面使用默认的连接池，下面修改为使用druid
		DruidDataSource dds=new DecryptDruidSource();
		dds.setUrl(url);
		dds.setDriverClassName(driver_class_name);
		//使用加密的用户名密码
		dds.setUsername(username);
		dds.setPassword(password);
		dds.setMaxActive(maxActive);
		dds.setInitialSize(initialSize);
		dds.setMaxWait(maxWait);
		dds.setMinIdle(minIdle);
		dds.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		dds.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		dds.setValidationQuery(validationQuery);
		dds.setTestWhileIdle(testWhileIdle);
		dds.setTestOnBorrow(testOnBorrow);
		dds.setTestOnReturn(testOnReturn);
		dds.setPoolPreparedStatements(poolPreparedStatements);
		dds.setMaxOpenPreparedStatements(maxOpenPreparedStatements);
		dds.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
		dds.setFilters(filters);
		dds.setConnectionProperties(connectionProperties);
		dds.setUseGlobalDataSourceStat(useGlobalDataSourceStat);
		return dds;
	}
	
	@Bean(name="appDataSource")
	@Qualifier("appDataSource")
	@ConfigurationProperties(prefix="spring.datasource.app")
	public DataSource secondDataSource() throws Exception{
		DruidDataSource dds=new DecryptDruidSource();
		dds.setUrl(url1);
		dds.setDriverClassName(driver_class_name1);
		//使用加密的用户名密码
		dds.setUsername(username1);
		dds.setPassword(password1);
		dds.setMaxActive(maxActive1);
		dds.setInitialSize(initialSize1);
		dds.setMaxWait(maxWait1);
		dds.setMinIdle(minIdle1);
		dds.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis1);
		dds.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis1);
		dds.setValidationQuery(validationQuery1);
		dds.setTestWhileIdle(testWhileIdle1);
		dds.setTestOnBorrow(testOnBorrow1);
		dds.setTestOnReturn(testOnReturn1);
		dds.setPoolPreparedStatements(poolPreparedStatements1);
		dds.setMaxOpenPreparedStatements(maxOpenPreparedStatements1);
		dds.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize1);
		dds.setFilters(filters1);
		dds.setConnectionProperties(connectionProperties1);
		dds.setUseGlobalDataSourceStat(useGlobalDataSourceStat1);
		return dds;
	}
}
