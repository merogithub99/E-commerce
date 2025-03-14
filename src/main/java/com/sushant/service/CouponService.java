package com.sushant.service;

import com.sushant.model.Cart;
import com.sushant.model.Coupon;
import com.sushant.model.User;

import java.util.List;

public interface CouponService {
    Cart applyCoupon(String code, double orderValue, User user) throws Exception;
    Cart removeCoupon(String code, User user) throws Exception;
    Coupon findCouponById(Long id) throws Exception;
    Coupon createCoupon(Coupon coupon);
    List<Coupon> findAllCoupons();
    void  deleteCoupon(Long id) throws Exception;
}
