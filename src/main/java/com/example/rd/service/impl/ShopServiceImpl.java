package com.example.rd.service.impl;

import com.example.rd.dto.Result;
import com.example.rd.entity.RedisData;
import com.example.rd.entity.Shop;
import com.example.rd.repository.ShopRepository;
import com.example.rd.service.ShopService;
import com.example.rd.util.RedisConstants;
import com.example.rd.util.SystemConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    ShopRepository shopRepository;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    ObjectMapper objectMapper;

    private static final ExecutorService CACHE_BUILD_EXECUTOR = Executors.newFixedThreadPool(3);

    @Override
    public Result getShopByType(int typeId, int page) {
        Page<Shop> shops = shopRepository.getByShopType(typeId, PageRequest.of(page, SystemConstants.MAX_PAGE_SIZE));
        return Result.ok(shops.getContent());
    }

    @Override
    public Result getResultById(long id) throws JsonProcessingException, InterruptedException {
        //穿透版本
        //return queryWithPassThrough(id);

        //Mutex版本
        return queryWithMutex(id);

        //LogicLock版本
//        return queryWithLogicLock(id);
    }

    public Result queryWithLogicLock(Long id) throws JsonProcessingException {
        String key = RedisConstants.CACHE_SHOP_KEY + id;
        //1. 查Redis
        String val = stringRedisTemplate.opsForValue().get(key);
        RedisData<Shop> redisData = null;
        //2.從Redis從查出來的值是"" 表示MySQL不存在
        if ("".equals(val)) {
            return Result.fail("商店不存在");
        }
        if (!StringUtils.isBlank(val)) {
            // 3.Redis有值則檢查expireTime
            redisData = objectMapper.readValue(val, RedisData.class);
            // 4.時間未過期直接返回資料
            if (redisData.getExpireTime().isAfter(LocalDateTime.now())) {
                return Result.ok(redisData.getData());
            }
        }
        //5. Redis不存在或過期，取得鎖之後fork一個Thread進行建立Redis資料，主Thread直接返回舊資料
        Boolean lockKey = lock(RedisConstants.LOCK_SHOP_KEY + id);
        if (lockKey == Boolean.FALSE) {
            //6. 沒得到鎖，直接返回舊資料
            return redisData == null ? Result.fail("商店不存在") : Result.ok(redisData.getData());
        } else {
            //7. 取得鎖，fork一個Thread進行建立Redis資料，主Thread直接返回舊資料
            CACHE_BUILD_EXECUTOR.submit(() -> {
                try {
                    this.shop2Redis(id, RedisConstants.CACHE_SHOP_TTL);
                } catch (JsonProcessingException | InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    unlock(RedisConstants.LOCK_SHOP_KEY + id);
                }
            });
            //8. 返回舊資料
            return redisData == null ? Result.fail("商店不存在") : Result.ok(redisData.getData());
        }
    }

    public void shop2Redis(long id, Long expireTime) throws JsonProcessingException, InterruptedException {
        // 為了模擬多Thread測試，這邊等待200ms
        // 1.Redis重建前，其他Thread不會再次重建
        // 2.其他Thread可以取得舊資料回傳
        Thread.sleep(200);
        Shop shop = shopRepository.findById(id).orElse(null);
        if (shop == null) {
            //8. MySQL也不存在 避免Cache Penetration 使用""存入Redis
            stringRedisTemplate.opsForValue().set(RedisConstants.CACHE_SHOP_KEY + id, "", RedisConstants.CACHE_NULL_TTL, TimeUnit.MINUTES);
        } else {
            //9.存入Redis
            RedisData<Shop> redisData = new RedisData<>(shop, expireTime);
            stringRedisTemplate.opsForValue().set(RedisConstants.CACHE_SHOP_KEY + id, objectMapper.writeValueAsString(redisData));
        }
    }

    public Result queryWithMutex(long id) throws JsonProcessingException, InterruptedException {
        String key = RedisConstants.CACHE_SHOP_KEY + id;
        //1. 查Redis
        String val = stringRedisTemplate.opsForValue().get(key);
        //2.從Redis從查出來的值是"" 表示MySQL不存在
        if ("".equals(val)) {
            return Result.fail("商店不存在");
        }
        if (StringUtils.isBlank(val)) {
            //3. val為null 表示Redis不存在，但MySQL可能存在，
            //4. 嘗試加鎖
            try {
                Boolean lockKey = lock(RedisConstants.LOCK_SHOP_KEY + id);
                if (lockKey == Boolean.FALSE) {
                    //5. 等待一段時間後重新取得並執行
                    Thread.sleep(50);
                    return queryWithMutex(id);
                }
                // 為了模擬多Thread測試，這邊等待200ms
                //Thread.sleep(200);
                val = stringRedisTemplate.opsForValue().get(key);
                //6. 取得鎖後再次檢查Redis資料，真的不存在才需要查MySQL並存入Redis
                if (StringUtils.isBlank(val)) {
                    Shop shop = shopRepository.findById(id).orElse(null);
                    if (!(shop == null)) {
                        //7. MySQL也不存在 避免Cache Penetration 使用""存入Redis
                        val = objectMapper.writeValueAsString(shop);
                        stringRedisTemplate.opsForValue().set(key, val, RedisConstants.CACHE_SHOP_TTL, TimeUnit.MINUTES);
                    } else {
                        //8.存入Redis
                        stringRedisTemplate.opsForValue().set(key, "", RedisConstants.CACHE_NULL_TTL, TimeUnit.MINUTES);
                    }

                }
            } finally {
                //9.解鎖
                unlock(RedisConstants.LOCK_SHOP_KEY + id);
            }
        }

        //10.從Redis查出來的值是正常的
        if (StringUtils.isBlank(val)) {
            return Result.fail("商店不存在");
        }
        Shop shop = objectMapper.readValue(val, Shop.class);
        return Result.ok(shop);
    }


    public Result queryWithPassThrough(long id) throws JsonProcessingException {
        String key = RedisConstants.CACHE_SHOP_KEY + id;
        //1. 查Redis
        String val = stringRedisTemplate.opsForValue().get(key);
        //2.從Redis從查出來的值是"" 表示MySQL不存在
        if ("".equals(val)) {
            return Result.fail("商店不存在");
        }
        if (StringUtils.isBlank(val)) {
            //3.val為null 表示Redis不存在，但MySQL可能存在
            Shop shop = shopRepository.findById(id).orElse(null);
            if (shop == null) {
                //4. MySQL也不存在 避免Cache Penetration 使用""存入Redis
                stringRedisTemplate.opsForValue().set(key, "", RedisConstants.CACHE_NULL_TTL, TimeUnit.MINUTES);
                return Result.fail("商店不存在");
            }
            //5.存入Redis
            val = objectMapper.writeValueAsString(shop);
            stringRedisTemplate.opsForValue().set(key, val, RedisConstants.CACHE_SHOP_TTL, TimeUnit.MINUTES);
            return Result.ok(shop);
        }

        //6.從Redis查出來的值是正常的
        Shop shop = objectMapper.readValue(val, Shop.class);
        return Result.ok(shop);
    }


    @Override
    @Transactional
    public Result update(Shop shop) {

        if (shop.getId() == null) {
            return Result.fail("商店不可為空");
        }

        //1. 先更新MySQL
        shopRepository.save(shop);
        //2. 再刪除Redis
        stringRedisTemplate.delete(RedisConstants.CACHE_SHOP_KEY + shop.getId());
        return Result.ok();
    }

    private Boolean lock(String key) {
        return stringRedisTemplate.opsForValue().setIfAbsent(key, "1", RedisConstants.LOCK_TTL, TimeUnit.SECONDS);
    }

    private Boolean unlock(String key) {
        return stringRedisTemplate.delete(key);
    }

}
