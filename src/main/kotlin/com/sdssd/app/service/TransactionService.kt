package com.sdssd.app.service

import com.sdssd.app.enums.TransactionType
import com.sdssd.app.model.Transaction
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

    fun debitWallet(amount: Double, walletId: UUID){

        var wallet = walletRepository.findById(walletId).get()

        wallet.balance?.subtract(BigDecimal.valueOf(amount));
        walletRepository.save(wallet)

    }

    fun creditWallet(amount: Double, walletId: UUID){
        var wallet = walletRepository.findById(walletId).get()

        wallet.balance?.add(BigDecimal.valueOf(amount));
        walletRepository.save(wallet)
    }

    fun makeTransaction(transaction: Transaction){
        if (transaction.approved!!) return

        if (transaction.transactionType == TransactionType.FUND.name) creditWallet(transaction.amount!!, transaction.toWallet?.id!!)

        if (transaction.transactionType == TransactionType.WITHDRAW.name) debitWallet(transaction.amount!!, transaction.fromWallet?.id!!)

        if (transaction.transactionType == TransactionType.TRANSFER.name){
            creditWallet(transaction.amount!!, transaction.toWallet?.id!!)
            debitWallet(transaction.amount!!, transaction.fromWallet?.id!!)
        }

        transaction.approved = true
        saveTransaction(transaction)
    }


}
