package com.example.rd.service.impl;

import com.example.rd.dto.Result;
import com.example.rd.entity.Voucher;
import com.example.rd.entity.VoucherOrder;
import com.example.rd.repository.VoucherOrderRepository;
import com.example.rd.repository.VoucherRepository;
import com.example.rd.service.VoucherOrderService;
import com.example.rd.util.IdGenerator;
import com.example.rd.util.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class VoucherOrderServiceImpl implements VoucherOrderService {

    @Autowired
    VoucherOrderRepository voucherOrderRepository;

    @Autowired
    VoucherRepository voucherRepository;

    @Autowired
    IdGenerator idGenerator;

    @Override
    @Transactional
    public Result seckillVoucher(Long id) {
        //一人只可購買一個
        int count = voucherOrderRepository.countByVoucherIdAAndUserIdAnd(id, UserHolder.getUser().getId());
        if (count > 0) {
            return Result.fail("Voucher already bought");
        }

        Voucher voucher = voucherRepository.findById(id).orElse(null);
        if (voucher == null) {
            return Result.fail("Voucher not found");
        }

        // =0 賣光
        // >=1 有庫存
        // =-1 沒有限制
        if (voucher.getStock() == 0) {
            return Result.fail("Voucher out of stock");
        } else if (voucher.getStock() >= 1) {
            //Optimistic Lock Compare and Swap
            int result = voucherRepository.casSave(id);
            if (result != 1) {
                return Result.fail("Voucher out of stock");
            }
        }

        VoucherOrder order = new VoucherOrder();
        order.setId(idGenerator.getId("order"));
        order.setUserId(UserHolder.getUser().getId());
        order.setVoucherId(id);
        order.setPayType(1);
        order.setStatus(1);
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        voucherOrderRepository.save(order);
        return Result.ok(order);
    }
}
