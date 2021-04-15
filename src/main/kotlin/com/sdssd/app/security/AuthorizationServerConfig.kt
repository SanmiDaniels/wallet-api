package com.sdssd.app.security

import com.sdssd.app.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore

@Configuration
@EnableAuthorizationServer
class AuthorizationServerConfig(val authenticationManager: AuthenticationManager, val userService: UserService,
                                val accessTokenConverter: JwtAccessTokenConverter) : AuthorizationServerConfigurerAdapter() {


    val CLIENT_ID = "walletapp"
    val CLIENT_SECRET = "\$2y\$12\$JPJLzIQa/XvnWlA2TpA2LOI44auBnYNHRzyWqPoVrSdzK9fOdoqsy"
    val GRANT_TYPE_PASSWORD = "password"
    val AUTHORIZATION_CODE = "authorization_code"
    val REFRESH_TOKEN = "refresh_token"
    val IMPLICIT = "implicit"
    val SCOPE_READ = "read"
    val SCOPE_WRITE = "write"
    val TRUST = "trust"
    val ACCESS_TOKEN_VALIDITY_SECONDS = 3 * 60 * 60
    val FREFRESH_TOKEN_VALIDITY_SECONDS = 6 * 60 * 60


    @Bean
    fun tokenStore(): TokenStore {
        return JwtTokenStore(accessTokenConverter)
    }

    @Throws(Exception::class)
    override fun configure(configurer: ClientDetailsServiceConfigurer) {
        configurer.inMemory()
                    .withClient(CLIENT_ID)
                    .secret(CLIENT_SECRET)
                    .authorizedGrantTypes(GRANT_TYPE_PASSWORD, AUTHORIZATION_CODE, REFRESH_TOKEN, IMPLICIT)
                    .scopes(SCOPE_READ, SCOPE_WRITE, TRUST)
                    .accessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS)
                    .refreshTokenValiditySeconds(FREFRESH_TOKEN_VALIDITY_SECONDS)
    }


    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
        endpoints
                .tokenStore(tokenStore())
                .accessTokenConverter(accessTokenConverter)
                .authenticationManager(authenticationManager)
                .userDetailsService(userService)
    }

}
