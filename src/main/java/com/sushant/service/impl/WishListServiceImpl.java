package com.sushant.service.impl;

import com.sushant.model.Product;
import com.sushant.model.User;
import com.sushant.model.WishList;
import com.sushant.repository.WishListRepo;
import com.sushant.service.WishListService;
import org.springframework.stereotype.Service;

@Service

public class WishListServiceImpl implements WishListService {

    private final WishListRepo wishListRepo;

    public WishListServiceImpl(WishListRepo wishListRepo) {
        this.wishListRepo = wishListRepo;
    }

    @Override
    public WishList createWishList(User user) {
        WishList wishList = new WishList();
        wishList.setUser(user);
        return wishListRepo.save(wishList);
    }

    @Override
    public WishList getWishListByUserId(User user) {
        WishList wishList= wishListRepo.findByUserId(user.getId());
    if(wishList==null){
        wishList = createWishList(user);
    }
    return  wishList;
    }

    @Override
    public WishList addProductToWishList(User user, Product product) {
        WishList wishList = getWishListByUserId(user);
        if(wishList.getProducts().contains(product)){
            wishList.getProducts().remove(product);
        }
        else wishList.getProducts().add(product);

        return wishListRepo.save(wishList);
    }
}
