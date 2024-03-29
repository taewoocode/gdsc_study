package com.example.gdsc.data.repository;

import com.example.gdsc.data.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByName(String name, Sort sort);

    Page<Product> findByName(String name, Pageable pageable);

    @Query("SELECT p FROM Product AS p WHERE p.name= :name")
    List<Product> findByName(@Param("name")String name);
}
