package com.iscas.quickframe.config.redis;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

/**
*@auhor:zhuquanwen
*@date:2016年12月16日
*@desc:redis管理session的Dao
*/
@Component
public class RedisCacheSessionDao extends CachingSessionDAO {
	@Autowired
	@Qualifier(value="redisTemplate")
    private RedisTemplate<Object, Object> redisTemplate;

    @Resource(name = "redisTemplate")
    //private ValueOperations<Serializable, Session> valueOps;
    private ValueOperations<String, Session> valueOps;
    private int timeToLive = 15; //session缓存时间15分钟

    @Override
    protected void doUpdate(Session session) {
        valueOps.set(session.getId().toString(), session, timeToLive, TimeUnit.MINUTES);
    }

    @Override
    protected void doDelete(Session session) {
        if (session == null || session.getId() == null) {
            return;
        }
        redisTemplate.delete(session.getId());
    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        valueOps.set(sessionId.toString(), session, timeToLive, TimeUnit.MINUTES);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        return (SimpleSession)valueOps.get(sessionId);
    }

    public int getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(int timeToLive) {
        this.timeToLive = timeToLive;
    }

}

