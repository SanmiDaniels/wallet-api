package com.sdssd.app.dto

import com.sdssd.app.model.Wallet
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*
import javax.validation.constraints.Pattern

class WalletDto (


        @Pattern(regexp = "^(GBP|USD|EUR|NGN|GHS)$", message = "This currency is not supported")
        @Schema(description = "Currency code of walletr")
        val currencyCode: String,

        @Schema(description = "Show is wallet is user's main")
        val mainWallet: Boolean

    ){

    fun toWallet(): Wallet {
        return Wallet(currency = Currency.getInstance(this.currencyCode),main =  this.mainWallet);
    }


}
