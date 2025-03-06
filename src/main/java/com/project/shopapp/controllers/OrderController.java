package com.project.shopapp.controllers;

import com.project.shopapp.Service.OrderService;
import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.models.Order;
import com.project.shopapp.response.OrderListResponse;
import com.project.shopapp.util.LocalizationUtils;
import jakarta.validation.Valid;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/order")
public class OrderController {
    private final OrderService orderService;
    private final LocalizationUtils localizationUtils;
    @PostMapping("")
    public ResponseEntity<?> createNew(@Valid @RequestBody OrderDTO orderDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> error = result.getFieldErrors().stream().map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage()).toList();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            Order order = orderService.createOrder(orderDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("user/{user_id}") // lấy danh sách order của user theo id
    public ResponseEntity<?> getOrdersbyUser(@Valid @PathVariable("user_id") long user_id) {
        try {
            List<Order> OrderByUserId = orderService.findByUserId(user_id);
            return ResponseEntity.ok().body(OrderByUserId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{id}") // lấy ra chi tiết order
    public ResponseEntity<?> getOrders(@Valid @PathVariable("id") long id) {
        try {
            Order order = orderService.getOrder(id);
            return ResponseEntity.ok().body(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@Valid @PathVariable("id") long id, @Valid @RequestBody OrderDTO orderDTO) {
        try {
            Order order = orderService.updateOrder(id,orderDTO);
            return ResponseEntity.ok().body(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@Valid @PathVariable Long id) {
        //Soft delete
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Delete " + id + " successfully!");
    }

    @GetMapping("/get-orders-by-keyword")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderListResponse> getOrderByKeyword(@RequestParam(required = false) String keyword,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int limit,
                                                         Pageable pageable
                                                         ) {
        PageRequest pageRequest = PageRequest.of(page,limit, Sort.by("id").ascending());

        Page<Order> orderPage = orderService.findByKeyWord(keyword,pageRequest);
        int totalPage = orderPage.getTotalPages();
        List<Order> orderList = orderPage.getContent();
    return ResponseEntity.ok(
            OrderListResponse.builder()
                    .orders(orderList)
                    .TotalPages(totalPage)
                    .build()
    );
    }
}
//3:11