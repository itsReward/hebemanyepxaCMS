package com.hebe.hebemanyepxa.controller

import com.hebe.hebemanyepxa.model.Apparel
import com.hebe.hebemanyepxa.service.ApparelService
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.math.BigDecimal

@Controller
@RequestMapping("/admin/apparel")
class AdminApparelController(private val apparelService: ApparelService) {

    @GetMapping
    fun list(
        @PageableDefault(size = 10, sort = ["createdAt"], direction = Sort.Direction.DESC) pageable: Pageable,
        model: Model
    ): String {
        val apparel = apparelService.findAll(pageable)
        model.addAttribute("apparel", apparel)
        model.addAttribute("pageTitle", "Manage Apparel")
        return "admin/apparel/list"
    }

    @GetMapping("/create")
    fun createForm(model: Model): String {
        model.addAttribute("apparel", Apparel(
            title = "",
            slug = "",
            description = "",
            price = BigDecimal.ZERO
        ))
        model.addAttribute("pageTitle", "Create New Apparel")
        return "admin/apparel/create"
    }

    @PostMapping("/create")
    fun create(
        @ModelAttribute apparel: Apparel,
        bindingResult: BindingResult,
        @RequestParam("images") images: List<MultipartFile>?,
        model: Model
    ): String {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Create New Apparel")
            return "admin/apparel/create"
        }

        apparelService.create(apparel, images)
        return "redirect:/admin/apparel"
    }

    @GetMapping("/edit/{id}")
    fun editForm(@PathVariable id: Long, model: Model): String {
        val apparel = apparelService.findById(id)
        if (apparel.isEmpty) {
            return "redirect:/admin/apparel"
        }

        model.addAttribute("apparel", apparel.get())
        model.addAttribute("pageTitle", "Edit Apparel")
        return "admin/apparel/edit"
    }

    @PostMapping("/edit/{id}")
    fun edit(
        @PathVariable id: Long,
        @ModelAttribute apparel: Apparel,
        bindingResult: BindingResult,
        @RequestParam("images") images: List<MultipartFile>?,
        model: Model
    ): String {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Apparel")
            return "admin/apparel/edit"
        }

        apparelService.update(id, apparel, images)
        return "redirect:/admin/apparel"
    }

    @PostMapping("/delete/{id}")
    fun delete(@PathVariable id: Long): String {
        apparelService.delete(id)
        return "redirect:/admin/apparel"
    }

    @PostMapping("/image/{apparelId}/delete/{imageId}")
    fun deleteImage(@PathVariable apparelId: Long, @PathVariable imageId: Long): String {
        apparelService.deleteImage(imageId)
        return "redirect:/admin/apparel/edit/$apparelId"
    }

    @PostMapping("/image/{apparelId}/primary/{imageId}")
    fun setPrimaryImage(@PathVariable apparelId: Long, @PathVariable imageId: Long): String {
        apparelService.setImageAsPrimary(imageId, apparelId)
        return "redirect:/admin/apparel/edit/$apparelId"
    }
}