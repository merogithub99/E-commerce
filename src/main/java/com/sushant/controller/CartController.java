package com.sushant.controller;

import com.sushant.model.CardItem;
import com.sushant.model.Cart;
import com.sushant.model.Product;
import com.sushant.model.User;
import com.sushant.repository.CartItemRepo;
import com.sushant.request.AddItemRequest;
import com.sushant.response.ApiResponse;
import com.sushant.service.CartItemService;
import com.sushant.service.CartService;
import com.sushant.service.ProductService;
import com.sushant.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final UserService userService;
    private final ProductService productService;

    public CartController(CartService cartService, CartItemService cartItemService, UserService userService, ProductService productService) {
        this.cartService = cartService;
        this.cartItemService = cartItemService;
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Cart> findUserCartHandler(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartService.findUserCart(user);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }


    @PutMapping("/add")
    public ResponseEntity<CardItem> addItemToCart(
            @RequestBody AddItemRequest req,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Product product = productService.findProductById(req.getProductId());

        CardItem item = cartService.addCartItem(user,
                product,
                req.getSize(),
                req.getQuantity()
        );

        ApiResponse res = new ApiResponse();
        res.setMessage("item added to cart to successfully");

        return new ResponseEntity<>(item, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItemHandler(
            @PathVariable Long cartItemId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        cartItemService.removeCardItem(user.getId(), cartItemId);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("item removed successfully!");
        return new ResponseEntity<>(apiResponse, HttpStatus.ACCEPTED);
    }

    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<CardItem> updateCartItemHandler(
            @PathVariable Long cartItemId,
            @RequestBody CardItem cardItem,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        CardItem updatedCardItem = null;
        if(cardItem.getQuantity()>0){
            updatedCardItem=cartItemService.updateCartItem( user.getId(), cartItemId,cardItem);
        }

        return  new ResponseEntity<>(updatedCardItem,HttpStatus.ACCEPTED);
    }


}
