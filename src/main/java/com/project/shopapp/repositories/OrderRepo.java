package com.project.shopapp.repositories;

import com.project.shopapp.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order,Long> {
    // tìm đơn hàng của user
    List<Order> findByUser_id (Long userId);

    @Query(value="SELECT * From orders o Where "+
            "(:keyword Is null or :keyword = '' or o.fullname Like %:keyword% or o.address Like %:keyword% " +
            "or o.note like %:keyword%)",
            nativeQuery = true)
    Page<Order> findByKeyWord(String keyword,Pageable pageable);
}
