package com.nibble.orderservice.dto;

import java.math.BigDecimal;

public record ItemDto(String itemName, BigDecimal price, Integer quantity) {
}
