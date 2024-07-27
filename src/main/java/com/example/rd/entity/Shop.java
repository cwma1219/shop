package com.example.rd.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "shop")
public class Shop implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type_id", nullable = false)
    private String typeId;

    @Column(name = "images", nullable = false)
    private String images;

    @Column(name = "area")
    private String area;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "x", nullable = false)
    private String x;

    @Column(name = "y", nullable = false)
    private String y;

    @Column(name = "avg_price")
    private String avgPrice;

    @Column(name = "sold", nullable = false)
    private Long sold;

    @Column(name = "comments", nullable = false)
    private Long comments;

    @Column(name = "score", nullable = false)
    private Integer score;

    @Column(name = "open_hours")
    private String openHours;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

}
