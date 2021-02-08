package com.sdssd.app.controller

import com.sdssd.app.dto.WalletDto
import com.sdssd.app.service.UserService
import com.sdssd.app.service.WalletService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/wallet")
class WalletController(val userService: UserService, val walletService: WalletService) {

    @PostMapping("/create/{email}")
    fun createWalletForUser(@PathVariable email:String, @RequestBody wallet: WalletDto): ResponseEntity<Any> {
        return if(userService.canAddWallet(email))
            ResponseEntity<Any>("User cannot create more than one wallet", HttpStatus.FORBIDDEN);
        else ResponseEntity<Any>(walletService.createWallet(wallet), HttpStatus.OK);
    }









}
