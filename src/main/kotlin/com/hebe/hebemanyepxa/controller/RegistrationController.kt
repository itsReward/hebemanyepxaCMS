package com.hebe.hebemanyepxa.controller

import com.hebe.hebemanyepxa.dto.UserRegistrationDto
import com.hebe.hebemanyepxa.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/register")
class RegistrationController(private val userService: UserService) {

    @GetMapping
    fun showRegistrationForm(model: Model): String {
        model.addAttribute("user", UserRegistrationDto("", "", "", "", ""))
        return "register"
    }

    @PostMapping
    fun registerUser(
        @ModelAttribute("user") registrationDto: UserRegistrationDto,
        redirectAttributes: RedirectAttributes
    ): String {
        return try {
            userService.registerUser(registrationDto)
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful. You can now log in.")
            "redirect:/login"
        } catch (e: IllegalArgumentException) {
            redirectAttributes.addFlashAttribute("errorMessage", e.message)
            "redirect:/register"
        } catch (e: Exception) {
            redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred. Please try again.")
            "redirect:/register"
        }
    }
}