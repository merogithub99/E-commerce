package com.sushant.repository;

import com.sushant.model.CardItem;
import com.sushant.model.Cart;
import com.sushant.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepo extends JpaRepository<CardItem, Long> {
    CardItem findByCartAndProductAndSize(Cart cart, Product product, String size);
}
