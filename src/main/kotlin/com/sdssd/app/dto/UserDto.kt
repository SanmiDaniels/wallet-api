package com.sdssd.app.dto

import com.sdssd.app.model.User
import org.springframework.security.crypto.password.PasswordEncoder
import javax.validation.constraints.Pattern

class UserDto(
        val email: String ,
        val password: String,

        @field:Pattern(regexp = "^(NOOB|ELITE|ADMIN)$", message = "Invalid User Type")
        val userType: String,
) {

    fun toUser(encoder: PasswordEncoder) : User{
        return User(this.email, encoder.encode(this.password), this.userType)
    }
}
