package com.sdssd.app.model
import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.v3.oas.annotations.media.Schema
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
        @Schema( description = "Unique ID of wallet")
        var id: UUID? = null

        @ManyToOne
        @JsonIgnore
        @JoinColumn(name = "user_email")
        @Schema(description = "User that owns the wallet")
        var user: User? = null
        @Schema(description = "Currency the wallet holds")
        var currency: Currency? = null
        @Schema(description = "Balance of the wallet")
        var balance: BigDecimal? = BigDecimal.valueOf(0)
        @Schema(description = "Shows if wallet is user's main or not")
        var isMain = false

        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "toWallet",fetch = FetchType.LAZY)
        @Schema(description = "All transactions carried out to the wallet")
        var toTransactions: MutableSet<Transaction> = HashSet()

        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "fromWallet",fetch = FetchType.LAZY)
        @Schema(description = "All transactions carried out from the wallet")
        var fromTransactions: MutableSet<Transaction> = HashSet()

        constructor() {}
        constructor(currency: Currency?, main: Boolean) {
                this.currency = currency
                isMain = main
        }
}
