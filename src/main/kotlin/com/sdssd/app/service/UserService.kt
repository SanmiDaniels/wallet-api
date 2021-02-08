package com.sdssd.app.service

import com.sdssd.app.dto.UserDto
import com.sdssd.app.model.User
import com.sdssd.app.repository.UserRepository
import com.sdssd.app.repository.WalletRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*


@Service
class UserService(val userRepo: UserRepository, val walletRepository: WalletRepository,
                  val passwordEncoder: PasswordEncoder) : UserDetailsService {


    fun createUser(newUser: UserDto) : Boolean{
        if(userRepo.existsById(newUser.email)){
            return false
        } else {
            userRepo.save(newUser.toUser(passwordEncoder))
            return true
        }
    }


    fun canAddWallet(email: String): Boolean{

        return !(userRepo.findById(email).get().userType == "NOOB" && walletRepository.numberOfWallets(email) > 0)
    }






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
