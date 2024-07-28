package com.example.rd.util;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

public class Lock {
    private String name;
    private StringRedisTemplate stringRedisTemplate;

    private static final String PREFIX = "lock:";

    public Lock(String name, StringRedisTemplate stringRedisTemplate) {
        this.name = name;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public boolean tryLock(long timeout) {
        long threadId = Thread.currentThread().threadId();
        Boolean success = stringRedisTemplate.opsForValue().setIfAbsent(PREFIX + name, String.valueOf(threadId), timeout, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(success);
    }

    public void unlock() {
        stringRedisTemplate.delete(PREFIX + name);
    }
}
