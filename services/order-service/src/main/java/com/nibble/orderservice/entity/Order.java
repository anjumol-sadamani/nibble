package com.nibble.orderservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nibble.orderservice.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private BigDecimal totalPrice;
    private OrderStatus status;
    private Long customerId;
    private Long vendorId;

    @JsonIgnore
    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderItem> orderItems = new ArrayList<>();

    public Order(BigDecimal totalPrice, OrderStatus status, Long customerId, Long vendorId) {
        this.totalPrice = totalPrice;
        this.status = status;
        this.customerId = customerId;
        this.vendorId = vendorId;
    }

    public void addOrderItem(OrderItem item){
        orderItems.add(item);
        item.setOrder(this);
    }

    public void addOrderItems(List<OrderItem> items){
        items.forEach(this::addOrderItem);
    }
}
