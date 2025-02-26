package com.sushant.repository;

import com.sushant.model.SellerReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerReportRepo  extends JpaRepository<SellerReport,Long> {

    SellerReport findBySellerId(Long sellerId);
}
