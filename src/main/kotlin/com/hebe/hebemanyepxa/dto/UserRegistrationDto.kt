package com.hebe.hebemanyepxa.dto

data class UserRegistrationDto(
    val username: String,
    val password: String,
    val confirmPassword: String,
    val fullName: String,
    val email: String
)