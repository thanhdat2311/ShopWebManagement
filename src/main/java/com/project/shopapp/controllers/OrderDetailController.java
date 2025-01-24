package com.project.shopapp.controllers;

import com.project.shopapp.Service.OrderDetailService;
import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.OrderDetail;
import com.project.shopapp.util.LocalizationUtils;
import jakarta.validation.Valid;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/orderdetail")
public class OrderDetailController {
    private final OrderDetailService orderDetailService;
    private final LocalizationUtils localizationUtils;

    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> error = result.getFieldErrors().stream().map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage()).toList();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            OrderDetail orderDetail = orderDetailService.createOrderDetail(orderDetailDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(orderDetail);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}") // lấy danh sách order của user theo id
    public ResponseEntity<?> getOrderDetail(@Valid @PathVariable("id") Long id) {
        try {
            if (id >= 0) {
                OrderDetail orderDetail = orderDetailService.getOrderDetail(id);
                return ResponseEntity.ok().body(orderDetail);
            } else {
                return ResponseEntity.badRequest().body("Id must be >=0");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/order/{order_id}") // lấy danh sách order detail của order theo id
    public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("order_id") Long orderId) {
        try {
            List<OrderDetail> orderDetailByOrder = orderDetailService.findByOrderId(orderId);
            return ResponseEntity.ok().body(orderDetailByOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(@Valid @PathVariable("id") long id, @Valid @RequestBody OrderDetailDTO updateOrderDetailDTO) {
        try {
            OrderDetail orderDetail = orderDetailService.updateOrderDetail(id,updateOrderDetailDTO);
            return ResponseEntity.ok().body(orderDetail);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@Valid @PathVariable Long id) {
        return ResponseEntity.ok("Delete " + id + " successfully!");
    }
}
