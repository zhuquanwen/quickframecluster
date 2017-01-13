package com.iscas.quickframe.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
*@auhor:zhuquanwen
*@date:2016年12月21日
*@desc:
*/


@Component
public class SpringUtil implements ApplicationContextAware{
    private static ApplicationContext applicationContext = null;
 
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
       if(SpringUtil.applicationContext == null){
           SpringUtil.applicationContext  = applicationContext;
       }
      System.out.println("---------------------------------------------------------------------");
      System.out.println("---------------------------------------------------------------------");
    System.out.println("---------------simple.plugin.spring.SpringUtil------------------------------------------------------");
       System.out.println("========ApplicationContext配置成功,在普通类可以通过调用SpringUtils.getAppContext()获取applicationContext对象,applicationContext="+SpringUtil.applicationContext+"========");
      System.out.println("---------------------------------------------------------------------");
    }
   
    //获取applicationContext
    public static ApplicationContext getApplicationContext() {
       return applicationContext;
    }
   
    //通过name获取 Bean.
    public static Object getBean(String name){
       return getApplicationContext().getBean(name);
    }
   
    //通过class获取Bean.
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static  Object getBean(Class clazz){
       return getApplicationContext().getBean(clazz);
    }
   
    //通过name,以及Clazz返回指定的Bean
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static   Object getBean(String name,Class clazz){
       return   getApplicationContext().getBean(name, clazz);
    }
}
