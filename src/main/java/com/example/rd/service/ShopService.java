package com.example.rd.service;

import com.example.rd.dto.Result;
import com.example.rd.entity.Shop;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface ShopService {
    Result getShopByType(int typeId, int page);

    Result getResultById(long id) throws JsonProcessingException, InterruptedException;

    Result update(Shop shop);
}
