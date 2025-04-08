package com.hebe.hebemanyepxa.controller

import com.hebe.hebemanyepxa.dto.QuoteFormDto
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
        model.addAttribute("quoteForm", QuoteFormDto())
        model.addAttribute("pageTitle", "Add New Quote")
        return "admin/quotes/create"
    }

    @PostMapping("/create")
    fun create(
        @ModelAttribute quoteForm: QuoteFormDto,
        bindingResult: BindingResult,
        model: Model
    ): String {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Add New Quote")
            return "admin/quotes/create"
        }

        // Convert DTO to entity and save
        val quote = Quote(
            quote = quoteForm.quoteText,
            author = quoteForm.author,
            category = quoteForm.category,
            isFeatured = quoteForm.isFeatured
        )
        quoteService.create(quote)
        return "redirect:/admin/quotes"
    }

    @GetMapping("/edit/{id}")
    fun editForm(@PathVariable id: Long, model: Model): String {
        val quoteOptional = quoteService.findById(id)
        if (quoteOptional.isEmpty) {
            return "redirect:/admin/quotes"
        }

        val quote = quoteOptional.get()
        // Convert entity to DTO
        val quoteForm = QuoteFormDto(
            id = quote.id,
            quoteText = quote.quote,
            author = quote.author,
            category = quote.category,
            isFeatured = quote.isFeatured
        )

        model.addAttribute("quoteForm", quoteForm)
        model.addAttribute("pageTitle", "Edit Quote")
        return "admin/quotes/edit"
    }

    @PostMapping("/edit/{id}")
    fun edit(
        @PathVariable id: Long,
        @ModelAttribute quoteForm: QuoteFormDto,
        bindingResult: BindingResult,
        model: Model
    ): String {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Quote")
            return "admin/quotes/edit"
        }

        // Convert DTO to entity
        val quote = Quote(
            id = id,
            quote = quoteForm.quoteText,
            author = quoteForm.author,
            category = quoteForm.category,
            isFeatured = quoteForm.isFeatured
        )
        quoteService.update(id, quote)
        return "redirect:/admin/quotes"
    }

    @PostMapping("/delete/{id}")
    fun delete(@PathVariable id: Long): String {
        quoteService.delete(id)
        return "redirect:/admin/quotes"
    }
}