package com.sushant.service;

import com.sushant.model.Product;
import com.sushant.model.User;
import com.sushant.model.WishList;

public interface WishListService {
    WishList createWishList(User user);
    WishList getWishListByUserId(User user);

    WishList addProductToWishList(User user, Product product);
}
