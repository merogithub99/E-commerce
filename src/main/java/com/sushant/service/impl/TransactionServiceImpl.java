package com.sushant.service.impl;

import com.sushant.model.Order;
import com.sushant.model.Seller;
import com.sushant.model.Transaction;
import com.sushant.repository.SellerRepo;
import com.sushant.repository.TransactionalRepo;
import com.sushant.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
//@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionalRepo transactionalRepo;
    private final SellerRepo sellerRepo;

    public TransactionServiceImpl(TransactionalRepo transactionalRepo, SellerRepo sellerRepo) {
        this.transactionalRepo = transactionalRepo;
        this.sellerRepo = sellerRepo;
    }


    @Override
    public Transaction createTransaction(Order order) {
        Seller seller = sellerRepo.findById(order.getSellerId()).get();
        Transaction transaction = new Transaction();
        transaction.setSeller((seller));
        transaction.setCustomer(order.getUser());
        transaction.setOrder(order);

        return transactionalRepo.save(transaction);
    }

    @Override
    public List<Transaction> getTransactionBySellerId(Seller seller) {
        return
                transactionalRepo.findAllBySellerId(seller.getId());
    }

    @Override
    public List<Transaction> getAllTransactions() {

        return transactionalRepo.findAll();
    }
}
