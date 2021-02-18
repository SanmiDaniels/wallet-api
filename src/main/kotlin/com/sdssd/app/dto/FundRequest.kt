package com.sdssd.app.dto

import javax.validation.constraints.Pattern

class FundRequest(

        @Pattern(regexp = "^(GBP|USD|EUR|NGN|GHS)$", message = "This currency is not supported")
        val currency: String,
        val amount: Double

) {
}
