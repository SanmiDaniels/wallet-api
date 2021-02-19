package com.sdssd.app.service

import com.sdssd.app.enums.TransactionType
import com.sdssd.app.model.Transaction
import com.sdssd.app.model.Wallet
import com.sdssd.app.repository.TransactionRepository
import com.sdssd.app.repository.WalletRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.*

@Service
@Transactional
class TransactionService(val transactionRepository: TransactionRepository,
                         val walletRepository: WalletRepository) {

    fun saveTransaction(transaction: Transaction): Transaction{
        return transactionRepository.save(transaction)
    }

    fun getTransaction(transactionId: UUID): Transaction {
        return transactionRepository.getOne(transactionId)
    }



    fun makeTransaction(transaction: Transaction): Transaction? {
        val fromWallet = transaction.fromWallet
        val toWallet = transaction.toWallet

        if (transaction.approved!!) return null;

        if (transaction.transactionType == TransactionType.FUND.name){
            toWallet?.balance = toWallet?.balance?.add(BigDecimal.valueOf(transaction.amount!!))
        }

        if (transaction.transactionType == TransactionType.WITHDRAW.name) {
            fromWallet?.balance = fromWallet?.balance?.subtract(BigDecimal.valueOf(transaction.amount!!))
        }

        if (transaction.transactionType == TransactionType.TRANSFER.name){
            toWallet?.balance = toWallet?.balance?.add(BigDecimal.valueOf(transaction.amount!!))
            fromWallet?.balance = fromWallet?.balance?.subtract(BigDecimal.valueOf(transaction.amount!!))
        }

        transaction.approved = true
        toWallet?.let { walletRepository.save(it) }
        fromWallet?.let { walletRepository.save(it) }
        return saveTransaction(transaction)
    }

    fun getTransactions(): List<Transaction>{
        return transactionRepository.findAll();
    }


}
