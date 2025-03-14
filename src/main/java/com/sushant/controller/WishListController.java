package com.sushant.controller;

import com.sushant.model.Product;
import com.sushant.model.User;
import com.sushant.model.WishList;
import com.sushant.service.ProductService;
import com.sushant.service.UserService;
import com.sushant.service.WishListService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
public class WishListController {

    private final WishListService wishListService;
    private final UserService userService;

    private final ProductService productService;

    public WishListController(WishListService wishListService, UserService userService, ProductService productService) {
        this.wishListService = wishListService;
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping()
    public ResponseEntity<WishList> getWishlistByUserId(
            @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        WishList wishlist = wishListService.getWishListByUserId(user);
        return ResponseEntity.ok(wishlist);
    }


    @PostMapping("/add-product/{productId}")
    public ResponseEntity<WishList> addProductToWishlist(
            @PathVariable Long productId,
            @RequestHeader("Authorization") String jwt) throws Exception {

        Product product = productService.findProductById(productId);
        User user = userService.findUserByJwtToken(jwt);
        WishList updatedWishlist = wishListService.addProductToWishList(
                user,
                product);
        return ResponseEntity.ok(updatedWishlist);

    }

}
