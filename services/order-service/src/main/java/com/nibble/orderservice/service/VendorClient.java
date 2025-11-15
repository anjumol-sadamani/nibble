package com.nibble.orderservice.service;

import com.nibble.orderservice.dto.ItemDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class VendorClient {

    @CircuitBreaker(name = "vendorService", fallbackMethod = "checkStockAvailabilityFallback")
    public boolean checkStockAvailability(Long vendorId, List<ItemDto> items){
//        if (Math.random() > 0.5) {
//            System.out.println("Vendor service failed!");
//            throw new RuntimeException("Vendor service is down!");
//        }
        System.out.println("Vendor service responded successfully");
        return true;
    }

    private boolean checkStockAvailabilityFallback(Long vendorId, List<ItemDto> items, Exception e){
        System.out.println("Circuit breaker fallback triggered: " + e.getMessage());
        return false;
    }
}
