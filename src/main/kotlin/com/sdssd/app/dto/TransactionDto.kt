package com.sdssd.app.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigInteger
import java.util.*

class TransactionDto(

        @Schema(description = "")
        val transactionRef: UUID?,
        @Schema(description = "Type of transaction")
        val transactionType: String?,
        @Schema(description = "ID of wallet transaction is done from")
        val fromWalletId: UUID? =  null,
        @Schema(description = "ID of wallet transaction done into")
        val toWalletId: UUID? = null,
        @Schema(description = "Shows if transaction is approved")
        val approved: Boolean?,
        @Schema(description = "Amount of transaction")
        val amount: Double?

        ) {
}
