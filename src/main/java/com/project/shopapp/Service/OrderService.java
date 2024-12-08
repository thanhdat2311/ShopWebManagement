package com.project.shopapp.Service;

import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.OrderStatus;
import com.project.shopapp.models.User;
import com.project.shopapp.repositories.OrderRepo;
import com.project.shopapp.repositories.UserRepo;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService implements IOrderService {
    private final UserRepo userRepo;
    private final OrderRepo orderRepo;
    private final ModelMapper modelMapper;

    @Override
    public Order createOrder(OrderDTO orderDTO) {
        User user = userRepo.findById(orderDTO.getUser_id())
                .orElseThrow(() -> new IllegalArgumentException("Cannot found user with id " + orderDTO.getUser_id()));
        modelMapper.typeMap(OrderDTO.class, Order.class).addMappings(mapper -> mapper.skip(Order::setId));
        Order order = new Order();
        modelMapper.map(orderDTO, order);
        order.setUser(user);
        order.setOrders_date(new Date());
        order.setStatus(OrderStatus.Pending);

        if (orderDTO.getShipping_date() == null || orderDTO.getShipping_date().isBefore(LocalDate.now())) {
            throw new InvalidParameterException("Shipping Date must be at least Today!");
        } else {
            order.setShipping_date(orderDTO.getShipping_date());
        }
        order.setActive(true);
        orderRepo.save(order);
        return order;
    }

    @Override
    public Order getOrder(Long id) {
        Order order = orderRepo.findById(id).orElseThrow( () -> new InvalidParameterException("Cannot Find Order With Id: "+id));
        return order;
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        return orderRepo.findByUser_id(userId);
    }

    @Override
    public Order updateOrder(Long id, OrderDTO orderDTO) {
        Order order = orderRepo.findById(id).orElseThrow(()->new InvalidParameterException("Cannot found!"));
        User user = userRepo.findById(order.getUser().getId()).orElseThrow(()->new InvalidParameterException("Cannot Found User"));
        modelMapper.typeMap(OrderDTO.class,Order.class).addMappings(mapper->mapper.skip(Order::setId));
        modelMapper.map(orderDTO,order);
        orderRepo.save(order);
        return order;
    }

    @Override
    public void deleteOrder(Long id) {
    Order order = orderRepo.findById(id).orElseThrow(()-> new InvalidParameterException("Cannot find Order With Id "+ id));
    order.setActive(false);
    orderRepo.save(order);
    }
}
