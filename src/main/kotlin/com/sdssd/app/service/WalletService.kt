package com.sdssd.app.service

import com.sdssd.app.dto.FundRequest
import com.sdssd.app.dto.WalletDto
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
class WalletService(val walletRepository: WalletRepository,
                    val userService: UserService, val conversionApiService: ConversionApiService) {


    fun createWallet(walletDto: WalletDto, email: String): Any {
        val wallet = walletDto.toWallet();
        wallet.user = userService.getUserByEmail(email);
       return  walletRepository.save(wallet);

    }

    fun fundUser(useremail: String, fundReq: FundRequest) {

        val userToBeFunded =  userService.getUserByEmail(useremail);

        if (userToBeFunded.userType == "NOOB"){
          var conversionResponse =   conversionApiService.convert(fromCurrency = userToBeFunded.wallets.elementAt(0).currency,
                    toCurrency = Currency.getInstance(fundReq.currency),amount = fundReq.amount )
            print(conversionResponse.toString())
        }

    }

}
