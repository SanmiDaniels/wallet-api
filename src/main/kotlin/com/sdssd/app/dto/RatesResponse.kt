package com.sdssd.app.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
class RatesResponse(
        val success: Boolean?,
        val timpestamp: Int?,
        val base: String?,
        val date: Date?,
        val rates: Rate
) {
    override fun toString(): String {
        return "ConversionResponse(success=$success, timpestamp=$timpestamp, base=$base, date=$date, rates=$rates)"
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Rate(
        val USD: Float,
        val GBP: Float,
        val EUR: Float,
        val NGN: Float,
        val GHS: Float,
){

    fun getCurrencyRate(currency: String): Float{
        return when(currency){
            "USD" -> this.USD;
            "GBP" -> this.GBP;
            "EUR" -> this.EUR;
            "NGN" -> this.NGN;
            "GHS" -> this.GHS
            else -> {
                throw Exception("This currency is no supported")
            };
        }
    }


    override fun toString(): String {
        return "Rate(USD=$USD, GBP=$GBP, EUR=$EUR, NGN=$NGN, GHS=$GHS)"
    }
}



