package com.sdssd.app.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Pattern

@Entity
class Transaction(

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: UUID? = null,

        @ManyToOne
        @JsonIgnore
        @JoinColumn(name = "onWallet_id")
        var toWallet: Wallet? = null,

        @ManyToOne
        @JsonIgnore
        @JoinColumn(name = "byWallet_id")
        var fromWallet: Wallet? = null,

        @Pattern(regexp = "^(FUNDING|WITHDRAWAL)$", message = "Invalid Transaction Type")
        var transactionType: String? = null,

        var amount: Float? = null,

        @ManyToOne
        @JsonIgnore
        @JoinColumn(name = "initiated_by")
        var initiatedBy: User? = null,

        @ManyToOne
        @JsonIgnore
        @JoinColumn(name = "initiated_on")
        var initiatedOn: User? = null,

        var approved: Boolean? = false,

){


}
