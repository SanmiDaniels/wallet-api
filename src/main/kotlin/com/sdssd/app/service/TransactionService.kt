package com.sdssd.app.service

import com.sdssd.app.model.Transaction
import com.sdssd.app.repository.TransactionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TransactionService(val transactionRepository: TransactionRepository) {

    fun saveTransaction(transaction: Transaction): Transaction{
        return transactionRepository.save(transaction)
    }







}
