package com.sdssd.app.dto

import com.sdssd.app.model.Wallet
import java.util.*

class WalletDto (

    val currencyCode: String,

    val mainWallet: Boolean

    ){

    fun toWallet(): Wallet {
        return Wallet(currency = Currency.getInstance(this.currencyCode),main =  this.mainWallet);
    }


}
