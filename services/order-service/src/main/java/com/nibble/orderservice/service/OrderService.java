package com.nibble.orderservice.service;

import com.nibble.orderservice.dto.OrderDto;
import com.nibble.orderservice.entity.Order;
import com.nibble.orderservice.entity.OrderItem;
import com.nibble.orderservice.enums.OrderStatus;
import com.nibble.orderservice.repository.OrderRepository;
import com.nibble.orderservice.service.event.OrderEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepo;
    private final VendorClient vendorClient;
    private final OrderEventPublisher orderEventPublisher;

    public Order createOrder(OrderDto orderDto){
       final boolean isValidOrder = validateOrder(orderDto);
       if(!isValidOrder) throw new IllegalArgumentException("order is not valid");

       final BigDecimal totalPrice = calculateTotal(orderDto);


       final List<OrderItem> orderItems = orderDto.items()
               .stream()
               .map(itemDto ->
                       new OrderItem(itemDto.itemName(),itemDto.quantity(),itemDto.price()))
               .toList();

        final Order order = new Order(
                totalPrice, OrderStatus.PENDING, orderDto.customerId(), orderDto.vendorId());
        order.addOrderItems(orderItems);
        Order savedOrder = orderRepo.save(order);
        log.info("Successfully saved order with ID: {}", savedOrder.getId());

        orderEventPublisher.publishOrderCreatedEvent(savedOrder);
        return savedOrder;
    }

    private BigDecimal calculateTotal(OrderDto orderDto) {
       return orderDto.items()
                .stream()
                .map(item -> item.price().multiply(BigDecimal.valueOf(item.quantity())))
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    private boolean validateOrder(OrderDto orderDto) {
        //check if items stock is still available by calling vendor service
        return vendorClient.checkStockAvailability(orderDto.vendorId(), orderDto.items() );
    }


}
