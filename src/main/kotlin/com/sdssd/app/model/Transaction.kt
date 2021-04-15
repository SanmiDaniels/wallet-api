package com.sdssd.app.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.sdssd.app.dto.TransactionDto
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Pattern

@Entity
class Transaction(

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(columnDefinition = "BINARY(16)")
        @Schema( description = "Id of transaction. Will be auto generated")
        var id: UUID? = null,

        @ManyToOne
        @JsonIgnore
        @JoinColumn(name = "toWallet_id")
        @Schema( description = "Wallet to be transferred into")
        var toWallet: Wallet? = null,

        @ManyToOne
        @JsonIgnore
        @JoinColumn(name = "fromWallet_id")
        @Schema(description = "Wallet to be transferred from. Can be null for withdrawals")
        var fromWallet: Wallet? = null,

        @Pattern(regexp = "^(FUNDING|WITHDRAWAL)$", message = "Invalid Transaction Type")
        @Schema(example = "FUNDING, WITHDRAWAL", description = "The type of the transaction")
        var transactionType: String? = null,

        @Schema(example = "10", description = "Amount of the transaction")
        var amount: Double? = null,

        @ManyToOne
        @JsonIgnore
        @JoinColumn(name = "initiated_by")
        @Schema(description = "Owner of wallet that made the transaction.")
        var initiatedBy: User? = null,

        @ManyToOne
        @JsonIgnore
        @JoinColumn(name = "initiated_on")
        @Schema(description = "Owner of wallet that is receiving the transaction")
        var initiatedOn: User? = null,

        @Schema( description = "Shows if transaction has been approved and carried out")
        var approved: Boolean? = false,

){

        fun toDto(): TransactionDto{
                return TransactionDto(this.id,this.transactionType, fromWallet?.id, toWallet?.id, this.approved, this.amount  )
        }

}
