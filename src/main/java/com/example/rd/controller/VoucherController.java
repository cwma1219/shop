package com.example.rd.controller;

import com.example.rd.dto.Result;
import com.example.rd.entity.Voucher;
import com.example.rd.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/voucher")
public class VoucherController {

    @Autowired
    VoucherService voucherService;

    @GetMapping("/list/{shopId}")
    public Result queryVoucherOfShop(@PathVariable("shopId") Long shopId) {
        return voucherService.getVoucherOfShop(shopId);
    }

    @PostMapping("/add")
    public void addVoucher(@RequestBody Voucher voucher) {
        voucherService.addVoucher(voucher);
    }

}
