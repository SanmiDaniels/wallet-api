package com.sdssd.app.service

import com.sdssd.app.dto.WalletDto
import com.sdssd.app.repository.WalletRepository
import org.springframework.stereotype.Service

@Service
class WalletService(walletRepository: WalletRepository) {


    fun createWallet(wallet: WalletDto): String {
        return "Ok"
    }


}
