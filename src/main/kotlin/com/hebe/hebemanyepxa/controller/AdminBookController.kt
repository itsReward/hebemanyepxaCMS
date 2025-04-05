package com.hebe.hebemanyepxa.controller

import com.hebe.hebemanyepxa.model.Book
import com.hebe.hebemanyepxa.service.BookService
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@Controller
@RequestMapping("/admin/books")
class AdminBookController(private val bookService: BookService) {

    @GetMapping
    fun list(
        @PageableDefault(size = 10, sort = ["createdAt"], direction = Sort.Direction.DESC) pageable: Pageable,
        model: Model
    ): String {
        val books = bookService.findAll(pageable)
        model.addAttribute("books", books)
        model.addAttribute("pageTitle", "Manage Books")
        return "admin/books/list"
    }

    @GetMapping("/create")
    fun createForm(model: Model): String {
        model.addAttribute("book", Book(
            title = "",
            slug = "",
            description = "",
            author = ""
        ))
        model.addAttribute("pageTitle", "Add New Book")
        return "admin/books/create"
    }

    @PostMapping("/create")
    fun create(
        @ModelAttribute book: Book,
        bindingResult: BindingResult,
        @RequestParam("coverImage") coverImage: MultipartFile?,
        model: Model
    ): String {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Add New Book")
            return "admin/books/create"
        }

        bookService.create(book, coverImage)
        return "redirect:/admin/books"
    }

    @GetMapping("/edit/{id}")
    fun editForm(@PathVariable id: Long, model: Model): String {
        val book = bookService.findById(id)
        if (book.isEmpty) {
            return "redirect:/admin/books"
        }

        model.addAttribute("book", book.get())
        model.addAttribute("pageTitle", "Edit Book")
        return "admin/books/edit"
    }

    @PostMapping("/edit/{id}")
    fun edit(
        @PathVariable id: Long,
        @ModelAttribute book: Book,
        bindingResult: BindingResult,
        @RequestParam("coverImage") coverImage: MultipartFile?,
        model: Model
    ): String {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Book")
            return "admin/books/edit"
        }

        bookService.update(id, book, coverImage)
        return "redirect:/admin/books"
    }

    @PostMapping("/delete/{id}")
    fun delete(@PathVariable id: Long): String {
        bookService.delete(id)
        return "redirect:/admin/books"
    }
}