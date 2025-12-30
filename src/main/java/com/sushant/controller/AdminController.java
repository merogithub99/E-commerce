package com.sushant.controller;

import com.sushant.domain.AccountStatus;
import com.sushant.model.Seller;
import com.sushant.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.PrivilegedAction;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminController {
private  final SellerService sellerService;
//seller service


    @PatchMapping("/seller/{id}/status/{status}")
    public ResponseEntity<Seller> updateSellerStatus(
            @PathVariable Long id,
            @PathVariable AccountStatus status) throws Exception {

        Seller updatedSeller = sellerService.updateSellerAccountStatus(id, status);
        return ResponseEntity.ok(updatedSeller);
    }
}
