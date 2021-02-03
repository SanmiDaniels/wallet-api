package com.sdssd.app.controller


import com.sdssd.app.dto.UserDto
import com.sdssd.app.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/user")
class UserController(val userService: UserService) {

    @PostMapping
    fun createUser(@Valid @RequestBody newUser: UserDto): UserDto {
        return newUser;
    }





}
