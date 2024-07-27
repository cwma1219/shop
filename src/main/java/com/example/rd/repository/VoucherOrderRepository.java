package com.example.rd.repository;

import com.example.rd.entity.VoucherOrder;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface VoucherOrderRepository extends JpaRepository<VoucherOrder, Long>, JpaSpecificationExecutor<VoucherOrder> {

    @Query("SELECT COUNT(vo) " +
            "FROM VoucherOrder vo " +
            "WHERE vo.voucherId = :voucherId " +
            "AND vo.userId = :userId")
    int countByVoucherIdAAndUserIdAnd(@Param("voucherId") Long voucherId, @Param("userId") Long userId);
}