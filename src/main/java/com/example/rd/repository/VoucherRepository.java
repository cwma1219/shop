package com.example.rd.repository;

import com.example.rd.entity.Shop;
import com.example.rd.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VoucherRepository extends JpaRepository<Voucher, Long>, JpaSpecificationExecutor<Voucher> {

    @Query("SELECT v " +
            "FROM Voucher v " +
            "WHERE v.shopId = :shopId ")
    List<Voucher> getByShopId(@Param("shopId") Long shopId);

    @Modifying
    @Query("UPDATE Voucher v " +
            "SET v.stock = v.stock - 1 " +
            "WHERE v.id = :id " +
            "AND v.stock > 0 ")
    int casSave(@Param("id") Long id);

}