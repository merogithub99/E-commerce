package com.sushant.service.impl;

import com.sushant.model.CardItem;
import com.sushant.model.Cart;
import com.sushant.model.Product;
import com.sushant.model.User;
import com.sushant.repository.CartItemRepo;
import com.sushant.repository.CartRepo;
import com.sushant.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
//@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepo cartRepo;
    private final CartItemRepo cartItemRepo;

    public CartServiceImpl(CartRepo cartRepo, CartItemRepo cartItemRepo) {
        this.cartRepo = cartRepo;
        this.cartItemRepo = cartItemRepo;
    }

    @Override
    public CardItem addCartItem(User user, Product product, String size, int quantity) {
        Cart cart = findUserCart(user);
        CardItem isPresent = cartItemRepo.findByCartAndProductAndSize(cart, product, size);

        if (isPresent == null) {
            CardItem cardItem = new CardItem();
            cardItem.setProduct(product);
            cardItem.setQuantity(quantity);
            cardItem.setUserId(user.getId());
            cardItem.setMrpPrice(quantity*product.getMrpPrice());
            cardItem.setSize(size);

            int totalPrice = quantity * product.getSellingPrice();
            cardItem.setSellingPrice(totalPrice);

            cart.getCardItems().add(cardItem);
            cardItem.setCart(cart);
            return  cartItemRepo.save(cardItem);
        }
        return isPresent;
    }

    @Override
    public Cart findUserCart(User user) {
        Cart cart = cartRepo.findByUserId(user.getId());
        int totalPrice = 0;
        int totalDiscountedPrice = 0;
        int totalItem = 0;
        for (CardItem cardItem : cart.getCardItems()) {
            totalPrice += cardItem.getMrpPrice();
            totalDiscountedPrice += cardItem.getSellingPrice();
            totalItem += cardItem.getQuantity();
        }
        cart.setTotalMrpPrice(totalPrice);
        cart.setTotalItem(totalItem);
        cart.setTotalSellingPrice(totalDiscountedPrice);
        cart.setDiscount(calculateDiscountPercentage(totalPrice, totalDiscountedPrice));
        cart.setTotalItem(totalItem);

        return cart;
        // 9 38
    }

    private int calculateDiscountPercentage(int mrpPrice, int sellingPrice) {

        if (mrpPrice <= 0) {
            return 0;
//            throw new IllegalArgumentException("actual price must be greater than zero");
        }

        double discount = mrpPrice - sellingPrice;
        double discountPercentage = (discount / mrpPrice) * 100;
        return (int) discountPercentage;
    }
}
