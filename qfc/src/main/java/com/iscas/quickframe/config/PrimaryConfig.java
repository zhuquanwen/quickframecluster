package com.iscas.quickframe.config;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
/**
*@auhor:zhuquanwen
*@date:2016年12月1日
*@desc:权限认证数据库配置
*/
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef="entityManagerFactoryPrimary",
        transactionManagerRef="transactionManagerPrimary",
        basePackages= { "com.iscas.quickframe.domain.p" }) //设置Repository所在位置
public class PrimaryConfig {
	@Autowired
	@Qualifier("permissionDataSource")
	private DataSource permissionDataSource;
	
	 @Primary
	 @Bean(name = "entityManagerPrimary")
	 public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
	     return entityManagerFactoryPrimary(builder).getObject().createEntityManager();
	 }
	 
	 @Primary
	 @Bean(name = "entityManagerFactoryPrimary")
	 public LocalContainerEntityManagerFactoryBean entityManagerFactoryPrimary (EntityManagerFactoryBuilder builder) {
	     return builder
	             .dataSource(permissionDataSource)
	             .properties(getVendorProperties(permissionDataSource))
	             .packages("com.iscas.quickframe.domain.p") //设置实体类所在位置
	             .persistenceUnit("primaryPersistenceUnit")
	             .build();
	 }
	 
	 @Autowired
	 private JpaProperties jpaProperties;

	 private Map<String, String> getVendorProperties(DataSource dataSource) {
	     return jpaProperties.getHibernateProperties(dataSource);
	 }
	 
	 @Primary
	 @Bean(name = "transactionManagerPrimary")
	 public PlatformTransactionManager transactionManagerPrimary(EntityManagerFactoryBuilder builder) {
	     return new JpaTransactionManager(entityManagerFactoryPrimary(builder).getObject());
	 }
}
