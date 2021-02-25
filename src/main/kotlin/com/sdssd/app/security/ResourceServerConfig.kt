package com.sdssd.app.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler
import org.springframework.web.cors.CorsConfiguration
import java.util.List
import javax.servlet.http.HttpServletRequest


@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
class ResourceServerConfig : ResourceServerConfigurerAdapter() {

    @Bean
    fun encoder(): PasswordEncoder {
        return BCryptPasswordEncoder();
    }

    override fun configure(resources: ResourceServerSecurityConfigurer) {
        resources.resourceId("resource_id").stateless(false)
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.cors().configurationSource { request: HttpServletRequest? ->
            val cors = CorsConfiguration()
            cors.allowedOrigins = List.of("http://localhost:8080")
            cors.allowedMethods = List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
            cors.allowedHeaders = List.of("*")
            cors
        }.and().exceptionHandling().accessDeniedHandler(OAuth2AccessDeniedHandler())
        .and().authorizeRequests().anyRequest().permitAll();
    }

}
