package com.project.shopapp.Service;

import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderService {
    Order createOrder(OrderDTO orderDTO);

    Order getOrder(Long id);

    List<Order> findByUserId(Long userId);

    Order updateOrder(Long id, OrderDTO orderDTO);

    void deleteOrder(Long id);

    Page<Order> findByKeyWord(String keyword, PageRequest pageRequest);
}
