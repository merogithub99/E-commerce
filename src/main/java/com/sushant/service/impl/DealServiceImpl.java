package com.sushant.service.impl;

import com.sushant.model.Deal;
import com.sushant.model.HomeCategory;
import com.sushant.repository.DealRepo;
import com.sushant.repository.HomeCategoryRepository;
import com.sushant.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//@RequiredArgsConstructor
public class DealServiceImpl implements DealService {

    private final DealRepo dealRepo;
    private final HomeCategoryRepository homeCategoryRepository;

    public DealServiceImpl(DealRepo dealRepo, HomeCategoryRepository homeCategoryRepository) {
        this.dealRepo = dealRepo;
        this.homeCategoryRepository = homeCategoryRepository;
    }

    @Override
    public List<Deal> getDeals() {
        return dealRepo.findAll();
    }

    @Override
    public Deal createDeal(Deal deal) {
        HomeCategory category = homeCategoryRepository.findById(deal.getCategory().getId()).orElse(null);
        Deal newDeal = dealRepo.save(deal);
        newDeal.setCategory(category);
        newDeal.setDiscount(deal.getDiscount());
        return dealRepo.save(newDeal);
    }

    @Override
    public Deal updateDeal(Deal deal, Long id) throws Exception {
        Deal existingDeal = dealRepo.findById(id).orElse(null);
        HomeCategory category = homeCategoryRepository.findById(deal.getCategory().getId()).orElseThrow(() -> new Exception("deal not exist"));

        if (existingDeal != null) {
            if (deal.getDiscount() != null) {
                existingDeal.setDiscount(deal.getDiscount());
            }
            if (category != null) {
                existingDeal.setCategory(category);
            }
            return dealRepo.save(existingDeal);
        }
        throw new Exception("Deal not found");
    }

        @Override
        public void deleteDeal (Long id) throws Exception {
            Deal deal = dealRepo.findById(id).orElseThrow(() -> new Exception("deal not found"));
            dealRepo.delete(deal);
        }
    }
