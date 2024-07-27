package com.example.rd.repository;

import com.example.rd.entity.ShopType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ShopTypeRepository extends JpaRepository<ShopType, String>, JpaSpecificationExecutor<ShopType> {

}