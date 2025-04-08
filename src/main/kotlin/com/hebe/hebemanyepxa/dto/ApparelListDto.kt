package com.hebe.hebemanyepxa.dto

import java.math.BigDecimal

data class ApparelListDto(
    val id: Long,
    val title: String,
    val price: BigDecimal,
    val isFeatured: Boolean,
    val isAvailable: Boolean,
    val primaryImagePath: String? = null
)