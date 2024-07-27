package com.example.rd.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
public class IdGenerator {

    private static final long START_TIMESTAMP = 1704067200L;

    private static final int OFFSET = 32;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //long(Id) 為 2^64 拆分成=> 1(正負) 31(時間戳) 32(編號)
    public long getId(String key){
        LocalDateTime now = LocalDateTime.now();
        long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
        long timestamp = nowSecond - START_TIMESTAMP;

        String date = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        //使用Redis來產生ID，避免多表時出現重複ID
        //date的用途是用來記錄時間，同時避免Redis超過自動增加上限
        long count = stringRedisTemplate.opsForValue().increment("incr:"+key+":"+date);

        //組合Id
        return timestamp << OFFSET | count;
    }
}
