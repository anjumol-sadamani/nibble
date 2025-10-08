package com.nibble.orderservice.dto;

import java.util.List;

public record OrderDto(Long vendorId, Long customerId, List<ItemDto> items) {
}
