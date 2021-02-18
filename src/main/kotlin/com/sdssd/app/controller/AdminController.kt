package com.sdssd.app.controller


import com.sdssd.app.service.AdminService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/admin")
class AdminController(val adminService: AdminService) {

    @PostMapping("/change-main-wallet")
    fun changeMainWallet(@RequestParam useremail: String, @RequestParam toWalletId: UUID): ResponseEntity<Any> {

        return if (adminService.changeMainWallet(useremail, toWalletId)){
            return ResponseEntity<Any>("Successful", HttpStatus.OK)
        } else ResponseEntity<Any>("It appear your user email or wallet id is incorrect", HttpStatus.BAD_REQUEST)

    }

    @PostMapping("/approve-transaction")
    fun approveTransaction(@RequestParam transactionId: UUID): ResponseEntity<Any>{

        adminService.approveTransaction(transactionId);
        return ResponseEntity<Any>("Successful", HttpStatus.OK)
    }

    @PostMapping("/promote-user")
    fun promoteUser(@RequestParam useremail: String): ResponseEntity<Any>{

        adminService.promoteUser(useremail)
        return ResponseEntity<Any>("Successful", HttpStatus.OK)
    }

    @PostMapping("/demote-user")
    fun demoteUser(@RequestParam useremail: String): ResponseEntity<Any>{

        adminService.demoteUser(useremail)
        return ResponseEntity<Any>("Successful", HttpStatus.OK)
    }






}
