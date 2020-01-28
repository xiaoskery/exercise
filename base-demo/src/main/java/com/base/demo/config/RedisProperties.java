package com.base.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("spring.redis")
public class RedisProperties {
    private String host;
    private int port;
    private int jedisPoolMaxIdel;
    private int jedisPoolMaxWait;
    private int jedisPoolMinIdel;
    private int timeout;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getJedisPoolMaxIdel() {
        return jedisPoolMaxIdel;
    }

    public void setJedisPoolMaxIdel(int jedisPoolMaxIdel) {
        this.jedisPoolMaxIdel = jedisPoolMaxIdel;
    }

    public int getJedisPoolMaxWait() {
        return jedisPoolMaxWait;
    }

    public void setJedisPoolMaxWait(int jedisPoolMaxWait) {
        this.jedisPoolMaxWait = jedisPoolMaxWait;
    }

    public int getJedisPoolMinIdel() {
        return jedisPoolMinIdel;
    }

    public void setJedisPoolMinIdel(int jedisPoolMinIdel) {
        this.jedisPoolMinIdel = jedisPoolMinIdel;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
