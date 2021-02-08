package com.sdssd.app.model

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.validation.constraints.Email

@Entity
class User(
           @Id
           @Email
           val email: String,

           val password: String,

           val userType: String,

           @OneToMany(cascade = [CascadeType.ALL], mappedBy = "user")
           var wallets: MutableSet<Wallet> = HashSet()

           )
