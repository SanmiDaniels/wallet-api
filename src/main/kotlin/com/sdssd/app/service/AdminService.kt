package com.sdssd.app.service

import com.sdssd.app.repository.WalletRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
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




}
