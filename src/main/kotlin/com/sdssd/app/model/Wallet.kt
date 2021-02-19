package com.sdssd.app.model
import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.GenericGenerator
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import javax.persistence.*


@Entity
class Wallet {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(columnDefinition = "BINARY(16)")
        var id: UUID? = null

        @ManyToOne
        @JsonIgnore
        @JoinColumn(name = "user_email")
        var user: User? = null
        var currency: Currency? = null
        var balance: BigDecimal? = BigDecimal.valueOf(0)
        var isMain = false

        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "toWallet",fetch = FetchType.LAZY)
        var toTransactions: MutableSet<Transaction> = HashSet()

        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "fromWallet",fetch = FetchType.LAZY)
        var fromTransactions: MutableSet<Transaction> = HashSet()

        constructor() {}
        constructor(currency: Currency?, main: Boolean) {
                this.currency = currency
                isMain = main
        }
}
