package com.hebe.hebemanyepxa.dto

data class BookListDto(
    val id: Long,
    val title: String,
    val author: String,
    val publishYear: Int?,
    val isFeatured: Boolean,
    val coverImagePath: String? = null
)