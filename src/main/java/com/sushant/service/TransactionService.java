package com.sushant.service;

import com.sushant.model.Order;
import com.sushant.model.Seller;
import com.sushant.model.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction createTransaction(Order order);
    List<Transaction> getTransactionBySellerId(Seller seller);
    List<Transaction> getAllTransactions();
}
