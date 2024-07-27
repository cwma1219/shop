package com.example.rd.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@Table(name = "voucher")
public class Voucher implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shop_id")
    private Long shopId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "sub_title")
    private String subTitle;

    @Column(name = "rules")
    private String rules;

    @Column(name = "pay_value", nullable = false)
    private Long payValue;

    @Column(name = "actual_value", nullable = false)
    private Long actualValue;

    @Column(name = "type", nullable = false)
    private Long type;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;
}
