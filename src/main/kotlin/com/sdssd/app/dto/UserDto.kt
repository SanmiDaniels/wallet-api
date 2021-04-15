package com.sdssd.app.dto

import com.sdssd.app.model.User
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.security.crypto.password.PasswordEncoder
import javax.validation.constraints.Pattern

class UserDto(
        @Schema(description = "Email of user")
        val email: String ,
        @Schema(description = "Password of user")
        val password: String,

        @field:Pattern(regexp = "^(NOOB|ELITE|ADMIN)$", message = "Invalid User Type")
        @Schema(description = "Type of user")
        val userType: String,
) {

    fun toUser(encoder: PasswordEncoder) : User{
        return User(this.email, encoder.encode(this.password), this.userType)
    }
}
