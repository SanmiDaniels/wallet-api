package com.sdssd.app.controller


import com.sdssd.app.service.AdminService
import com.sdssd.app.service.TransactionService
import com.sdssd.app.service.WalletService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin Controller", description = "All APIs related to admin operations and endpoints")
class AdminController(val adminService: AdminService,val walletService: WalletService,
                      val transactionService: TransactionService) {

    @PostMapping("/change-main-wallet")
    @Operation(summary = "Changes main wallet of user", description = "This endpoint changes the main wallet of a user from a particular wallet to another wallet")
    @ApiResponses(value = [ApiResponse(responseCode =  "200", description = "Main wallet has been successfully changed"), ApiResponse(responseCode = "400", description = "One of the parameters may have been wrong"),])
    fun changeMainWallet(@RequestParam @Parameter(description = "Email of user whose main wallet is to be changed") useremail: String,
                         @RequestParam @Parameter(description = "Id of wallet to be changed to main") toWalletId: UUID): ResponseEntity<Any> {

        return if (adminService.changeMainWallet(useremail, toWalletId)){
            return ResponseEntity<Any>("Successful", HttpStatus.OK)
        } else ResponseEntity<Any>("It appear your user email or wallet id is incorrect", HttpStatus.BAD_REQUEST)

    }

    @PostMapping("/approve-transaction")
    @Operation(summary = "Approves transactions for users", description = "For transactions made by users of NOOB category, this endpoint is used to approve these transactions")
    @ApiResponses(value = [ApiResponse(responseCode =  "200", description = "Transaction has been approved"),])
    fun approveTransaction(@RequestParam @Parameter(description = "Id of transaction to be approved") transactionId: UUID): ResponseEntity<Any>{

        adminService.approveTransaction(transactionId);
        return ResponseEntity<Any>("Successful", HttpStatus.OK)
    }

    @PostMapping("/promote-user")
    @Operation(summary = "Promotes User", description = "This endpoint can be used to promote a user from one level to another, from NOOB to Elite or from ELITE to ADMIN. An ELITE user promoted to ADMIN will lose his wallets ")
    @ApiResponses(value = [ApiResponse(responseCode =  "200", description = "User has been successfully promoted"),])
    fun promoteUser(@RequestParam @Parameter(description = "Email of user to be promoted") useremail: String): ResponseEntity<Any>{

        adminService.promoteUser(useremail)
        return ResponseEntity<Any>("Successful", HttpStatus.OK)
    }

    @PostMapping("/demote-user")
    @Operation(summary = "Demotes User", description = "This endpoint is used to demote a user from one level to level lower. A NOOB user cannot be demoted")
    @ApiResponses(value = [ApiResponse(responseCode =  "200", description = "User has been successfully demoted"),])
    fun demoteUser(@RequestParam @Parameter(description = "Email of user to be demoted") useremail: String): ResponseEntity<Any>{

        adminService.demoteUser(useremail)
        return ResponseEntity<Any>("Successful", HttpStatus.OK)
    }

    @GetMapping("/get-transactions")
    @Operation(summary = "Get all transactions", description = "Returns all transactions carried out by all users")
    @ApiResponses(value = [ApiResponse(responseCode =  "200", description = "List of all transactions"),])
    fun getTransactions(): ResponseEntity<Any> {
        return ResponseEntity<Any>(transactionService.getTransactions(), HttpStatus.OK)
    }

    @GetMapping("/get-wallets")
    @Operation(summary = "Get all wallets", description = "Returns all wallets owned by users")
    @ApiResponses(value = [ApiResponse(responseCode =  "200", description = "List of all user wallets"),])
    fun getWallets(): ResponseEntity<Any> {
        return ResponseEntity<Any>(walletService.getAllWalltes(), HttpStatus.OK)
    }


}
