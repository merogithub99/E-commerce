package com.sushant.controller;

import com.sushant.config.JwtProvider;
import com.sushant.domain.AccountStatus;
import com.sushant.exception.SellerException;
import com.sushant.model.Seller;
import com.sushant.model.SellerReport;
import com.sushant.model.SellerReportService;
import com.sushant.model.VerificationCode;
import com.sushant.repository.SellerRepo;
import com.sushant.repository.SellerReportRepo;
import com.sushant.repository.VerificationCodeRepo;
import com.sushant.request.LoginRequest;
import com.sushant.response.ApiResponse;
import com.sushant.response.AuthResponse;
import com.sushant.service.AuthService;
import com.sushant.service.EmailService;
import com.sushant.service.SellerService;
import com.sushant.util.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequiredArgsConstructor
@RequestMapping("/sellers")
public class SellerController {

    private  final EmailService emailService;
    private  final JwtProvider jwtProvider;
    private final SellerService sellerService;
    private final VerificationCodeRepo verificationCodeRepo;
    private final AuthService authService;
    private final SellerReportService sellerReportService;

    public SellerController(EmailService emailService, JwtProvider jwtProvider, SellerService sellerService, VerificationCodeRepo verificationCodeRepo, AuthService authService, SellerReportService sellerReportService) {
        this.emailService = emailService;
        this.jwtProvider = jwtProvider;
        this.sellerService = sellerService;
        this.verificationCodeRepo = verificationCodeRepo;
        this.authService = authService;
        this.sellerReportService = sellerReportService;
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginSeller(@RequestBody LoginRequest req) throws Exception {
        String otp = req.getOtp();
        String email = req.getEmail();

        req.setEmail("seller_" + email);
        AuthResponse authResponse = authService.signin(req);
        return ResponseEntity.ok(authResponse);
    }


    @PatchMapping("/verify/{otp}")
    public ResponseEntity<Seller> verifySellerEmail(
            @PathVariable String otp) throws Exception {
        VerificationCode verificationCode = verificationCodeRepo.findByOtp(otp);
        if(verificationCode == null || !verificationCode.getOtp().equals(otp)){
            throw  new Exception("wrong otp");
        }

        Seller seller=sellerService.verifyEmail(verificationCode.getEmail(), otp);


        return new ResponseEntity<>(seller, HttpStatus.OK);
//        return ResponseEntity.ok(seller);
    }

    @PostMapping
    public ResponseEntity<Seller> createSeller(
            @RequestBody Seller seller) throws Exception {
        Seller savedSeller = sellerService.createSeller(seller);


        String otp = OtpUtil.generateOtp();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(seller.getEmail());
        verificationCodeRepo.save(verificationCode);

        String subject ="Online bazaar email verification code";
        String text ="Welcome to online bazaar,verify your account using this link";
        String frontend_url = "http://localhost:3000/verify-seller";
        emailService.sendVerificationOtpEmail(seller.getEmail(),verificationCode.getOtp(),subject,text +frontend_url);



        return  new ResponseEntity<>(seller,HttpStatus.CREATED);
//        return  ResponseEntity.created(savedSeller);
    }


    @GetMapping("/{id}")
    public  ResponseEntity<Seller> getSellerById(@PathVariable Long id) throws SellerException {
        Seller seller =sellerService.getSellerById(id);
        return  ResponseEntity.ok(seller);
    }


    @GetMapping("/profile")
    public  ResponseEntity<Seller> getSellerByJwt(@RequestHeader("Authorization") String jwt) throws Exception {

        Seller seller =sellerService.getSellerProfile(jwt);
        return ResponseEntity.ok(seller);
    }

    @GetMapping("/report")
    public  ResponseEntity<SellerReport> getSellerReport(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        Seller seller = sellerService.getSellerProfile(jwt);
        SellerReport sellerReport = sellerReportService.getSellerReport(seller);

        return ResponseEntity.ok(sellerReport);
    }


    @GetMapping
    public  ResponseEntity<List<Seller>> getAllSellers(
            @RequestParam(required = false)AccountStatus accountStatus){

        List<Seller> sellers = sellerService.getAllSellers(accountStatus);
        return ResponseEntity.ok(sellers);
    }


    @PatchMapping()
    public ResponseEntity<Seller> updateSeller(
            @RequestHeader("Authorization") String jwt,
            @RequestBody Seller seller) throws Exception {

        Seller profile =sellerService.getSellerProfile(jwt);
        Seller updatedSeller = sellerService.updateSeller(seller.getId(),seller);

        return ResponseEntity.ok(updatedSeller);
    }


    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> deleteSeller(@PathVariable Long id) throws Exception {
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }

}
