package com.sushant.service.impl;

import com.sushant.model.CardItem;
import com.sushant.model.User;
import com.sushant.repository.CartItemRepo;
import com.sushant.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
//@RequiredArgsConstructor
public class CardItemServiceImpl implements CartItemService {
    private  final CartItemRepo cartItemRepo;

    public CardItemServiceImpl(CartItemRepo cartItemRepo) {
        this.cartItemRepo = cartItemRepo;
    }

    @Override
    public CardItem updateCartItem(Long userId, Long id, CardItem cardItem) throws Exception {
        CardItem item= findCardItemById(id);
        User cardItemUser= item.getCart().getUser();
        if (cardItemUser.getId().equals(userId)){
            item.setQuantity(cardItem.getQuantity());
            item.setMrpPrice(item.getQuantity()*item.getProduct().getMrpPrice());
            item.setSellingPrice(item.getQuantity()*item.getProduct().getSellingPrice());
            return  cartItemRepo.save(item);
        }
        throw new Exception("You cannot update  this cardItem");
    }

    @Override
    public void removeCardItem(Long userId, Long cartItemId) throws Exception {
        CardItem item= findCardItemById(cartItemId);
        User cardItemUser= item.getCart().getUser();

        if (cardItemUser.getId().equals(userId)){
            cartItemRepo.delete(item);
        }
        else throw  new Exception("You cannot delete this item");


    }

    @Override
    public CardItem findCardItemById(Long id) throws Exception {
        return cartItemRepo.findById(id).orElseThrow(()->
                new Exception("cart item not found with id"));
    }
}
