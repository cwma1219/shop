package com.example.rd.controller;

import com.example.rd.dto.Result;
import com.example.rd.service.ShopTypeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shop-type")
public class ShopTypeController {

    @Autowired
    ShopTypeService shopTypeService;

    @GetMapping("list")
    public Result list() throws JsonProcessingException {
        return shopTypeService.getAllList();
    }
}
