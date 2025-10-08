package com.nibble.orderservice.controller;

import com.nibble.orderservice.dto.OrderDto;
import com.nibble.orderservice.entity.Order;
import com.nibble.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<Order> createOrder(@RequestBody OrderDto orderDto){
       Order order = orderService.createOrder(orderDto);
       return ResponseEntity.ok(order);
    }
}
