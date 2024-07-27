package com.example.rd.service;

import com.example.rd.dto.Result;
import com.example.rd.entity.Voucher;

public interface VoucherService {
    public Result getVoucherOfShop(Long shopId);

    public void addVoucher(Voucher voucher);
}
