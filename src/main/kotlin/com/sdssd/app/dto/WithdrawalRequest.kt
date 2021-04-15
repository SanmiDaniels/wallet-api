package com.sdssd.app.dto

import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.Pattern

class WithdrawalRequest (

        @Pattern(regexp = "^(GBP|USD|EUR|NGN|GHS)$", message = "This currency is not supported")
        @Schema(description = "Currency to be withdrawn")
        val currency: String,
        @Schema(description = "Amount to be withdrawn")
        val amount: Double
        ){
}
