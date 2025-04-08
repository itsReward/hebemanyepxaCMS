package com.hebe.hebemanyepxa.dto

data class BookFormDto(
    val id: Long = 0,
    val title: String = "",
    val slug: String = "",
    val description: String = "",
    val author: String = "",
    val publishYear: Int? = null,
    val bookLink: String? = null,
    val isFeatured: Boolean = false
)