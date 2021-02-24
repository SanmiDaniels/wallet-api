package com.sdssd.app.controller


import com.sdssd.app.service.AdminService
import com.sdssd.app.service.TransactionService
import com.sdssd.app.service.WalletService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
class AdminController(val adminService: AdminService,val walletService: WalletService,
                      val transactionService: TransactionService) {

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

    @GetMapping("/get-transactions")
    fun getTransactions(): ResponseEntity<Any> {
        return ResponseEntity<Any>(transactionService.getTransactions(), HttpStatus.OK)
    }

    @GetMapping("/get-wallets")
    fun getWalletss(): ResponseEntity<Any> {
        return ResponseEntity<Any>(walletService.getAllWalltes(), HttpStatus.OK)
    }


}
