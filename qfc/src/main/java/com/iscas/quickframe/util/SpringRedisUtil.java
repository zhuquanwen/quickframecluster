package com.iscas.quickframe.util;

import java.io.Serializable;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;


public class SpringRedisUtil {

	@SuppressWarnings("unchecked")
	private static RedisTemplate<Serializable, Serializable> redisTemplate = 
				(RedisTemplate<Serializable, Serializable>) SpringUtil
						.getBean("redisTemplate");
	
	
	public static void saveBytes(byte[] key,byte[] value) {
		
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.set(key, value);
				
				return null;
			}
		});
	}
	
	public static void save(final String key, Object value) {

		final byte[] vbytes = SerializeUtil.serialize(value);
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.set(redisTemplate.getStringSerializer().serialize(key), vbytes);
				
				return null;
			}
		});
	}
	
	public static void saveBytes(byte[] key,byte[] value,int expire) {
		
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				//connection.set(redisTemplate.getStringSerializer().serialize(key), vbytes);
				connection.setEx(key, expire, value);
				return null;
			}
		});
	}
	
	public static void save(final String key, Object value, long seconds) {

		final byte[] vbytes = SerializeUtil.serialize(value);
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				//connection.set(redisTemplate.getStringSerializer().serialize(key), vbytes);
				connection.setEx(redisTemplate.getStringSerializer().serialize(key), seconds, vbytes);
				return null;
			}
		});
	}

	public static byte[] getBytes(byte[] key){
		return  redisTemplate.execute(new RedisCallback<byte[]>() {
			@Override
			public byte[] doInRedis(RedisConnection connection)
					throws DataAccessException {
				if (connection.exists(key)) {
					byte[] valuebytes = connection.get(key);
					
					return valuebytes;
				}
				return null;
			}
		});
	}
	
	public static <T> T get(final String key, Class<T> elementType) {
		return redisTemplate.execute(new RedisCallback<T>() {
			@Override
			public T doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[] keybytes = redisTemplate.getStringSerializer().serialize(key);
				if (connection.exists(keybytes)) {
					byte[] valuebytes = connection.get(keybytes);
					@SuppressWarnings("unchecked")
					T value = (T) SerializeUtil.unserialize(valuebytes);
					return value;
				}
				return null;
			}
		});
	}
	
	public static Object deleteBytes(byte[] key) {
		
		return redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				if (connection.exists(key)) {
					Long i = connection.del(key);
					return (Object) i;
				}
				return null;
			}
		});
	}
	
	public static <T> T delete(final String key) {
		return redisTemplate.execute(new RedisCallback<T>() {
			@SuppressWarnings("unchecked")
			@Override
			public T doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[] keybytes = redisTemplate.getStringSerializer().serialize(key);
				if (connection.exists(keybytes)) {
					Long i = connection.del(keybytes);
					return (T) i;
				}
				return null;
			}
		});
	}
	
	public static void flushDB() {
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				//connection.set(redisTemplate.getStringSerializer().serialize(key), vbytes);
				connection.flushDb();
				return null;
			}
		});
	}
	public static Long dbSize() {
		return redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				//connection.set(redisTemplate.getStringSerializer().serialize(key), vbytes);
				Long size = 0L;
				size = connection.dbSize();
				return size;
			}
		});
	}
	
	public static Set<byte[]> keys(String pattern) {
		return redisTemplate.execute(new RedisCallback<Set<byte[]>>() {
			@Override
			public Set<byte[]> doInRedis(RedisConnection connection)
					throws DataAccessException {
				Set<byte[]> keys = null;
				byte[] patternbytes = redisTemplate.getStringSerializer().serialize(pattern);
				keys = connection.keys(patternbytes);
				return keys;
			}
		});
	}
}
