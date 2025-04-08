package com.hebe.hebemanyepxa.controller

import com.hebe.hebemanyepxa.dto.BookFormDto
import com.hebe.hebemanyepxa.dto.BookListDto
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
        val booksPage = bookService.findAll(pageable)

        // Convert to DTOs
        val bookListDtos = booksPage.map { book ->
            BookListDto(
                id = book.id,
                title = book.title,
                author = book.author,
                publishYear = book.publishYear,
                isFeatured = book.isFeatured,
                coverImagePath = book.coverImage
            )
        }

        model.addAttribute("books", bookListDtos)
        model.addAttribute("booksPage", booksPage) // Keep for pagination
        model.addAttribute("pageTitle", "Manage Books")
        return "admin/books/list"
    }

    @GetMapping("/create")
    fun createForm(model: Model): String {
        model.addAttribute("bookForm", BookFormDto())
        model.addAttribute("pageTitle", "Add New Book")
        return "admin/books/create"
    }

    @PostMapping("/create")
    fun create(
        @ModelAttribute bookForm: BookFormDto,
        bindingResult: BindingResult,
        @RequestParam("coverImage") coverImage: MultipartFile?,
        model: Model
    ): String {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Add New Book")
            return "admin/books/create"
        }

        // Convert DTO to entity
        val book = Book(
            title = bookForm.title,
            slug = bookForm.slug,
            description = bookForm.description,
            author = bookForm.author,
            publishYear = bookForm.publishYear,
            bookLink = bookForm.bookLink,
            isFeatured = bookForm.isFeatured
        )

        bookService.create(book, coverImage)
        return "redirect:/admin/books"
    }

    @GetMapping("/edit/{id}")
    fun editForm(@PathVariable id: Long, model: Model): String {
        val bookOptional = bookService.findById(id)
        if (bookOptional.isEmpty) {
            return "redirect:/admin/books"
        }

        val book = bookOptional.get()
        // Convert entity to DTO
        val bookForm = BookFormDto(
            id = book.id,
            title = book.title,
            slug = book.slug,
            description = book.description,
            author = book.author,
            publishYear = book.publishYear,
            bookLink = book.bookLink,
            isFeatured = book.isFeatured
        )

        model.addAttribute("bookForm", bookForm)
        model.addAttribute("book", book) // Keep for displaying cover image
        model.addAttribute("bookId", id)
        model.addAttribute("pageTitle", "Edit Book")
        return "admin/books/edit"
    }

    @PostMapping("/edit/{id}")
    fun edit(
        @PathVariable id: Long,
        @ModelAttribute bookForm: BookFormDto,
        bindingResult: BindingResult,
        @RequestParam("coverImage") coverImage: MultipartFile?,
        model: Model
    ): String {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Book")
            return "admin/books/edit"
        }

        // Convert DTO to entity
        val book = Book(
            id = id,
            title = bookForm.title,
            slug = bookForm.slug,
            description = bookForm.description,
            author = bookForm.author,
            publishYear = bookForm.publishYear,
            bookLink = bookForm.bookLink,
            isFeatured = bookForm.isFeatured
        )

        bookService.update(id, book, coverImage)
        return "redirect:/admin/books"
    }

    @PostMapping("/delete/{id}")
    fun delete(@PathVariable id: Long): String {
        bookService.delete(id)
        return "redirect:/admin/books"
    }
}