package com.hebe.hebemanyepxa.controller

import jakarta.servlet.RequestDispatcher
import jakarta.servlet.http.HttpServletRequest
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class CustomErrorController : ErrorController {

    @RequestMapping("/error")
    fun handleError(request: HttpServletRequest, model: Model): String {
        val status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)

        if (status != null) {
            val statusCode = status.toString().toInt()
            model.addAttribute("status", statusCode)

            // Get error message
            val error = when (statusCode) {
                HttpStatus.NOT_FOUND.value() -> "Page Not Found"
                HttpStatus.FORBIDDEN.value() -> "Access Denied"
                HttpStatus.INTERNAL_SERVER_ERROR.value() -> "Internal Server Error"
                else -> request.getAttribute(RequestDispatcher.ERROR_MESSAGE)?.toString() ?: "Unknown Error"
            }

            model.addAttribute("error", error)
            model.addAttribute("message", request.getAttribute(RequestDispatcher.ERROR_MESSAGE))
        }

        return "error"
    }
}