package com.sushant.controller;

import com.stripe.model.PaymentLink;
import com.sushant.model.*;
import com.sushant.response.ApiResponse;
import com.sushant.response.PaymentLinkResponse;
import com.sushant.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private  final PaymentService paymentService;
    private  final UserService userService;
    private  final SellerService sellerService;

    private  final OrderService orderService;
    private  final SellerReportService sellerReportService;
    private  final TransactionService transactionService;

    public PaymentController(PaymentService paymentService, UserService userService, SellerService sellerService, OrderService orderService, SellerReportService sellerReportService, TransactionService transactionService) {
        this.paymentService = paymentService;
        this.userService = userService;
        this.sellerService = sellerService;
        this.orderService = orderService;
        this.sellerReportService = sellerReportService;
        this.transactionService = transactionService;
    }

    @GetMapping("/api/payment/{paymentId}")
    public ResponseEntity<ApiResponse> paymentSuccessfulHandler(
            @PathVariable String paymentId,
            @RequestParam String paymentLinkId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user= userService.findUserByJwtToken(jwt);
        PaymentLinkResponse paymentLinkResponse;

        PaymentOrder paymentOrder = paymentService
                .getPaymentOrderByPaymentId(paymentLinkId);


        boolean paymentSuccess= paymentService.ProceedPaymentOrder(
                paymentOrder,
                paymentId,
                paymentLinkId
        );

        if(paymentSuccess) {
            for (Order order : paymentOrder.getOrders()) {
                transactionService.createTransaction(order);
                Seller seller = sellerService.getSellerById(order.getSellerId());
                SellerReport report = sellerReportService.getSellerReport(seller);
                report.setTotalOrders(report.getTotalOrders()+1);
                report.setTotalEarnings(report.getTotalEarnings()+ order.getTotalSellingPrice());
                report.setTotalSales(report.getTotalSales()+order.getOrderItems().size());
                sellerReportService.updateSellerReport(report);
            }
        }

        ApiResponse apiResponse = new ApiResponse();
         apiResponse.setMessage("payment success");
         return  new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    //11 49

}
