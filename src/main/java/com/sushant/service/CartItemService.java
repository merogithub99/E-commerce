package com.sushant.service;

import com.sushant.model.CardItem;

public interface CartItemService {
    CardItem updateCartItem(Long userId,Long id, CardItem cardItem ) throws Exception;
    void removeCardItem(Long userId, Long cartItemId) throws Exception;
    CardItem findCardItemById(Long id) throws Exception;
}
