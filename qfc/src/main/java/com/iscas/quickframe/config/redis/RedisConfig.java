package com.iscas.quickframe.config.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
*@auhor:zhuquanwen
*@date:2016年12月16日
*@desc:Redis配置（暂时不使用）
*/

@Configuration
public class RedisConfig {
//	@Bean
//	public RedisConnectionFactory redisConnectionFactory(){
//		return new JredisConnectionFactory();
//	}

	@Bean(name="redisTemplate")
	@Qualifier(value="redisTemplate")
	@ConditionalOnMissingBean(name = "redisTemplate") 
	public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
		RedisTemplate<Object,Object> template = new RedisTemplate<Object,Object>();
		template.setConnectionFactory(redisConnectionFactory);
//		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = 
//				new Jackson2JsonRedisSerializer(Object.class);
//		ObjectMapper om = new ObjectMapper();
//		om.setVisibility(PropertyAccessor.ALL,JsonAutoDetect.Visibility.ANY);
//		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//		jackson2JsonRedisSerializer.setObjectMapper(om);
//		template.setValueSerializer(jackson2JsonRedisSerializer);
//		template.setKeySerializer(new StringRedisSerializer());
		
		template.setKeySerializer(new StringRedisSerializer());
		template.afterPropertiesSet();
		return template;
	}
}
