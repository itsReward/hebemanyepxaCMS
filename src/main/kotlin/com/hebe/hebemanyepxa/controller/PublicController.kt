package com.hebe.hebemanyepxa.controller

import com.hebe.hebemanyepxa.model.ApparelImage
import com.hebe.hebemanyepxa.service.ApparelService
import com.hebe.hebemanyepxa.service.BookService
import com.hebe.hebemanyepxa.service.PoetryService
import com.hebe.hebemanyepxa.service.QuoteService
import com.hebe.hebemanyepxa.util.HtmlUtil
import com.hebe.hebemanyepxa.util.SimpleHtmlUtil
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.server.ResponseStatusException
import org.springframework.http.HttpStatus
import java.time.LocalDate

@Controller
class PublicController(
    private val poetryService: PoetryService,
    private val apparelService: ApparelService,
    private val bookService: BookService,
    private val quoteService: QuoteService
) {

    private val htmlUtil = SimpleHtmlUtil()

    // Add the HTML utility to all models for use in templates
    private fun addHtmlUtil(model: Model) {
        model.addAttribute("htmlUtil", htmlUtil)
    }

    @GetMapping("/")
    fun home(model: Model): String {
        // Featured content for homepage
        val featuredPoetry = poetryService.findFeatured()
        val featuredBooks = bookService.findFeatured()
        val featuredQuotes = quoteService.findFeatured()
        val featuredApparel = apparelService.findFeatured()

        model.addAttribute("featuredPoetry", featuredPoetry)
        model.addAttribute("featuredBooks", featuredBooks)
        model.addAttribute("featuredQuotes", featuredQuotes)
        model.addAttribute("featuredApparel", featuredApparel)

        // Add HTML utility for templates to use
        addHtmlUtil(model)

        return "public/index"
    }

    @GetMapping("/about")
    fun about(model: Model): String {
        return "public/about"
    }

    @GetMapping("/contact")
    fun contact(model: Model): String {
        return "public/contact"
    }

    @GetMapping("/books")
    fun books(model: Model): String {
        // Get all books for the books page
        val booksPageable = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "publishYear"))
        val books = bookService.findAll(booksPageable)

        model.addAttribute("books", books)

        // Add HTML utility for templates to use
        addHtmlUtil(model)

        return "public/books"
    }

    @GetMapping("/books/{slug}")
    fun bookDetail(@PathVariable slug: String, model: Model): String {
        val book = bookService.findBySlug(slug).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found")
        }

        model.addAttribute("book", book)

        // Related books by same author
        val relatedPageable = PageRequest.of(0, 4, Sort.by(Sort.Direction.DESC, "publishYear"))
        val relatedBooks = bookService.findByAuthor(book.author, relatedPageable).content
            .filter { it.id != book.id }
            .take(3)

        model.addAttribute("relatedBooks", relatedBooks)

        // Add HTML utility for templates to use
        addHtmlUtil(model)

        return "public/book-detail"
    }
    @GetMapping("/quotes")
    fun quotes(model: Model): String {
        // Get quotes grouped by category
        val quotesPageable = PageRequest.of(0, 50, Sort.by(Sort.Direction.DESC, "createdAt"))
        val allQuotes = quoteService.findAll(quotesPageable).content

        // Extract unique categories
        val categories = allQuotes
            .mapNotNull { it.category }
            .flatMap { it.split(",").map { cat -> cat.trim() } }
            .distinct()

        model.addAttribute("quotes", allQuotes)
        model.addAttribute("categories", categories)

        return "public/quotes"
    }

    @GetMapping("/blog")
    fun blog(model: Model): String {
        return "public/blog"
    }


    @GetMapping("/poetry")
    fun poetry(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        model: Model
    ): String {
        val poetryPageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publishDate"))
        val poetryPage = poetryService.findPublished(poetryPageable)

        // Process content to remove HTML tags in excerpt only (for list view)
        poetryPage.content.forEach { poem ->
            if (poem.excerpt != null) {
                // We can't modify the poem directly due to immutability,
                // but excerpts are only for display in the view
                model.addAttribute("cleanExcerpt-${poem.id}", htmlUtil.cleanHtml(poem.excerpt))
            }
        }

        model.addAttribute("poetry", poetryPage)
        model.addAttribute("currentPage", page)
        model.addAttribute("htmlUtil", htmlUtil)

        return "public/poetry"
    }

    @GetMapping("/poetry/{slug}")
    fun poetryDetail(@PathVariable slug: String, model: Model): String {
        val poem = poetryService.findBySlug(slug).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Poem not found")
        }

        // Add the HTML cleaner utility
        val htmlCleaner = SimpleHtmlUtil()

        // Clean the poem content and add it to the model
        val cleanContent = htmlCleaner.cleanHtml(poem.content)
        model.addAttribute("poem", poem)
        model.addAttribute("cleanContent", cleanContent)

        // Get previous and next poems (your existing code)
        // ...

        return "public/poetry-detail"
    }

    @GetMapping("/apparel")
    fun apparel(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "12") size: Int,
        model: Model
    ): String {
        val apparelPageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))
        val apparelPage = apparelService.findAvailable(apparelPageable)

        // Create a helper object to safely handle image retrieval
        val imageHelper = object {
            fun getPrimaryImagePath(images: List<ApparelImage>?): String {
                if (images.isNullOrEmpty()) {
                    return "assets/images/apparel-placeholder.jpg"
                }

                // Try to find primary image
                val primaryImage = images.find { it.isPrimary }

                // If no primary image, return first image
                return primaryImage?.filePath
                    ?: images[0].filePath
                    ?: "assets/images/apparel-placeholder.jpg"
            }
        }

        model.addAttribute("apparel", apparelPage)
        model.addAttribute("currentPage", page)
        model.addAttribute("imageHelper", imageHelper)

        return "public/apparel"
    }

    @GetMapping("/apparel/{slug}")
    fun apparelDetail(@PathVariable slug: String, model: Model): String {
        val apparelItem = apparelService.findBySlug(slug).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Apparel item not found")
        }
        model.addAttribute("apparelItem", apparelItem)

        // Get related items
        val relatedPageable = PageRequest.of(0, 4, Sort.by(Sort.Direction.DESC, "createdAt"))
        val relatedItems = apparelService.findAvailable(relatedPageable).content
            .filter { it.id != apparelItem.id }
            .take(4)
        model.addAttribute("relatedItems", relatedItems)

        // Add the image helper to the model
        model.addAttribute("imageHelper", com.hebe.hebemanyepxa.util.ImageHelper())

        return "public/apparel-detail"
    }
}