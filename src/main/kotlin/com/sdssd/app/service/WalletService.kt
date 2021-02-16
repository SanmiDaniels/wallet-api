package com.sdssd.app.service

import com.sdssd.app.dto.FundRequest
import com.sdssd.app.dto.RatesResponse
import com.sdssd.app.dto.WalletDto
import com.sdssd.app.dto.WithdrawalRequest
import com.sdssd.app.enums.TransactionType
import com.sdssd.app.enums.UserType
import com.sdssd.app.model.Transaction
import com.sdssd.app.model.User
import com.sdssd.app.model.Wallet
import com.sdssd.app.repository.WalletRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Service
@Transactional
class WalletService(val walletRepository: WalletRepository, val transactionService: TransactionService,
                    val userService: UserService, val ratesResponse: RatesResponse) {


    fun createWallet(walletDto: WalletDto, email: String): Any {
        val wallet = walletDto.toWallet()
        wallet.user = userService.getUserByEmail(email)
        return  walletRepository.save(wallet)

    }

    fun fundUser(toUserEmail: String, fundReq: FundRequest, fromUserEmail: String? = null ): Boolean {

        val userToBeFunded =  userService.getUserByEmail(toUserEmail)

        if(userToBeFunded.wallets.isEmpty()) return false

        val fromUser = fromUserEmail?.let { userService.getUserByEmail(it) }
        val fromWallet = fromUser?.wallets?.filter { wallet -> ( wallet.currency == Currency.getInstance(fundReq.currency))}

        if (fromWallet != null && fromWallet.isEmpty()) return false

        if (userToBeFunded.userType == UserType.NOOB.name){
            fundNoobUser(userToBeFunded, fromUser, fromWallet, fundReq)
        }

        if (userToBeFunded.userType == UserType.ELITE.name){
            fundEliteUser(userToBeFunded, fromUser, fromWallet, fundReq)
        }

        return true
    }


    fun withdrawAmount(withdraw: WithdrawalRequest, useremail: String): Any {

        val user =  userService.getUserByEmail(useremail)
        if (user.userType == UserType.NOOB.name) return withdrawFromNoobUser(user, withdraw)

        if (user.userType == UserType.ELITE.name) return withdrawFromEliteUser(user, withdraw)

        return "Unknown user type"
    }

    fun fundNoobUser(userToBeFunded: User, fromUser: User?, fromWallet: List<Wallet>?, fundReq: FundRequest) {
        var convertedAmount: Float = (fundReq.amount / ratesResponse.rates.getCurrencyRate(fundReq.currency))*(ratesResponse.rates.getCurrencyRate(userToBeFunded.wallets.elementAt(0).currency?.currencyCode!!))
        transactionService.saveTransaction(
                Transaction(toWallet = userToBeFunded.wallets.elementAt(0),
                        transactionType = fromUser?.let { TransactionType.TRANSFER.name } ?: TransactionType.FUND.name,
                        amount = convertedAmount, initiatedOn = userToBeFunded, fromWallet = fromWallet?.elementAt(0),
                        initiatedBy = fromUser?.let { it } ?: userToBeFunded))
    }

    fun fundEliteUser(userToBeFunded: User, fromUser: User?, fromWallet: List<Wallet>?, fundReq: FundRequest) {

        val walletWithSameCurrency = userToBeFunded.wallets.find{wallet -> ( wallet.currency == Currency.getInstance(fundReq.currency)) }
        walletWithSameCurrency?.let {  transactionService.saveTransaction(
                Transaction(toWallet = it,
                        transactionType = fromUser?.let { TransactionType.TRANSFER.name } ?: TransactionType.FUND.name,
                        amount = fundReq.amount, initiatedOn = userToBeFunded, fromWallet = fromWallet?.elementAt(0),
                        initiatedBy = fromUser?.let { fromUser } ?: userToBeFunded)) } ?: run {
                        val newWallet = Wallet(currency = Currency.getInstance(fundReq.currency), main = false)
                        newWallet.user = userToBeFunded

                        transactionService.saveTransaction(
                                Transaction(toWallet = walletRepository.save(newWallet),
                                        transactionType = fromUser?.let { TransactionType.TRANSFER.name }
                                                ?: TransactionType.FUND.name,
                                        amount = fundReq.amount, initiatedOn = userToBeFunded, fromWallet = fromWallet?.elementAt(0),
                                        initiatedBy = fromUser?.let { fromUser } ?: userToBeFunded))
        }



    }


    fun withdrawFromNoobUser(user: User, withdraw: WithdrawalRequest): Any{

        var convertedAmount: Float = (withdraw.amount / ratesResponse.rates.getCurrencyRate(withdraw.currency))*(ratesResponse.rates.getCurrencyRate(user.wallets.elementAt(0).currency?.currencyCode!!))

        transactionService.saveTransaction(
                Transaction(fromWallet = user.wallets.elementAt(0), transactionType = TransactionType.WITHDRAW.name,
                        amount = convertedAmount, initiatedOn = user, initiatedBy = user))

        return "Done"
    }

    fun withdrawFromEliteUser(user: User, withdraw: WithdrawalRequest): Any{

        val walletWithSameCurrency = user.wallets.find{wallet -> ( wallet.currency == Currency.getInstance(withdraw.currency)) }

        walletWithSameCurrency?.let { transactionService.saveTransaction(
                Transaction(fromWallet = walletWithSameCurrency, transactionType = TransactionType.WITHDRAW.name,
                        amount = withdraw.amount, initiatedOn = user, initiatedBy = user)) } ?: run {

        var mainWallet = user.wallets.find{wallet -> ( wallet.isMain)};
        var convertedAmount: Float = (withdraw.amount / ratesResponse.rates.getCurrencyRate(withdraw.currency))*(ratesResponse.rates.getCurrencyRate(mainWallet?.currency?.currencyCode!!))
            transactionService.saveTransaction(
                    Transaction(fromWallet = mainWallet, transactionType = TransactionType.WITHDRAW.name,
                            amount = convertedAmount, initiatedOn = user, initiatedBy = user))
        }


        return "Done"
    }


}
