package com.sdssd.app.model

import java.math.BigInteger
import java.util.*
import javax.persistence.*

@Entity
class Wallet(

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: UUID,

        @ManyToOne
        @JoinColumn(name = "user_email")
        val user: User,

        val currency: Currency,

        val balance: BigInteger,

        val mainWallet: Boolean
) {



}
