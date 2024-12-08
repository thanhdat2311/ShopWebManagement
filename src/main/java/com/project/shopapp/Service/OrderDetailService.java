package com.project.shopapp.Service;

import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.OrderDetail;
import com.project.shopapp.models.Product;
import com.project.shopapp.repositories.OrderDetailRepo;
import com.project.shopapp.repositories.OrderRepo;
import com.project.shopapp.repositories.ProductRepo;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderDetailService implements  IOrderDetailService{
    OrderRepo orderRepo;
    OrderDetailRepo orderDetailRepo;
    ProductRepo productRepo;
    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) {
        Order order = orderRepo.findById(orderDetailDTO.getOrder_id()).orElseThrow(()-> new InvalidParameterException("Cannot found order!"));
        Product product = productRepo.findById(orderDetailDTO.getProduct_id()).orElseThrow(()-> new InvalidParameterException("Cannot found product!"));
        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .number_of_product(orderDetailDTO.getNumber_of_product())
                .product(product)
                .price(orderDetailDTO.getPrice())
                .total_money(orderDetailDTO.getTotal_money())
                .color(orderDetailDTO.getColor())
                .build();
        orderDetailRepo.save(orderDetail);
        return orderDetail;
    }

    @Override
    public OrderDetail getOrderDetail(Long id) {
        OrderDetail orderDetail = orderDetailRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Cannot found!"));
        return orderDetail;
    }

    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        List<OrderDetail> listOrderDetail = orderDetailRepo.findByOrder_id(orderId);
        return listOrderDetail;
    }

    @Override
    public OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) {
        OrderDetail orderDetail = orderDetailRepo.findById(id).orElseThrow(()->new IllegalArgumentException("Not Found Order Detail!"));
        orderDetail.setPrice(orderDetailDTO.getPrice());
        orderDetail.setNumber_of_product(orderDetailDTO.getNumber_of_product());
        orderDetail.setTotal_money(orderDetailDTO.getTotal_money());
        orderDetail.setColor(orderDetailDTO.getColor());
        orderDetailRepo.save(orderDetail);
        return orderDetail;
    }

    @Override
    public void deleteOrderDetail(Long id) {

    }
}
