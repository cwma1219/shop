package com.example.rd.service;

import com.example.rd.dto.Result;
import com.example.rd.vo.LoginVo;

public interface UserService {

    public Result sendCode(String phone);

    public Result login(LoginVo vo) throws IllegalAccessException;

    public Result getResultById(Long id);
}
