package com.iscas.quickframe.config;
/**
*@auhor:zhuquanwen
*@date:2016年12月16日
*@desc: 暂时不使用
*/
/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.session.Session;
import org.jasig.cas.client.session.SessionMappingStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * HashMap backed implementation of SessionMappingStorage.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.1
 *
 */
public final class MyHashMapBackedSessionMappingStorage implements SessionMappingStorage {
	@Autowired
	@Qualifier(value="redisTemplate")
    private RedisTemplate<Object, Object> redisTemplate;

    @Resource(name = "redisTemplate")
    private ValueOperations<Serializable, Session> valueOps;
    /**
     * Maps the ID from the CAS server to the Session.
     */
    private final Map<String,HttpSession> MANAGED_SESSIONS = new HashMap<String,HttpSession>();

    /**
     * Maps the Session ID to the key from the CAS Server.
     */
    private final Map<String,String> ID_TO_SESSION_KEY_MAPPING = new HashMap<String,String>();

    private final Log log = LogFactory.getLog(getClass());

	public synchronized void addSessionById(String mappingId, HttpSession session) {
        ID_TO_SESSION_KEY_MAPPING.put(session.getId(), mappingId);
        MANAGED_SESSIONS.put(mappingId, session);

	}                               

	public synchronized void removeBySessionById(String sessionId) {
        if (log.isDebugEnabled()) {
            log.debug("Attempting to remove Session=[" + sessionId + "]");
        }

        final String key = ID_TO_SESSION_KEY_MAPPING.get(sessionId);

        if (log.isDebugEnabled()) {
            if (key != null) {
                log.debug("Found mapping for session.  Session Removed.");
            } else {
                log.debug("No mapping for session found.  Ignoring.");
            }
        }
        MANAGED_SESSIONS.remove(key);
        ID_TO_SESSION_KEY_MAPPING.remove(sessionId);
	}

	public synchronized HttpSession removeSessionByMappingId(String mappingId) {
		final HttpSession session = MANAGED_SESSIONS.get(mappingId);

        if (session != null) {
        	removeBySessionById(session.getId());
        }

        return session;
	}
	public synchronized void removeRedisSessionByMappingId(String mappingId) {
		final HttpSession session = MANAGED_SESSIONS.get(mappingId);

		 if (session == null || session.getId() == null) {
	            return;
	        }
	     redisTemplate.delete(session.getId());

       
	}
	
	  
//	@Override  
//	public void removeBySessionById(String sessionId) {  
//	    log.debug("Attempting to remove Session=[" + sessionId + "]");  
//	    String sessionKey = getKey(sessionId);  
//	    String st = stringRedisTemplate.opsForValue().get(sessionKey);  
//	  
//	    if (log.isDebugEnabled()) {  
//	        if (st != null) {  
//	            log.debug("Found mapping for session.  Session Removed.");  
//	        } else {  
//	            log.debug("No mapping for session found.  Ignoring.");  
//	        }  
//	    }  
//	    stringRedisTemplate.delete(sessionKey);  
//	    sessionRedisTemplate.delete(st);  
//	}
}

