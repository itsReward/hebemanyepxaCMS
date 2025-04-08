package com.hebe.hebemanyepxa.controller

import com.hebe.hebemanyepxa.model.Poetry
import com.hebe.hebemanyepxa.service.PoetryService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@Controller
@RequestMapping("/admin/poetry")
class AdminPoetryController(private val poetryService: PoetryService) {

    @GetMapping
    fun list(
        @PageableDefault(size = 10, sort = ["publishDate"], direction = Sort.Direction.DESC) pageable: Pageable,
        model: Model,
        request: HttpServletRequest
    ): String {
        val poetry = poetryService.findAll(pageable)
        model.addAttribute("poetry", poetry)
        model.addAttribute("pageTitle", "Manage Poetry")
        model.addAttribute("request", request)
        return "admin/poetry/list"
    }

    @GetMapping("/create")
    fun createForm(model: Model): String {
        model.addAttribute("poetry", Poetry(
            title = "",
            slug = "",
            content = "",
            publishDate = LocalDate.now()
        ))
        model.addAttribute("pageTitle", "Create New Poetry")
        return "admin/poetry/create"
    }

    @PostMapping("/create")
    fun create(
        @ModelAttribute poetry: Poetry,
        bindingResult: BindingResult,
        model: Model
    ): String {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Create New Poetry")
            return "admin/poetry/create"
        }

        poetryService.create(poetry)
        return "redirect:/admin/poetry"
    }

    @GetMapping("/edit/{id}")
    fun editForm(@PathVariable id: Long, model: Model, request: HttpServletRequest): String {
        val poetry = poetryService.findById(id)
        if (poetry.isEmpty) {
            return "redirect:/admin/poetry"
        }

        model.addAttribute("poetry", poetry.get())
        model.addAttribute("pageTitle", "Edit Poetry")
        model.addAttribute("request", request)
        return "admin/poetry/edit"
    }

    @PostMapping("/edit/{id}")
    fun edit(
        @PathVariable id: Long,
        @ModelAttribute poetry: Poetry,
        bindingResult: BindingResult,
        model: Model
    ): String {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Poetry")
            return "admin/poetry/edit"
        }

        poetryService.update(id, poetry)
        return "redirect:/admin/poetry"
    }

    @PostMapping("/delete/{id}")
    fun delete(@PathVariable id: Long): String {
        poetryService.delete(id)
        return "redirect:/admin/poetry"
    }
}