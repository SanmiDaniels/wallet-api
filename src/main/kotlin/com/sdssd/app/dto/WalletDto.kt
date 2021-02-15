package com.sdssd.app.dto

import com.sdssd.app.model.Wallet
import java.util.*
import javax.validation.constraints.Pattern

class WalletDto (

        @Pattern(regexp = "^(GBP|USD|EUR|NGN|GHS)$", message = "This currency is not supported")
    val currencyCode: String,

    val mainWallet: Boolean

    ){

    fun toWallet(): Wallet {
        return Wallet(currency = Currency.getInstance(this.currencyCode),main =  this.mainWallet);
    }


}
