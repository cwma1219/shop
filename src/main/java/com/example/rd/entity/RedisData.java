package com.example.rd.entity;

import lombok.Getter;
import lombok.Setter;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;

@Getter
@Setter
public class RedisData<T> {
    private LocalDateTime expireTime;
    private T data;

    public RedisData() {}

    public RedisData(T data, long expire) {
        this.data = data;
        this.expireTime = LocalDateTime.now().plusMinutes(expire);
    }

}
