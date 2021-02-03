package com.sdssd.app.model

import javax.persistence.Entity
import javax.persistence.Id
import javax.validation.constraints.Email

@Entity
class User(
           @Id
           @Email
           val email: String,

           val password: String,

           val userType: String

           )
