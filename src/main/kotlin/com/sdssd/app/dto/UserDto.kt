package com.sdssd.app.dto

import com.sdssd.app.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder

class UserDto(val email: String, val password: String, val userType: String) {


    fun toUser(encoder: PasswordEncoder) : User{
        return User(this.email, encoder.encode(this.password), this.userType)
    }


}
