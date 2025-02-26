package com.sushant.repository;

import com.sushant.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionalRepo extends JpaRepository<Transaction ,Long> {
    List<Transaction> findAllBySellerId(Long sellerId);
}
