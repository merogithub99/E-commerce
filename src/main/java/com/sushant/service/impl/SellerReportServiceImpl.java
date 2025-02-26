package com.sushant.service.impl;

import com.sushant.model.Seller;
import com.sushant.model.SellerReport;
import com.sushant.model.SellerReportService;
import com.sushant.repository.SellerReportRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
//@RequiredArgsConstructor
public class SellerReportServiceImpl implements SellerReportService {

    private  final SellerReportRepo sellerReportRepo;

    public SellerReportServiceImpl(SellerReportRepo sellerReportRepo) {
        this.sellerReportRepo = sellerReportRepo;
    }

    @Override
    public SellerReport getSellerReport(Seller seller) {

        SellerReport sr = sellerReportRepo.findBySellerId(seller.getId());

        if(sr==null){
            SellerReport newReport = new SellerReport();
            newReport.setSeller(seller);
            return sellerReportRepo.save(newReport);
        }
        return sr;
    }

    @Override
    public SellerReport updateSellerReport(SellerReport sellerReport) {
        return sellerReportRepo.save(sellerReport);
    }
}
