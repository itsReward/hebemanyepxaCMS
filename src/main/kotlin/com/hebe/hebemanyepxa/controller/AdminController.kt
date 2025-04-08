package com.hebe.hebemanyepxa.controller

import com.hebe.hebemanyepxa.service.ApparelService
import com.hebe.hebemanyepxa.service.BookService
import com.hebe.hebemanyepxa.service.PoetryService
import com.hebe.hebemanyepxa.service.QuoteService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/admin")
class AdminController (
    private val poetryService: PoetryService,
    private val apparelService: ApparelService,
    private val bookService: BookService,
    private val quoteService: QuoteService
){

    @GetMapping("/dashboard")
    fun dashboard(model: Model): String {
        model.addAttribute("pageTitle", "Admin Dashboard")
        // Add counts
        model.addAttribute("poetryCount", poetryService.count())
        model.addAttribute("apparelCount", apparelService.count())
        model.addAttribute("booksCount", bookService.count())
        model.addAttribute("quotesCount", quoteService.count())

        // Add recent items
        val poetryPageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "createdAt"))
        val apparelPageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "createdAt"))

        model.addAttribute("recentPoetry", poetryService.findAll(poetryPageable).content)
        model.addAttribute("recentApparel", apparelService.findAll(apparelPageable).content)


        return "admin/dashboard"
    }

}