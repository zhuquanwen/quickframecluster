package com.iscas.quickframe.config;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
/**
*@auhor:zhuquanwen
*@date:2016年12月1日
*@desc:应用数据库连接配置
*/
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef="entityManagerFactorySecond",
        transactionManagerRef="transactionManagerSecond",
        basePackages= { "${entityLocation}" }) //设置Repository所在位置
public class SecondConfig {
	@Autowired
	@Qualifier("appDataSource")
	private DataSource appDataSource;
	
	@Value("${entityLocation}")
	private String entityLocation;
	 @Bean(name = "entityManagerSecond")
	 public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
	     return entityManagerFactorySecond(builder).getObject().createEntityManager();
	 }
	 
	 @Bean(name = "entityManagerFactorySecond")
	 public LocalContainerEntityManagerFactoryBean entityManagerFactorySecond (EntityManagerFactoryBuilder builder) {
	     return builder
	             .dataSource(appDataSource)
	             .properties(getVendorProperties(appDataSource))
	             .packages(entityLocation) //设置实体类所在位置
	             .persistenceUnit("secondPersistenceUnit")
	             .build();
	 }
	 @Autowired
	 private JpaProperties jpaProperties;

	 private Map<String, String> getVendorProperties(DataSource dataSource) {
	     return jpaProperties.getHibernateProperties(dataSource);
	 }
	 
	 
	 @Bean(name = "transactionManagerSecond")
	 public PlatformTransactionManager transactionManagerSecond(EntityManagerFactoryBuilder builder) {
	     return new JpaTransactionManager(entityManagerFactorySecond(builder).getObject());
	 }
}
