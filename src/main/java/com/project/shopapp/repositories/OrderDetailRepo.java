package com.project.shopapp.repositories;

import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepo extends JpaRepository<OrderDetail,Long> {

    List<OrderDetail> findByOrder_id(Long order_id);
}
