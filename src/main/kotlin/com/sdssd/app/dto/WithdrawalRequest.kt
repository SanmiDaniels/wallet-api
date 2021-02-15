package com.sdssd.app.dto

import javax.validation.constraints.Pattern

class WithdrawalRequest (

        @Pattern(regexp = "^(GBP|USD|EUR|NGN|GHS)$", message = "This currency is not supported")
        val currency: String,
        val amount: Float
        ){
}
