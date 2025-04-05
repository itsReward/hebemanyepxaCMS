package com.hebe.hebemanyepxa.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/admin")
class AdminController {

    @GetMapping("/dashboard")
    fun dashboard(model: Model): String {
        model.addAttribute("pageTitle", "Admin Dashboard")
        return "admin/dashboard"
    }

}