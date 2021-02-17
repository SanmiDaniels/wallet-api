package com.sdssd.app.controller


import com.sdssd.app.service.AdminService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.*

@Controller
@RequestMapping("/admin")
class AdminController(val adminService: AdminService) {

    @PostMapping("/change-main-wallet")
    fun changeMainWallet(@RequestParam useremail: String, @RequestParam toWalletId: UUID): ResponseEntity<Any> {

        return if (adminService.changeMainWallet(useremail, toWalletId)){
            return ResponseEntity<Any>("Successful", HttpStatus.OK)
        } else ResponseEntity<Any>("It appear your user email or wallet id is incorrect", HttpStatus.BAD_REQUEST)

    }






}
