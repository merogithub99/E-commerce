package com.sushant.repository;

import com.sushant.model.Order;
import com.sushant.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepo extends JpaRepository<OrderItem,Long> {
}
