package com.sushant.repository;

import com.sushant.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListRepo extends JpaRepository<WishList,Long> {
    WishList findByUserId(Long userId);
}
