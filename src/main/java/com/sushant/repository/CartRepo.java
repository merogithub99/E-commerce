package com.sushant.repository;

import com.sushant.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CartRepo extends JpaRepository<Cart,Long> {
    Cart findByUserId(Long userId);
}
