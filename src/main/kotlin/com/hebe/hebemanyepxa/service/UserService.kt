package com.hebe.hebemanyepxa.service

import com.hebe.hebemanyepxa.dto.UserRegistrationDto
import com.hebe.hebemanyepxa.model.User
import com.hebe.hebemanyepxa.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun registerUser(registrationDto: UserRegistrationDto): User {
        // Check if username already exists
        if (userRepository.existsByUsername(registrationDto.username)) {
            throw IllegalArgumentException("Username already exists")
        }

        // Check if email already exists
        if (userRepository.existsByEmail(registrationDto.email)) {
            throw IllegalArgumentException("Email already exists")
        }

        // Check if passwords match
        if (registrationDto.password != registrationDto.confirmPassword) {
            throw IllegalArgumentException("Passwords do not match")
        }

        // Create and save the new user
        val user = User(
            username = registrationDto.username,
            password = passwordEncoder.encode(registrationDto.password),
            fullName = registrationDto.fullName,
            email = registrationDto.email
        )

        return userRepository.save(user)
    }
}