package com.sdssd.app.repository

import com.sdssd.app.model.Wallet
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface WalletRepository: JpaRepository<Wallet, UUID> {

    @Query(value = "select count(*) from wallet where user_email = :email", nativeQuery = true)
    fun numberOfWallets(@Param("email") email: String): Int;


}
