package com.sdssd.app.model

import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.Pattern

@Entity
class User(
           @Id
           @Email
           val email: String,

           val password: String,

           val userType: String,

           @OneToMany(cascade = [CascadeType.ALL], mappedBy = "user",fetch = FetchType.LAZY)
           var wallets: MutableSet<Wallet> = HashSet(),

           @OneToMany(cascade = [CascadeType.ALL], mappedBy = "initiatedBy",fetch = FetchType.LAZY)
           var initiatedBy: MutableSet<Transaction> = HashSet(),

           @OneToMany(cascade = [CascadeType.ALL], mappedBy = "initiatedOn",fetch = FetchType.LAZY)
           var initiatedOn: MutableSet<Transaction> = HashSet()

           )
