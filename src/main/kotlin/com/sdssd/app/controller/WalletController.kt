package com.sdssd.app.controller

import com.sdssd.app.dto.FundRequest
import com.sdssd.app.dto.WalletDto
import com.sdssd.app.dto.WithdrawalRequest
import com.sdssd.app.service.UserService
import com.sdssd.app.service.WalletService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/wallet")
@Tag(name = "Wallet Controller", description = "Endpoints for wallet operations")
class WalletController(val userService: UserService, val walletService: WalletService) {

    @PostMapping("/create/{email}")
    @Operation(summary = "Creates a new wallet for user", description = "This endpoint creates a new wallet for the user")
    @ApiResponses(value = [ApiResponse(responseCode =  "200", description = "Wallet has been created"),
        ApiResponse(responseCode = "401", description = "This user is not allowed to create new wallet due to category restrictions"),])
    fun createWalletForUser(@PathVariable @Parameter(description = "Email of user") email:String,
                            @RequestBody @Parameter(description = "Wallet to be created") wallet: WalletDto): ResponseEntity<Any> {
        return if(userService.canAddWallet(email, wallet))
            ResponseEntity<Any>("User cannot create wallet", HttpStatus.FORBIDDEN);
        else ResponseEntity<Any>(walletService.createWallet(wallet, email), HttpStatus.OK);
    }

    @PostMapping("/fund")
    @PreAuthorize("hasAnyRole('NOOB', 'ELITE')")
    @Operation(summary = "Fund a user wallet", description = "Users use this to fund their own wallet. Details come from a payment proccessor")
    @ApiResponses(value = [ApiResponse(responseCode =  "202", description = "Fund request has been accepted to be processed"),
        ApiResponse(responseCode = "406", description = "The user's wallet cannot be funded at this time"),])
    fun fundWallet(@RequestBody @Parameter(description = "Details of fund request") fundReq: FundRequest): ResponseEntity<Any> {

        var useremail = SecurityContextHolder.getContext().getAuthentication().getName();

        return if (walletService.fundUser(toUserEmail = useremail, fundReq =  fundReq)){
            ResponseEntity<Any>("Funded", HttpStatus.ACCEPTED)
        }else ResponseEntity<Any>("Transaction failed", HttpStatus.NOT_ACCEPTABLE)

    }

    @PostMapping("/fund/{email}")
    @PreAuthorize("hasAnyRole('NOOB', 'ELITE')")
    @Operation(summary = "Fund another user wallet", description = "Users use this to fund their anothers' user wallet. Funds are transferred from one user to another")
    @ApiResponses(value = [ApiResponse(responseCode =  "202", description = "Fund request has been accepted to be processed"),])
    fun fundUserWallet(@RequestBody @Valid @Parameter(description = "Details of fund request") fundReq: FundRequest, @PathVariable @Parameter(description = "Email of user to be funded") email: String): ResponseEntity<Any>{

        var useremail = SecurityContextHolder.getContext().getAuthentication().getName();
        val response = walletService.fundUser(toUserEmail = email, fundReq =  fundReq,fromUserEmail = useremail);

        return ResponseEntity<Any>(response, HttpStatus.ACCEPTED)
    }

    @PostMapping("/withdraw")
    @PreAuthorize("hasAnyRole('NOOB', 'ELITE')")
    @Operation(summary = "Withdraw from a wallet", description = "This endpoint is used to withdrawl funds from a wallet")
    @ApiResponses(value = [ApiResponse(responseCode =  "202", description = "Withdrawal request has been accepted to be processed")])
    fun widthdrawFromWallet(@RequestBody @Valid @Parameter(description = "Details of withdrawal request") withdraw: WithdrawalRequest):ResponseEntity<Any>{

        var useremail = SecurityContextHolder.getContext().getAuthentication().getName();
        val response = walletService.withdrawAmount(withdraw, useremail);

        return ResponseEntity<Any>(response, HttpStatus.ACCEPTED)
    }

    @PostMapping("/check-balance")
    @PreAuthorize("hasAnyRole('NOOB', 'ELITE')")
    @Operation(summary = "Check balance of a wallet", description = "Users use this to check the remaining balance of their wallet")
    @ApiResponses(value = [ApiResponse(responseCode =  "200", description = "Wallet details and balance"), ApiResponse(responseCode =  "406", description = "Wallet does not exist for user")])
    fun checkBalance(@RequestBody @Valid @Parameter(description = "ID of wallet to be checked") walletId: UUID): ResponseEntity<Any>{

        var useremail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userService.walletBelongsToUser(useremail, walletId)){
            return ResponseEntity<Any>(walletService.getWalletById(walletId).get(), HttpStatus.ACCEPTED)
        } else return ResponseEntity<Any>(HttpStatus.BAD_REQUEST)
    }


}
