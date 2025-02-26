package com.sushant.service;

import com.sushant.model.CardItem;
import com.sushant.model.Cart;
import com.sushant.model.Product;
import com.sushant.model.User;

public interface CartService {
    public CardItem addCartItem(User user , Product product, String size,int quantity);
    public Cart findUserCart(User user);
}
