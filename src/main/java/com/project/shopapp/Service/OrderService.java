package com.project.shopapp.Service;

import com.project.shopapp.dtos.CartItemDTO;
import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.models.*;
import com.project.shopapp.repositories.OrderDetailRepo;
import com.project.shopapp.repositories.OrderRepo;
import com.project.shopapp.repositories.ProductRepo;
import com.project.shopapp.repositories.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
@AllArgsConstructor
public class OrderService implements IOrderService {
    private final OrderDetailRepo orderDetailRepo;
    private final ProductRepo productRepo;
    private final UserRepo userRepo;
    private final OrderRepo orderRepo;
    private final ModelMapper modelMapper;

    @Override
    @Transactional // Nếu bất kỳ thao tác nào trong transaction thất bại, toàn bộ transaction sẽ bị hủy bỏ
    public Order createOrder(OrderDTO orderDTO) {
        User user = userRepo.findById(orderDTO.getUser_id())
                .orElseThrow(() -> new IllegalArgumentException("Cannot found user with id " + orderDTO.getUser_id()));
        modelMapper.typeMap(OrderDTO.class, Order.class).addMappings(mapper -> mapper.skip(Order::setId));
        Order order = new Order();
        modelMapper.map(orderDTO, order);
        order.setUser(user);
        order.setOrders_date(new Date());
        order.setStatus(OrderStatus.Pending);
        LocalDate shippingDate = orderDTO.getShipping_date() == null ? LocalDate.now() : orderDTO.getShipping_date();
        if ( shippingDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Shipping Date must be at least Today!");
        } else {
            order.setShipping_date(orderDTO.getShipping_date());
        }
        order.setActive(true); // nên default trong sql
        orderRepo.save(order);
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (CartItemDTO cartItemDTO : orderDTO.getCartItemDTOList()) {
            OrderDetail orderDetail = new OrderDetail();
            long quantity = cartItemDTO.getQuantity();
            orderDetail.setOrder(order);
            // thông tin order
            Product product = productRepo.findById(cartItemDTO.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + cartItemDTO.getProductId()));
            orderDetail.setProduct(product);
            orderDetail.setNumber_of_product(quantity);
            orderDetail.setPrice(product.getPrice());
            orderDetail.setTotal_money(product.getPrice() * quantity);
            orderDetailList.add(orderDetail);
        }
        orderDetailRepo.saveAll(orderDetailList);
        return order;
    }

    @Override
    public Order getOrder(Long id) {
        Order order = orderRepo.findById(id).orElseThrow(() -> new InvalidParameterException("Cannot Find Order With Id: " + id));
        return order;
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        return orderRepo.findByUser_id(userId);
    }

    @Override
    public Order updateOrder(Long id, OrderDTO orderDTO) {
        Order order = orderRepo.findById(id).orElseThrow(() -> new InvalidParameterException("Cannot found!"));
        User user = userRepo.findById(order.getUser().getId()).orElseThrow(() -> new InvalidParameterException("Cannot Found User"));
        modelMapper.typeMap(OrderDTO.class, Order.class).addMappings(mapper -> mapper.skip(Order::setId));
        modelMapper.map(orderDTO, order);
        orderRepo.save(order);
        return order;
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = orderRepo.findById(id).orElseThrow(() -> new InvalidParameterException("Cannot find Order With Id " + id));
        order.setActive(false);
        orderRepo.save(order);
    }
}
