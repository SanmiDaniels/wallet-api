package com.sdssd.app.dto

import com.sdssd.app.model.User

class UserDto(val email: String, val password: String, val uerType: String) {


    fun toUser() : User{
        return User(this.email, this.password, this.uerType)
    }

}
