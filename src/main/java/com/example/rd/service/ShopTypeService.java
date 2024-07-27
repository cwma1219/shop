package com.example.rd.service;

import com.example.rd.dto.Result;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface ShopTypeService {

    public Result getAllList() throws JsonProcessingException;
}
