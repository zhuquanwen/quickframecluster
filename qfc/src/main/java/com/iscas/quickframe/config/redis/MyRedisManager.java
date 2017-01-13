package com.iscas.quickframe.config.redis;

import java.util.Set;

import org.apache.shiro.subject.SimplePrincipalCollection;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.SerializeUtils;

import com.iscas.quickframe.util.SpringRedisUtil;

public class MyRedisManager extends RedisManager{
	public static String SimplePrincipalCollection_username="SimplePrincipalCollection_username";
	
	public MyRedisManager(){
		
	}
	
	
	public void init(){
		
	}
	
	/**
	 * get value from redis
	 * @param key
	 * @return
	 */
	public byte[] get(byte[] key){
		return SpringRedisUtil.getBytes(key);
	}
	
	/**
	 * set 
	 * @param key
	 * @param value
	 * @return
	 */
	public byte[] set(byte[] key,byte[] value){
		
		try{
			String keyStr = new String(key);
			if(keyStr.startsWith("ShiroCasRealm.authorizationCache")){
				//不在这存储session,已经做了session的redis存储了
				return value;
			}else{
				setSimplePrincipalCollection(key);
			}
		}catch(Exception e){
			
		}
			
		
		this.set(key, value,this.getExpire());
//		Jedis jedis = jedisPool.getResource();
//		try{
//			jedis.set(key,value);
//			if(this.expire != 0){
//				jedis.expire(key, this.expire);
//		 	}
//		}finally{
//			jedisPool.returnResource(jedis);
//		}
		return value;
	}
	
	private void setSimplePrincipalCollection(byte[] key)throws Exception{
		Object obj = SerializeUtils.deserialize(key);
		if(obj instanceof org.apache.shiro.subject.SimplePrincipalCollection ){
			SimplePrincipalCollection spc = (SimplePrincipalCollection)obj;
			if(spc.getPrimaryPrincipal() != null){
				String username = spc.getPrimaryPrincipal().toString();
				String key1 = SimplePrincipalCollection_username +
						"org.apache.shiro.subject.SimplePrincipalCollection" +username;
				SpringRedisUtil.save(key1, spc, getExpire());
			}
			
			//如果是权限的key 把key存在数据库里
		}
	}
	
	/**
	 * set 
	 * @param key
	 * @param value
	 * @param expire
	 * @return
	 */
	public byte[] set(byte[] key,byte[] value,int expire){
//		Jedis jedis = jedisPool.getResource();
//		try{
//			jedis.set(key,value);
//			if(expire != 0){
//				jedis.expire(key, expire);
//		 	}
//		}finally{
//			jedisPool.returnResource(jedis);
//		}
		SpringRedisUtil.saveBytes(key, value,expire);
		return value;
	}
	
	/**
	 * del
	 * @param key
	 */
	public void del(byte[] key){
//		Jedis jedis = jedisPool.getResource();
//		try{
//			jedis.del(key);
//		}finally{
//			jedisPool.returnResource(jedis);
//		}
		SpringRedisUtil.deleteBytes(key);
	}
	
	/**
	 * flush
	 */
	public void flushDB(){
		SpringRedisUtil.flushDB();
	}
	
	/**
	 * size
	 */
	public Long dbSize(){
		return SpringRedisUtil.dbSize();
	}

	/**
	 * keys
	 * @param regex
	 * @return
	 */
	public Set<byte[]> keys(String pattern){
		
		return SpringRedisUtil.keys(pattern);
	}
	
	
	
}
