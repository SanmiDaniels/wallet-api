package com.sdssd.app.controller

import com.sdssd.app.dto.UserDto
import com.sdssd.app.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.net.http.HttpResponse
import javax.validation.Valid

@RestController
@RequestMapping("/user")
class UserController(val userService: UserService) {

    @PostMapping
    fun createUser(@Valid @RequestBody newUser: UserDto): ResponseEntity<String> {
        return if (userService.createUser(newUser)){
            ResponseEntity<String>("Created", HttpStatus.CREATED);
        }else ResponseEntity<String>("Already exists", HttpStatus.CONFLICT)
    }









}
