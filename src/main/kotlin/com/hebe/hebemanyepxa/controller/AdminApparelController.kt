package com.hebe.hebemanyepxa.controller

import com.hebe.hebemanyepxa.dto.ApparelFormDto
import com.hebe.hebemanyepxa.dto.ApparelListDto
import com.hebe.hebemanyepxa.model.Apparel
import com.hebe.hebemanyepxa.repository.ImageRepository
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
class AdminApparelController(
    private val apparelService: ApparelService,
    private val imageRepository: ImageRepository
) {

    @GetMapping
    fun list(
        @PageableDefault(size = 10, sort = ["createdAt"], direction = Sort.Direction.DESC) pageable: Pageable,
        model: Model
    ): String {
        val apparelPage = apparelService.findAll(pageable)

        // Convert to DTOs with primary image path
        val apparelListDtos = apparelPage.map { apparel ->
            // Find primary image or use first available
            val primaryImage = apparel.images.find { it.isPrimary } ?: apparel.images.firstOrNull()

            ApparelListDto(
                id = apparel.id,
                title = apparel.title,
                price = apparel.price,
                isFeatured = apparel.isFeatured,
                isAvailable = apparel.isAvailable,
                primaryImagePath = primaryImage?.filePath
            )
        }

        model.addAttribute("apparelItems", apparelListDtos)
        model.addAttribute("apparelPage", apparelPage) // Keep for pagination
        model.addAttribute("pageTitle", "Manage Apparel")
        return "admin/apparel/list"
    }

    @GetMapping("/create")
    fun createForm(model: Model): String {
        model.addAttribute("apparelForm", ApparelFormDto())
        model.addAttribute("pageTitle", "Create New Apparel")
        return "admin/apparel/create"
    }

    @PostMapping("/create")
    fun create(
        @ModelAttribute apparelForm: ApparelFormDto,
        bindingResult: BindingResult,
        @RequestParam("images") images: List<MultipartFile>?,
        model: Model
    ): String {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Create New Apparel")
            return "admin/apparel/create"
        }

        // Convert DTO to entity
        val apparel = Apparel(
            title = apparelForm.title,
            slug = apparelForm.slug,
            description = apparelForm.description,
            price = apparelForm.price,
            isFeatured = apparelForm.isFeatured,
            isAvailable = apparelForm.isAvailable
        )

        apparelService.create(apparel, images)
        return "redirect:/admin/apparel"
    }

    @GetMapping("/edit/{id}")
    fun editForm(@PathVariable id: Long, model: Model): String {
        val apparelOptional = apparelService.findById(id)
        if (apparelOptional.isEmpty) {
            return "redirect:/admin/apparel"
        }

        val apparel = apparelOptional.get()
        // Convert entity to DTO
        val apparelForm = ApparelFormDto(
            id = apparel.id,
            title = apparel.title,
            slug = apparel.slug,
            description = apparel.description,
            price = apparel.price,
            isFeatured = apparel.isFeatured,
            isAvailable = apparel.isAvailable
        )

        model.addAttribute("apparelForm", apparelForm)
        model.addAttribute("apparel", apparel) // Keep this to access images in the template
        model.addAttribute("apparelId", id)
        model.addAttribute("pageTitle", "Edit Apparel")
        return "admin/apparel/edit"
    }

    @PostMapping("/edit/{id}")
    fun edit(
        @PathVariable id: Long,
        @ModelAttribute apparelForm: ApparelFormDto,
        bindingResult: BindingResult,
        @RequestParam("images") images: List<MultipartFile>?,
        model: Model
    ): String {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Apparel")
            return "admin/apparel/edit"
        }

        // Get the existing apparel to maintain its images
        val existingApparel = apparelService.findById(id).orElseThrow()

        // Convert DTO to entity
        val apparel = Apparel(
            id = id,
            title = apparelForm.title,
            slug = apparelForm.slug,
            description = apparelForm.description,
            price = apparelForm.price,
            isFeatured = apparelForm.isFeatured,
            isAvailable = apparelForm.isAvailable,
            images = existingApparel.images // Preserve existing images
        )

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