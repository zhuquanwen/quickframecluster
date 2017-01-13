package com.iscas.quickframe.config;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.session.Session;
import org.jasig.cas.client.util.AbstractConfigurationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.iscas.quickframe.config.redis.RedisCacheSessionDao;

/**
*@auhor:zhuquanwen
*@date:2016年12月16日
*@desc: 由于SingleSignOutFilter是final的，自定义了一个过滤器，此过滤器模仿Cas的
*		SingleSignOutFilter，重写销毁session的部分，将redis中存储的session删除。
*		（暂时不使用）
*/
public final class MySingleSignOutFilter extends AbstractConfigurationFilter {
	
    private static final MySingleSignOutHandler handler = new MySingleSignOutHandler();
    @Autowired
	@Qualifier(value="redisTemplate")
    private RedisTemplate<Object, Object> redisTemplate;
    @Resource(name = "redisTemplate")
    private ValueOperations<Serializable, Session> valueOps;
    @Autowired
    private RedisCacheSessionDao redisCacheSessionDao;

    public void init(final FilterConfig filterConfig) throws ServletException {
        if (!isIgnoreInitConfiguration()) {
            handler.setArtifactParameterName(getPropertyFromInitParams(filterConfig, "artifactParameterName", "ticket"));
            handler.setLogoutParameterName(getPropertyFromInitParams(filterConfig, "logoutParameterName", "logoutRequest"));
        }
        handler.init();
    }

    public void setArtifactParameterName(final String name) {
        handler.setArtifactParameterName(name);
    }
    
    public void setLogoutParameterName(final String name) {
        handler.setLogoutParameterName(name);
    }

    public void setSessionMappingStorage(final MyHashMapBackedSessionMappingStorage storage) {
        handler.setSessionMappingStorage(storage);
    }
    
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (handler.isTokenRequest(request)) {
//        	System.out.println(request.getSession().getId());
            handler.recordSession(request,response);
        } else if (handler.isLogoutRequest(request)) {
//        	//页面session销毁前，将redis中的Session删掉，否则会认为session还在，
        	
            handler.destroySession(request,redisCacheSessionDao);

            
            // Do not continue up filter chain
            return;
        } else {
            log.trace("Ignoring URI " + request.getRequestURI());
        }
        
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {
        // nothing to do
    }
    
    protected static MySingleSignOutHandler getSingleSignOutHandler() {
        return handler;
    }
}
