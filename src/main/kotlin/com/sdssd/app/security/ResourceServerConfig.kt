package com.sdssd.app.security

import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler
import org.springframework.web.cors.CorsConfiguration
import java.util.List
import javax.servlet.http.HttpServletRequest


@Configuration
@EnableResourceServer
class ResourceServerConfig : ResourceServerConfigurerAdapter() {
    private val PERMIT_ALL_GET_REQUEST = arrayOf<String>("/user")
    private val PERMIT_ALL_REQUEST = arrayOf<String>("/user")
    private val PERMIT_ALL_POST_REQUEST = arrayOf<String>("/test")
    private val PERMIT_NOOB_GET_REQUEST = arrayOf<String>("/test")
    private val PERMIT_NOOB_POST_REQUEST = arrayOf<String>("/test")
    private val PERMIT_ELITE_GET_REQUEST = arrayOf<String>("/test")
    private val PERMIT_ELITE_POST_REQUEST = arrayOf<String>("/test")
    private val PERMIT_ADMIN_REQUEST = arrayOf<String>("/test")

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
        }.and().authorizeRequests().antMatchers(*PERMIT_ALL_REQUEST).permitAll()
                .antMatchers(HttpMethod.GET, *PERMIT_ALL_GET_REQUEST).permitAll()
                .antMatchers(HttpMethod.POST, *PERMIT_ALL_POST_REQUEST).permitAll()
                .antMatchers(HttpMethod.GET, *PERMIT_NOOB_GET_REQUEST).access("hasRole('NOOB')")
                .antMatchers(HttpMethod.POST, *PERMIT_NOOB_POST_REQUEST).access("hasRole('NOOB')")
                .antMatchers(HttpMethod.GET, *PERMIT_ELITE_GET_REQUEST).access("hasRole('ELITE')")
                .antMatchers(HttpMethod.PUT, *PERMIT_ELITE_POST_REQUEST).access("hasRole('ELITE')")
                .antMatchers(*PERMIT_ADMIN_REQUEST).access("hasRole('ADMIN')").and().exceptionHandling()
                .accessDeniedHandler(OAuth2AccessDeniedHandler())
    }

}
