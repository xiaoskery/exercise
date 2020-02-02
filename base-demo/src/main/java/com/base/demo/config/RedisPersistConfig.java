package com.base.demo.config;

import java.time.Duration;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisPersistConfig {

    @Autowired
    private RedisCommonProperties redisCommonProperties;
    @Autowired
    private RedisPersistProperties redisPersistProperties;
    @Autowired
    private RedisClusterProperties redisClusterProperties;

    @Primary
    @Bean
    public LettuceConnectionFactory redisConnectionFactory(GenericObjectPoolConfig genericObjectPoolConfig) {
        RedisStandaloneConfiguration redisStandaloneConfiguration =
            new RedisStandaloneConfiguration(redisPersistProperties.getHost(), redisPersistProperties.getPort());
        redisStandaloneConfiguration.setPassword(redisPersistProperties.getPassword());

        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
            .commandTimeout(Duration.ofSeconds(60)).poolConfig(genericObjectPoolConfig).build();

        return new LettuceConnectionFactory(redisStandaloneConfiguration, clientConfig);
    }

    @Bean
    public GenericObjectPoolConfig genericObjectPoolConfig() {
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxIdle(200);
        genericObjectPoolConfig.setMinIdle(20);
        genericObjectPoolConfig.setMaxTotal(1000);
        genericObjectPoolConfig.setMaxWaitMillis(3000);
        return genericObjectPoolConfig;
    }

    @Primary
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(Object.class));
        return redisTemplate;
    }

    @Qualifier("redisConnectionFactoryCommon")
    @Bean
    public LettuceConnectionFactory redisConnectionFactoryCommon() {
        RedisStandaloneConfiguration redisStandaloneConfiguration =
            new RedisStandaloneConfiguration(redisCommonProperties.getHost(), redisCommonProperties.getPort());
        redisStandaloneConfiguration.setPassword(redisCommonProperties.getPassword());
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Qualifier("redisTemplateCommon")
    @Bean
    public RedisTemplate redisTemplateCommon(
        @Qualifier(value = "redisConnectionFactoryCommon") LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(Object.class));
        return redisTemplate;
    }

    @Qualifier("redisConnectionFactoryCluster")
    @Bean
    public LettuceConnectionFactory redisConnectionFactoryCluster(GenericObjectPoolConfig genericObjectPoolConfig) {
        RedisClusterConfiguration redisClusterConfiguration =
            new RedisClusterConfiguration(redisClusterProperties.getNodes());
        redisClusterConfiguration.setPassword(redisClusterProperties.getPassword());

        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
            .commandTimeout(Duration.ofSeconds(60)).poolConfig(genericObjectPoolConfig).build();

        return new LettuceConnectionFactory(redisClusterConfiguration, clientConfig);
    }

    @Qualifier("redisTemplateCluster")
    @Bean
    public RedisTemplate redisTemplateCluster(
        @Qualifier(value = "redisConnectionFactoryCluster") LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(Object.class));
        return redisTemplate;
    }
}
