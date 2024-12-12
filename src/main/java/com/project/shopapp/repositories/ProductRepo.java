package com.project.shopapp.repositories;

import com.project.shopapp.models.Product;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product,Long> {
    boolean existsByName (String name);
    Page<Product> findAll(Pageable pageable);
    @Query(value = "SELECT * FROM products p WHERE " +
            "( :categoryId IS NULL OR :categoryId = 0 OR p.categories_id = :categoryId) "+
            "AND ( :keyword IS NULL OR p.name LIKE %:keyword% " +
            "OR p.description LIKE  %:keyword%)"  ,
            nativeQuery = true)
    Page<Product> searchProducts(@Param("categoryId") long categoryId,
                                 @Param("keyword") String keyword,
                                 Pageable pageable
    );
    @Query(value = "Select * from products p where p.id in :productIds" ,
            nativeQuery = true)
    List<Product> findProductsByIds(@Param("productIds") List<Long> productsId);
}
