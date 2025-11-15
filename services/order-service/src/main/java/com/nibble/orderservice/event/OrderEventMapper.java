package com.nibble.orderservice.event;

import com.nibble.orderservice.entity.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class OrderEventMapper {

    public OrderCreatedEvent toOrderCreatedEvent(Order order) {
        List<OrderCreatedEvent.OrderItemEvent> orderItemEvents =
                order.getOrderItems()
                        .stream()
                        .map( o -> new OrderCreatedEvent.OrderItemEvent(
                                o.getItemName(), o.getQuantity(), o.getPrice()
                        )).toList();
        return new OrderCreatedEvent(
                order.getId(), order.getCustomerId(), order.getVendorId(),
                order.getTotalPrice(), order.getStatus(), LocalDateTime.now(),
                orderItemEvents
        );
    }
}
