package com.nibble.orderservice.service;

import com.nibble.orderservice.dto.OrderDto;
import com.nibble.orderservice.entity.Order;
import com.nibble.orderservice.entity.OrderItem;
import com.nibble.orderservice.enums.OrderStatus;
import com.nibble.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepo;

    public Order createOrder(OrderDto orderDto){
       boolean isValidOrder = validateOrder(orderDto);
       if(!isValidOrder) throw new IllegalArgumentException("order is not valid");

       BigDecimal totalPrice = calculateTotal(orderDto);


       List<OrderItem> orderItems = orderDto.items()
               .stream()
               .map(itemDto ->
                       new OrderItem(itemDto.itemName(),itemDto.quantity(),itemDto.price()))
               .toList();

        Order order = new Order(
                totalPrice, OrderStatus.PENDING, orderDto.customerId(), orderDto.vendorId());
        order.addOrderItems(orderItems);


       return orderRepo.save(order);
    }

    private BigDecimal calculateTotal(OrderDto orderDto) {
       return orderDto.items()
                .stream()
                .map(item -> item.price().multiply(BigDecimal.valueOf(item.quantity())))
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    private boolean validateOrder(OrderDto orderDto) {
        //check if items stock is still available by calling vendor service
        return true;
    }


}
