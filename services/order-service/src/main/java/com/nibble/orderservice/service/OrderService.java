package com.nibble.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nibble.orderservice.dto.OrderDto;
import com.nibble.orderservice.entity.Order;
import com.nibble.orderservice.entity.OrderItem;
import com.nibble.orderservice.entity.OutboxMessage;
import com.nibble.orderservice.enums.OrderStatus;
import com.nibble.orderservice.enums.OutboxMessageStatus;
import com.nibble.orderservice.event.OrderCreatedEvent;
import com.nibble.orderservice.event.OrderEventMapper;
import com.nibble.orderservice.repository.OrderRepository;
import com.nibble.orderservice.repository.OutboxMessageRepository;
import com.nibble.orderservice.service.event.OrderEventPublisher;
import jakarta.transaction.Transactional;
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
    private final OrderEventMapper orderEventMapper;
    private final OutboxMessageRepository outboxMessageRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public Order createOrder(OrderDto orderDto) throws JsonProcessingException {
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

        OrderCreatedEvent event = orderEventMapper.toOrderCreatedEvent(savedOrder);
        String payloadJson = objectMapper.writeValueAsString(event);

        OutboxMessage outbox = new OutboxMessage();
        outbox.setAggregateType("ORDER");
        outbox.setAggregateId(order.getId().toString());
        outbox.setType("OrderCreated");
        outbox.setPayload(payloadJson);
        outbox.setStatus(OutboxMessageStatus.PENDING);
        outboxMessageRepository.save(outbox);


        log.info("Successfully saved order with ID: {}", savedOrder.getId());

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
