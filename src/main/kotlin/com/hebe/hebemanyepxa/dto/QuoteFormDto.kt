package com.hebe.hebemanyepxa.dto

data class QuoteFormDto(
    val id: Long = 0,
    val quoteText: String = "",
    val author: String = "",
    val category: String? = null,
    val isFeatured: Boolean = false
)