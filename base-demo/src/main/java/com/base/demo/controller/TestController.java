package com.base.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Qualifier("redisTemplateCommon")
    @Autowired
    private RedisTemplate redisTemplateCommon;

    @Qualifier("redisTemplateCluster")
    @Autowired
    private RedisTemplate redisTemplateCluster;

    @RequestMapping("/say")
    public String say(String name) {
        try {
            String retV = "Hello " + name;

            redisTemplate.opsForZSet().add("test", retV, System.currentTimeMillis());

            redisTemplateCommon.opsForZSet().add("test-common", retV, System.currentTimeMillis());

            redisTemplateCluster.opsForZSet().add("test-common", retV, System.currentTimeMillis());
            return retV;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
