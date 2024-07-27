package com.example.rd.repository;

import com.example.rd.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BlogRepository extends JpaRepository<Blog, String>, JpaSpecificationExecutor<Blog> {

    @Query("SELECT b " +
            "FROM Blog b " +
            "WHERE b.userId = :id")
    Page<Blog> searchBlog(@Param("id") String id, Pageable pageable);
}