package com.example.rd.service.impl;

import com.example.rd.dto.Result;
import com.example.rd.entity.ShopType;
import com.example.rd.repository.ShopTypeRepository;
import com.example.rd.service.ShopTypeService;
import com.example.rd.util.RedisConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopTypeServiceImpl implements ShopTypeService {

    @Autowired
    ShopTypeRepository shopTypeRepository;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public Result getAllList() throws JsonProcessingException {
        String val = stringRedisTemplate.opsForValue().get(RedisConstants.CACHE_SHOP_TYPE_KEY);

        if (StringUtils.isBlank(val)) {
            List<ShopType> shopTypes = shopTypeRepository.findAll();
            val = objectMapper.writeValueAsString(shopTypes);
            stringRedisTemplate.opsForValue().set(RedisConstants.CACHE_SHOP_TYPE_KEY, val);
            return Result.ok(shopTypes);
        }
        List<ShopType> shopTypes = objectMapper.readValue(val, List.class);
        return Result.ok(shopTypes);
    }
}
