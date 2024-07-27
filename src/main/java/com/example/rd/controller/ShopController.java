package com.example.rd.controller;

import com.example.rd.dto.Result;
import com.example.rd.entity.Shop;
import com.example.rd.service.ShopService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    ShopService shopService;

    @GetMapping("/of/type")
    public Result getShopByType(@RequestParam("typeId") int typeId,
                                @RequestParam(value = "current", defaultValue = "1") int current) {
        return shopService.getShopByType(typeId, current - 1);
    }

    @GetMapping("/{id}")
    public Result queryShopById(@PathVariable("id") long id) throws JsonProcessingException, InterruptedException {
        return shopService.getResultById(id);
    }

    @PutMapping
    public Result updateShop(@RequestBody Shop shop) {
        return shopService.update(shop);
    }

}
