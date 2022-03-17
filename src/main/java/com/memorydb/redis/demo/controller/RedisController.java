package com.memorydb.redis.demo.controller;

import com.memorydb.redis.demo.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class RedisController {

    @Autowired
    RedisService redisService;

    @GetMapping("/test/redis")
    public String saveNRetrieve() {
        log.info("In method : saveNRetrieve");
        return redisService.saveNRetrieve();
    }
}
