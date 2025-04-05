package com.hebe.hebemanyepxa.controller

import com.hebe.hebemanyepxa.model.Quote
import com.hebe.hebemanyepxa.service.QuoteService
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/admin/quotes")
class AdminQuoteController(private val quoteService: QuoteService) {

    @GetMapping
    fun list(
        @PageableDefault(size = 10, sort = ["createdAt"], direction = Sort.Direction.DESC) pageable: Pageable,
        model: Model
    ): String {
        val quotes = quoteService.findAll(pageable)
        model.addAttribute("quotes", quotes)
        model.addAttribute("pageTitle", "Manage Quotes")
        return "admin/quotes/list"
    }

    @GetMapping("/create")
    fun createForm(model: Model): String {
        model.addAttribute("quote", Quote(
            quote = "",
            author = ""
        ))
        model.addAttribute("pageTitle", "Add New Quote")
        return "admin/quotes/create"
    }

    @PostMapping("/create")
    fun create(
        @ModelAttribute quote: Quote,
        bindingResult: BindingResult,
        model: Model
    ): String {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Add New Quote")
            return "admin/quotes/create"
        }

        quoteService.create(quote)
        return "redirect:/admin/quotes"
    }

    @GetMapping("/edit/{id}")
    fun editForm(@PathVariable id: Long, model: Model): String {
        val quote = quoteService.findById(id)
        if (quote.isEmpty) {
            return "redirect:/admin/quotes"
        }

        model.addAttribute("quote", quote.get())
        model.addAttribute("pageTitle", "Edit Quote")
        return "admin/quotes/edit"
    }

    @PostMapping("/edit/{id}")
    fun edit(
        @PathVariable id: Long,
        @ModelAttribute quote: Quote,
        bindingResult: BindingResult,
        model: Model
    ): String {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Quote")
            return "admin/quotes/edit"
        }

        quoteService.update(id, quote)
        return "redirect:/admin/quotes"
    }

    @PostMapping("/delete/{id}")
    fun delete(@PathVariable id: Long): String {
        quoteService.delete(id)
        return "redirect:/admin/quotes"
    }
}