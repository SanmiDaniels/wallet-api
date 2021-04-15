package com.sdssd.app.model

import io.swagger.v3.oas.annotations.media.Schema
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.Pattern

@Entity
class User(
        @Id
        @Email
        @Schema(description = "Email address of user. Serves as a unique identifier")
        val email: String,

        @Schema(description = "Password of user")
        val password: String,

        @Schema(description = "Category of user")
        var userType: String,

        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "user",fetch = FetchType.LAZY)
           var wallets: MutableSet<Wallet> = HashSet(),

        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "initiatedBy",fetch = FetchType.LAZY)
           var initiatedBy: MutableSet<Transaction> = HashSet(),

        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "initiatedOn",fetch = FetchType.LAZY)
           var initiatedOn: MutableSet<Transaction> = HashSet()

           )
