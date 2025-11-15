package com.nibble.orderservice.service.event;

import com.nibble.orderservice.entity.Order;
import com.nibble.orderservice.event.OrderCreatedEvent;
import com.nibble.orderservice.event.OrderEventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class OrderEventPublisher {

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;
    private static  final String ORDER_CREATED_TOPIC = "order-created";
    private final OrderEventMapper orderEventMapper;

    public void publishOrderCreatedEvent(Order order, OrderCreatedEvent event){
        try {
            kafkaTemplate
                    .send(ORDER_CREATED_TOPIC, order.getId().toString(),
                            event)
                    .whenComplete(
                            (result, exception) -> {
                                if (exception == null) {
                                    log.info("Successfully published OrderCreated event for order ID: {} to topic: {}",
                                            order.getId(),
                                            result.getRecordMetadata().topic()
                                    );
                                } else {
                                    log.error("publish event failed for order ID: {}", order.getId(), exception);
                                }
                            });
        } catch (Exception e) {
            log.error("Unexpected error while publishing OrderCreated event for order ID: {}", order.getId(), e);
        }
    }

}
