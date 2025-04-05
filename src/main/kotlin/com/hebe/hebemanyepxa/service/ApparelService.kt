package com.hebe.hebemanyepxa.service

import com.hebe.hebemanyepxa.model.Apparel
import com.hebe.hebemanyepxa.model.ApparelImage
import com.hebe.hebemanyepxa.repository.ApparelRepository
import com.hebe.hebemanyepxa.repository.ImageRepository
import com.hebe.hebemanyepxa.util.SlugUtil
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.Optional

@Service
class ApparelService(
    private val apparelRepository: ApparelRepository,
    private val imageRepository: ImageRepository,
    private val fileStorageService: FileStorageService
) {

    fun findAll(pageable: Pageable): Page<Apparel> {
        return apparelRepository.findAll(pageable)
    }

    fun findById(id: Long): Optional<Apparel> {
        return apparelRepository.findById(id)
    }

    fun findBySlug(slug: String): Optional<Apparel> {
        return apparelRepository.findBySlug(slug)
    }

    fun findFeatured(): List<Apparel> {
        return apparelRepository.findByIsFeaturedTrue()
    }

    fun findAvailable(pageable: Pageable): Page<Apparel> {
        return apparelRepository.findByIsAvailableTrue(pageable)
    }

    @Transactional
    fun create(apparel: Apparel, imageFiles: List<MultipartFile>?): Apparel {
        // Generate slug if not provided
        val slug = if (apparel.slug.isBlank()) {
            generateUniqueSlug(apparel.title)
        } else {
            apparel.slug
        }

        // Create new apparel with the generated slug
        val savedApparel = apparelRepository.save(apparel.copy(slug = slug))

        // Process images if provided
        imageFiles?.forEachIndexed { index, file ->
            if (!file.isEmpty) {
                val filePath = fileStorageService.storeFile(file, "apparel/${savedApparel.id}")
                val isPrimary = index == 0 // First image is primary by default

                val apparelImage = ApparelImage(
                    apparel = savedApparel,
                    fileName = file.originalFilename ?: "image-${index + 1}",
                    filePath = filePath,
                    isPrimary = isPrimary
                )

                imageRepository.save(apparelImage)
            }
        }

        return apparelRepository.findById(savedApparel.id).get()
    }

    @Transactional
    fun update(id: Long, apparel: Apparel, imageFiles: List<MultipartFile>?): Optional<Apparel> {
        return apparelRepository.findById(id).map { existingApparel ->
            val updatedApparel = apparel.copy(
                id = existingApparel.id,
                slug = if (apparel.slug != existingApparel.slug && apparel.slug.isNotBlank()) {
                    generateUniqueSlug(apparel.slug)
                } else {
                    existingApparel.slug
                },
                createdAt = existingApparel.createdAt
            )

            val savedApparel = apparelRepository.save(updatedApparel)

            // Process new images if provided
            imageFiles?.forEachIndexed { index, file ->
                if (!file.isEmpty) {
                    val filePath = fileStorageService.storeFile(file, "apparel/${savedApparel.id}")

                    // Check if there are any existing images
                    val existingImages = imageRepository.findByApparelId(savedApparel.id)
                    val isPrimary = existingImages.isEmpty() && index == 0

                    val apparelImage = ApparelImage(
                        apparel = savedApparel,
                        fileName = file.originalFilename ?: "image-${index + 1}",
                        filePath = filePath,
                        isPrimary = isPrimary
                    )

                    imageRepository.save(apparelImage)
                }
            }

            apparelRepository.findById(savedApparel.id).get()
        }
    }

    @Transactional
    fun delete(id: Long) {
        // First find all images and delete the files
        val images = imageRepository.findByApparelId(id)
        images.forEach { image ->
            fileStorageService.deleteFile(image.filePath)
        }

        // Then delete the apparel (will cascade delete the images in DB)
        apparelRepository.deleteById(id)
    }

    @Transactional
    fun deleteImage(imageId: Long) {
        imageRepository.findById(imageId).ifPresent { image ->
            fileStorageService.deleteFile(image.filePath)
            imageRepository.delete(image)
        }
    }

    @Transactional
    fun setImageAsPrimary(imageId: Long, apparelId: Long) {
        // First reset all existing primary images
        val existingPrimary = imageRepository.findByApparelIdAndIsPrimaryTrue(apparelId)
        existingPrimary?.let {
            imageRepository.save(it.copy(isPrimary = false))
        }

        // Set the new primary image
        imageRepository.findById(imageId).ifPresent { image ->
            imageRepository.save(image.copy(isPrimary = true))
        }
    }

    private fun generateUniqueSlug(title: String): String {
        var slug = SlugUtil.toSlug(title)
        var counter = 1

        while (apparelRepository.existsBySlug(slug)) {
            slug = "${SlugUtil.toSlug(title)}-$counter"
            counter++
        }

        return slug
    }
}