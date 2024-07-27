package com.example.rd.repository;

import com.example.rd.entity.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShopRepository extends JpaRepository<Shop, Long>, JpaSpecificationExecutor<Shop> {

    @Query("SELECT s " +
            "FROM Shop s " +
            "WHERE s.typeId = :typeId")
    public Page<Shop> getByShopType(@Param("typeId") int typeId, Pageable pageable);
}