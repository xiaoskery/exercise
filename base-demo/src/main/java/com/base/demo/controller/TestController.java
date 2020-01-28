package com.base.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/say")
    public String say(String name) {
        String retV = "Hello " + name;

        redisTemplate.opsForZSet().add("test", retV, System.currentTimeMillis());
        return retV;
    }
}
