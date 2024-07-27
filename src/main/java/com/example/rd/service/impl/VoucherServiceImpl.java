package com.example.rd.service.impl;

import com.example.rd.dto.Result;
import com.example.rd.dto.VoucherDto;
import com.example.rd.entity.Voucher;
import com.example.rd.repository.VoucherRepository;
import com.example.rd.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VoucherServiceImpl implements VoucherService {

    @Autowired
    VoucherRepository voucherRepository;

    @Override
    public Result getVoucherOfShop(Long shopId) {
        List<Voucher> vouchers = voucherRepository.getByShopId(shopId);
        List<VoucherDto> voucherDtos = new ArrayList<>();
        for (Voucher voucher : vouchers) {
            voucherDtos.add(new VoucherDto(voucher));
        }
        return Result.ok(voucherDtos);
    }

    @Override
    @Transactional
    public void addVoucher(Voucher voucher) {
        voucher.setCreateTime(LocalDateTime.now());
        voucher.setUpdateTime(LocalDateTime.now());
        voucherRepository.save(voucher);
    }
}
