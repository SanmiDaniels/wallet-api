package com.sdssd.app.service

import com.sdssd.app.dto.FundRequest
import com.sdssd.app.dto.RatesResponse
import com.sdssd.app.dto.WalletDto
import com.sdssd.app.dto.WithdrawalRequest
import com.sdssd.app.enums.TransactionType
import com.sdssd.app.model.Transaction
import com.sdssd.app.repository.UserRepository
import com.sdssd.app.repository.WalletRepository
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import java.util.*


@Service
@Transactional
class WalletService(val walletRepository: WalletRepository, val transactionService: TransactionService,
                    val userService: UserService, val ratesResponse: RatesResponse) {


    fun createWallet(walletDto: WalletDto, email: String): Any {
        val wallet = walletDto.toWallet();
        wallet.user = userService.getUserByEmail(email);
       return  walletRepository.save(wallet);

    }

    fun fundUser(toUserEmail: String, fundReq: FundRequest, fromUserEmail: String? = null ) {

        val userToBeFunded =  userService.getUserByEmail(toUserEmail);

        val fromUser = fromUserEmail?.let { userService.getUserByEmail(it) };

        if (userToBeFunded.userType == "NOOB"){

            var convertedAmount: Float = (fundReq.amount / ratesResponse.rates.getCurrencyRate(fundReq.currency))*(ratesResponse.rates.getCurrencyRate(userToBeFunded.wallets.elementAt(0).currency?.currencyCode!!))
            transactionService.saveTransaction(
                    Transaction(wallet = userToBeFunded.wallets.elementAt(0), transactionType = TransactionType.FUND.name,
                                amount = convertedAmount, initiatedOn = userToBeFunded,
                            initiatedBy = fromUserEmail?.let { fromUser } ?: userToBeFunded))
        }

    }

    fun withdrawAmount(withdraw: WithdrawalRequest, useremail: String) {

        val user =  userService.getUserByEmail(useremail);

        if (user.userType == "NOOB"){

            var convertedAmount: Float = (withdraw.amount / ratesResponse.rates.getCurrencyRate(withdraw.currency))*(ratesResponse.rates.getCurrencyRate(user.wallets.elementAt(0).currency?.currencyCode!!))
            transactionService.saveTransaction(
                    Transaction(wallet = user.wallets.elementAt(0), transactionType = TransactionType.WITHDRAW.name,
                            amount = convertedAmount, initiatedOn = user, initiatedBy = user))
        }
    }

}
