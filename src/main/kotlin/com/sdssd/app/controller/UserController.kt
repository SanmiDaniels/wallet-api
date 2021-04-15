package com.sdssd.app.controller

import com.sdssd.app.dto.UserDto
import com.sdssd.app.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.net.http.HttpResponse
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/user")
@Tag(name = "User Controller", description = "All endpoints related to users")
class UserController(val userService: UserService) {

    @PostMapping
    @PreAuthorize("isAnonymous()")
    @Operation(summary = "Create a new user")
    @ApiResponses(value = [ApiResponse(responseCode =  "201", description = "User successfully created"), ApiResponse(responseCode =  "406", description = "User already exists")])
    fun createUser(@Valid @RequestBody @Parameter(description = "User to be created")  newUser: UserDto): ResponseEntity<String> {
        return if (userService.createUser(newUser)){
            ResponseEntity<String>("Created", HttpStatus.CREATED);
        }else ResponseEntity<String>("Already exists", HttpStatus.CONFLICT)
    }

    @PostMapping("/get-wallets")
    @PreAuthorize("hasAnyRole('NOOB', 'ELITE')")
    @Operation(summary = "Get all wallets for user", description = "Users use this to check the remaining balance of their wallet")
    @ApiResponses(value = [ApiResponse(responseCode =  "200", description = "All wallets")])
    fun getWallets() :ResponseEntity<Any>{

        var useremail = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity<Any>(userService.getUserByEmail(useremail).get().wallets, HttpStatus.OK)

    }







}
