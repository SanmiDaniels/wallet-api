package com.sdssd.app.service

import com.sdssd.app.dto.RatesResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.*

@Service
class RatesService{


    @Autowired
    lateinit var restTemplate: RestTemplate;

    @Value("\${fixer.base-url}")
    lateinit var apiUrl: String;

    @Value("\${fixer.api-key}")
    lateinit var apiKey: String;

    @Bean
    fun convert(): RatesResponse? {
    return restTemplate.getForObject(
                apiUrl+ "latest?access_key="+apiKey+"&symbols=GBP,USD,EUR,NGN,GHS", RatesResponse::class.java)
    }

}


