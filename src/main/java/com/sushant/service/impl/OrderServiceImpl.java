package com.sushant.service.impl;

import com.sushant.domain.OrderStatus;
import com.sushant.domain.PaymentStatus;
import com.sushant.model.*;
import com.sushant.repository.AddressRepository;
import com.sushant.repository.OrderItemRepo;
import com.sushant.repository.OrderRepo;
import com.sushant.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
//@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final AddressRepository addressRepository;

    private final OrderItemRepo orderItemRepo;

    public OrderServiceImpl(OrderRepo orderRepo, AddressRepository addressRepository, OrderItemRepo orderItemRepo) {
        this.orderRepo = orderRepo;
        this.addressRepository = addressRepository;
        this.orderItemRepo = orderItemRepo;
    }


    // 9 58 min

    @Override
    public Set<Order> createOrder(User user, Address shippingAddress, Cart cart) {
        if (!user.getAddresses().contains(shippingAddress)) {
            user.getAddresses().add(shippingAddress);
        }
        Address address = addressRepository.save(shippingAddress);

//        brand 1 => 4 shirt
//        brand 2=> 5 pen

        Map<Long, List<CardItem>> itemsBySeller = cart.getCardItems().stream()
                .collect(Collectors.groupingBy(item -> item.getProduct()
                        .getSeller().getId()));

        Set<Order> orders = new HashSet<>();
        for (Map.Entry<Long, List<CardItem>> entry : itemsBySeller.entrySet()) {
            Long sellerId = entry.getKey();
            List<CardItem> items = entry.getValue();
            int totalOrderPrice = items.stream().mapToInt(
                    CardItem::getSellingPrice
            ).sum();
            int totalItem = items.stream().mapToInt(CardItem::getQuantity).sum();


            Order createdOrder = new Order();
            createdOrder.setUser(user);
            createdOrder.setSellerId(sellerId);
            createdOrder.setTotalMrpPrice(totalOrderPrice);
            createdOrder.setTotalSellingPrice(totalOrderPrice);
            createdOrder.setTotalItem(totalItem);
            createdOrder.setShippingAddress(address);
            createdOrder.setOrderStatus(OrderStatus.PENDING);
            createdOrder.getPaymentDetails().setStatus(PaymentStatus.PENDING);
            Order savedorder = orderRepo.save(createdOrder);
            orders.add(savedorder);

            List<OrderItem> orderItems = new ArrayList<>();
            for (CardItem item : items) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(savedorder);
                orderItem.setMrpPrice(item.getMrpPrice());
                orderItem.setProduct(item.getProduct());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setSize(item.getSize());
                orderItem.setUserId(item.getUserId());
                orderItem.setSellingPrice(item.getSellingPrice());

                savedorder.getOrderItems().add(orderItem);

                OrderItem savedOrderItem = orderItemRepo.save(orderItem);
                orderItems.add(savedOrderItem);

            }

        }

        return orders;
    }

    @Override
    public Order findOrderById(long id) throws Exception {

        return orderRepo.findById(id).orElseThrow(()->
                new Exception("order not found...."));
    }

    @Override
    public List<Order> userOrderHistory(Long userId) {

        return orderRepo.findByUserId(userId);
    }

    @Override
    public List<Order> sellersOrder(Long sellerId) {
        return orderRepo.findBySellerId(sellerId);

    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus orderStatus) throws Exception {
       Order order=findOrderById(orderId);
       order.setOrderStatus(orderStatus);
        return orderRepo.save(order);
    }

    @Override
    public Order cancelOrder(Long orderId, User user) throws Exception {
        Order order=findOrderById(orderId);

        if(!user.getId().equals(order.getUser().getId())){
            throw  new Exception("you dont have access to this order");
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        return orderRepo.save(order);
    }

    @Override
    public OrderItem getOrderItemById(Long id) throws Exception {
        return orderItemRepo.findById(id).orElseThrow(()->
                new Exception("order item not exist"));
    }
}
