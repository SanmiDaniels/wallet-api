package com.sdssd.app.model
import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.GenericGenerator
import java.math.BigInteger
import java.util.*
import javax.persistence.*


@Entity
class Wallet {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: UUID? = null

        @ManyToOne
        @JsonIgnore
        @JoinColumn(name = "user_email")
        var user: User? = null
        var currency: Currency? = null
        var balance: BigInteger? = BigInteger.valueOf(0)
        var isMain = false

        constructor() {}
        constructor(currency: Currency?, main: Boolean) {
                this.currency = currency
                isMain = main
        }
}
