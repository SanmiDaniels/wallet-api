package com.sdssd.app.service

import com.sdssd.app.model.User
import com.sdssd.app.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.*


@Service
class UserService(val userRepo: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        val user: Optional<User> = userRepo.findById(email);

        if(user.isPresent){
            return org.springframework.security.core.userdetails.User(user.get().email, user.get().password,
                    getAuthority(user.get().userType))
        }else throw UsernameNotFoundException("Invalid username or password.");

    }

    private fun getAuthority(auth: String): List<SimpleGrantedAuthority?>? {
        return Arrays.asList(SimpleGrantedAuthority("ROLE_$auth"))
    }

}
