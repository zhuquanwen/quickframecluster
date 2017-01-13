package com.iscas.quickframe.config;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.cas.CasFilter;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.iscas.quickframe.util.SpringRedisUtil;

@SuppressWarnings("deprecation")
public class MyCasFilter extends CasFilter{
	@Autowired
	@Qualifier(value="redisTemplate")
    private RedisTemplate<Object, Object> redisTemplate;
	@Resource(name = "redisTemplate")
	private ValueOperations<Serializable, Session> valueOps;
	@Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		
		
		AuthenticationToken token = createToken(request, response);
		
		boolean flag =  executeLogin(request, response);
		
		Subject subject = getSubject(request, response);
	    if(subject != null){
	    	Session session = subject.getSession(false);
	    	if(session != null){
	    		if(token != null){
	    			Object obj = token.getCredentials();
	    			if(obj != null){
	    				String ticket = obj.toString();
		    			SpringRedisUtil.save(ticket,  session.getId(),CasConfig.INSTANCE.getRedisSessionStoreTime()*60);
	    				
	    			}
	    		}
	    	}
	    }
		
		return flag;
        
    }
}
