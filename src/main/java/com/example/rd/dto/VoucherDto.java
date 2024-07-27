package com.example.rd.dto;

import com.example.rd.entity.Voucher;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoucherDto {
    private Long id;
    private Long shopId;
    private String title;
    private String subTitle;
    private String rules;
    private Long payValue;
    private Long actualValue;
    private Long type;
    private Integer stock;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;

    VoucherDto(){}
    public VoucherDto(Voucher voucher){
        this.id = voucher.getId();
        this.shopId = voucher.getShopId();
        this.title = voucher.getTitle();
        this.subTitle = voucher.getSubTitle();
        this.rules = voucher.getRules();
        this.payValue = voucher.getPayValue();
        this.actualValue = voucher.getActualValue();
        this.type = voucher.getType();
        this.stock = voucher.getStock();
    }
}
