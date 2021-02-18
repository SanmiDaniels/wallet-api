package com.sdssd.app.service

import com.sdssd.app.enums.TransactionType
import com.sdssd.app.enums.UserType
import com.sdssd.app.model.Transaction
import com.sdssd.app.repository.WalletRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.*

@Service
@Transactional
class AdminService(val walletService: WalletService, val userService: UserService,
                    val transactionService: TransactionService, val walletRepository: WalletRepository) {


    fun changeMainWallet(useremail: String, toWalletId: UUID): Boolean{

        val user = userService.getUserByEmail(useremail);
        val newMainWallet = walletService.getWalletById(toWalletId);

        if (user.isEmpty) return false
        if (newMainWallet.isEmpty) return false;

        val currentMain =  user.get().wallets?.find { wallet -> wallet.isMain }
        currentMain?.isMain = false
        newMainWallet.get().isMain = true

        walletRepository.saveAll(listOf(currentMain, newMainWallet.get()))

        return true;
    }

    fun approveTransaction(transactionId: UUID){
       val transaction =  transactionService.getTransaction(transactionId);

        if (transaction.approved!!) return

        if (transaction.transactionType == TransactionType.FUND.name) transactionService.creditWallet(transaction.amount!!, transaction.toWallet?.id!!)

        if (transaction.transactionType == TransactionType.WITHDRAW.name) transactionService.debitWallet(transaction.amount!!, transaction.fromWallet?.id!!)

        if (transaction.transactionType == TransactionType.TRANSFER.name){
            transactionService.creditWallet(transaction.amount!!, transaction.toWallet?.id!!)
            transactionService.debitWallet(transaction.amount!!, transaction.fromWallet?.id!!)
        }

        transaction.approved = true
        transactionService.saveTransaction(transaction)
    }


    fun promoteUser(useremail: String) {

        val user = userService.getUserByEmail(useremail).get();

        if (user.userType == UserType.NOOB.name) user.userType = UserType.ELITE.name

        if (user.userType == UserType.ELITE.name){
            user.userType = UserType.ADMIN.name;
            user.wallets.clear();
        }
        userService.saveUser(user)
    }

    fun demoteUser(useremail: String) {

        val user = userService.getUserByEmail(useremail).get();

        if (user.userType == UserType.ELITE.name) user.userType = UserType.NOOB.name

        if (user.userType == UserType.ADMIN.name) user.userType = UserType.ELITE.name;

        userService.saveUser(user)
    }

}
