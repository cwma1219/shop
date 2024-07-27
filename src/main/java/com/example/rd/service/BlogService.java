package com.example.rd.service;

import com.example.rd.dto.Result;

public interface BlogService {
    public Result queryMyBolg(int page);

    public Result hot(int page);
}
