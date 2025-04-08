package com.hebe.hebemanyepxa.dto

import java.math.BigDecimal

data class ApparelFormDto(
    val id: Long = 0,
    val title: String = "",
    val slug: String = "",
    val description: String = "",
    val price: BigDecimal = BigDecimal.ZERO,
    val isFeatured: Boolean = false,
    val isAvailable: Boolean = true
)