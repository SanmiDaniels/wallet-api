package com.sdssd.app.service

import com.sdssd.app.dto.*
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


    fun getWalletById(walletId: UUID): Optional<Wallet> {
      return walletRepository.findById(walletId);
    }

    fun createWallet(walletDto: WalletDto, email: String): Any {
        val wallet = walletDto.toWallet()
        val user = userService.getUserByEmail(email)
        if (user.isEmpty) return "The user you seek is no where to be found"
        wallet.user = user.get()
        return  walletRepository.save(wallet)

    }

    fun fundUser(toUserEmail: String, fundReq: FundRequest, fromUserEmail: String? = null ): Boolean {

        val user =  userService.getUserByEmail(toUserEmail)

        if(user.isEmpty() && user.get().wallets.isEmpty()) return false
        val userToBeFunded = user.get();

        val fromUser = fromUserEmail?.let { userService.getUserByEmail(it).get() }
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

        val user =  userService.getUserByEmail(useremail).get()
        if (user.userType == UserType.NOOB.name) return withdrawFromNoobUser(user, withdraw)

        if (user.userType == UserType.ELITE.name) return withdrawFromEliteUser(user, withdraw)

        return "Unknown user type"
    }

    fun fundNoobUser(userToBeFunded: User, fromUser: User?, fromWallet: List<Wallet>?, fundReq: FundRequest): TransactionDto {
        var convertedAmount: Double = ((fundReq.amount / ratesResponse.rates.getCurrencyRate(fundReq.currency))*(ratesResponse.rates.getCurrencyRate(userToBeFunded.wallets.elementAt(0).currency?.currencyCode!!))).toDouble()
        return transactionService.saveTransaction(
                    Transaction(toWallet = userToBeFunded.wallets.elementAt(0),
                            transactionType = fromUser?.let { TransactionType.TRANSFER.name } ?: TransactionType.FUND.name,
                            amount = convertedAmount, initiatedOn = userToBeFunded, fromWallet = fromWallet?.elementAt(0),
                            initiatedBy = fromUser?.let { it } ?: userToBeFunded)).toDto()
    }

    fun fundEliteUser(userToBeFunded: User, fromUser: User?, fromWallet: List<Wallet>?, fundReq: FundRequest): TransactionDto {

        val walletWithSameCurrency = userToBeFunded.wallets.find{wallet -> ( wallet.currency == Currency.getInstance(fundReq.currency)) }
        var transaction = Transaction()

        walletWithSameCurrency?.let {
            transaction =  transactionService.saveTransaction(
                                Transaction(toWallet = it,
                                        transactionType = fromUser?.let { TransactionType.TRANSFER.name } ?: TransactionType.FUND.name,
                                        amount = fundReq.amount, initiatedOn = userToBeFunded, fromWallet = fromWallet?.elementAt(0),
                                        initiatedBy = fromUser?.let { fromUser } ?: userToBeFunded)) } ?: run {

        val newWallet = Wallet(currency = Currency.getInstance(fundReq.currency), main = false)
        newWallet.user = userToBeFunded

            transaction =  transactionService.saveTransaction(
                                Transaction(toWallet = walletRepository.save(newWallet),
                                        transactionType = fromUser?.let { TransactionType.TRANSFER.name }
                                                ?: TransactionType.FUND.name,
                                        amount = fundReq.amount, initiatedOn = userToBeFunded, fromWallet = fromWallet?.elementAt(0),
                                        initiatedBy = fromUser?.let { fromUser } ?: userToBeFunded))
        }

        transactionService.makeTransaction(transaction);
        return transaction.toDto()
    }


    fun withdrawFromNoobUser(user: User, withdraw: WithdrawalRequest): Any{

        var convertedAmount: Double = (withdraw.amount / ratesResponse.rates.getCurrencyRate(withdraw.currency))*(ratesResponse.rates.getCurrencyRate(user.wallets.elementAt(0).currency?.currencyCode!!))

        return transactionService.saveTransaction(
                Transaction(fromWallet = user.wallets.elementAt(0), transactionType = TransactionType.WITHDRAW.name,
                        amount = convertedAmount, initiatedOn = user, initiatedBy = user)).toDto()

    }

    fun withdrawFromEliteUser(user: User, withdraw: WithdrawalRequest): Any{

        val walletWithSameCurrency = user.wallets.find{wallet -> ( wallet.currency == Currency.getInstance(withdraw.currency)) }
        var transaction = Transaction()

        walletWithSameCurrency?.let {
            transaction =  transactionService.saveTransaction(
                            Transaction(fromWallet = walletWithSameCurrency, transactionType = TransactionType.WITHDRAW.name,
                                    amount = withdraw.amount, initiatedOn = user, initiatedBy = user)) } ?: run {

        val mainWallet = user.wallets.find{ wallet -> ( wallet.isMain)};
        val convertedAmount: Double = (withdraw.amount / ratesResponse.rates.getCurrencyRate(withdraw.currency))*(ratesResponse.rates.getCurrencyRate(mainWallet?.currency?.currencyCode!!))
            transaction =  transactionService.saveTransaction(
                            Transaction(fromWallet = mainWallet, transactionType = TransactionType.WITHDRAW.name,
                                    amount = convertedAmount, initiatedOn = user, initiatedBy = user))
        }

        transactionService.makeTransaction(transaction);
        return transaction.toDto();
    }


}
