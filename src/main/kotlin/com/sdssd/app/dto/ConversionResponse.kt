package com.sdssd.app.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
class ConversionResponse(
        val success: Boolean?,
        val query: Query?,
        val info: Info?,
        val historical: String?,
        val date: Date?,
        val result: Float?
) {

    override fun toString(): String {
        return "ConversionResponse(success=$success, query=$query, info=$info, historical=$historical, date=$date, result=$result)"
    }
}

class Info(
        val timpestamp: Int?,
        val rate: Float?,
){

    override fun toString(): String {
        return "Info(timpestamp=$timpestamp, rate=$rate)"
    }
}

class Query(
         val from: String?,
         val to: String?,
         val amount: Float?
){
    override fun toString(): String {
        return "Query(from=$from, to=$to, amount=$amount)"
    }
}

