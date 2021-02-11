package com.sdssd.app.service

import com.sdssd.app.dto.ConversionResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.*

@Service
class ConversionApiService{


    @Autowired
    lateinit var restTemplate: RestTemplate;

    @Value("\${fixer.base-url}")
    lateinit var apiUrl: String;

    @Value("\${fixer.api-key}")
    lateinit var apiKey: String;

    fun convert(fromCurrency: Currency?, toCurrency: Currency?, amount: Float?): ConversionResponse? {
    return restTemplate.getForObject(
                apiUrl+ "convert?access_key="+apiKey+"&from="+fromCurrency?.currencyCode+"&to="
                        +toCurrency?.currencyCode+"&amount="+ amount, ConversionResponse::class.java)
    }

}


