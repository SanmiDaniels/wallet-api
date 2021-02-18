package com.sdssd.app.dto

import java.math.BigInteger
import java.util.*

class TransactionDto(

        val transactionRef: UUID?,
        val transactionType: String?,
        val fromWalletId: UUID? =  null,
        val toWalletId: UUID? = null,
        val approved: Boolean?,
        val amount: Double?

        ) {
}
