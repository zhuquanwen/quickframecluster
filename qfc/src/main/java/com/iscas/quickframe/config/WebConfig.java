package com.iscas.quickframe.config;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
/**
*@auhor:zhuquanwen
*@date:2016年12月1日
*@desc:配置一些异常页面
*/
@SuppressWarnings("deprecation")
@Configuration
public class WebConfig {
	@Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
 
        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404");
                ErrorPage error502Page = new ErrorPage(HttpStatus.BAD_GATEWAY, "/502");
                ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500");
                ErrorPage errorValidPage = new ErrorPage(HttpStatus.BAD_REQUEST,"/400");
                container.addErrorPages(error404Page,error502Page,error500Page,errorValidPage);
                
            }

			
        };
    }
}
