package com.sushant.service.impl;

import com.sushant.model.Cart;
import com.sushant.model.Coupon;
import com.sushant.model.User;
import com.sushant.repository.CartRepo;
import com.sushant.repository.CouponRepo;
import com.sushant.repository.UserRepository;
import com.sushant.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
//@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    private final CouponRepo couponRepo;
    private final CartRepo cartRepo;
    private final UserRepository userRepository;

    public CouponServiceImpl(CouponRepo couponRepo, CartRepo cartRepo, UserRepository userRepository) {
        this.couponRepo = couponRepo;
        this.cartRepo = cartRepo;
        this.userRepository = userRepository;
    }

    @Override
    public Cart applyCoupon(String code, double orderValue, User user) throws Exception {
        Coupon coupon = couponRepo.findByCode(code);

        Cart cart = cartRepo.findByUserId(user.getId());

        if (coupon == null) {
            throw new Exception("coupon not valid");
        }
        if (user.getUsedCoupons().contains(coupon)) {
            throw new Exception("coupon already used");
        }
        if (orderValue < coupon.getMinimumOrderValue()) {
            throw new Exception("valid for minimum order value " + coupon.getMinimumOrderValue());
        }
        if (coupon.isActive() &&
                LocalDate.now().isAfter(coupon.getValidityStartDate())
                && LocalDate.now().isBefore(coupon.getValidityEndDate())
        ) {
            user.getUsedCoupons().add(coupon);
            userRepository.save(user);

            double discountedPrice =
                    (cart.getTotalSellingPrice() * coupon.getDiscountPercentage()) / 100;
            cart.setTotalSellingPrice(cart.getTotalSellingPrice() - discountedPrice);
            cart.setCouponCode(code);
            cartRepo.save(cart);
            return cart;
        }
        throw new Exception("coupon not valid");
    }

    @Override
    public Cart removeCoupon(String code, User user) throws Exception {

        Coupon coupon = couponRepo.findByCode(code);

        if (coupon == null) {
            throw new Exception("coupon not found ... ");
        }

        Cart cart = cartRepo.findByUserId(user.getId());
        double discountedPrice = (cart.getTotalSellingPrice() * coupon.getDiscountPercentage()) / 100;

        cart.setTotalSellingPrice(cart.getTotalSellingPrice() + discountedPrice);
        cart.setCouponCode(null);


        return cartRepo.save(cart);

    }

    @Override
    public Coupon findCouponById(Long id) throws Exception {
        return couponRepo.findById(id).orElseThrow(() ->
                new Exception("coupon not found"));
    }

    @Override
    @PreAuthorize("hasRole ('ADMIN')")
    public Coupon createCoupon(Coupon coupon) {
        return couponRepo.save(coupon);
    }

    @Override
    public List<Coupon> findAllCoupons() {
        return couponRepo.findAll();
    }

    @Override
    @PreAuthorize("hasRole ('ADMIN')")
    public void deleteCoupon(Long id) throws Exception {
        findCouponById(id);
        couponRepo.deleteById(id);

    }
}
