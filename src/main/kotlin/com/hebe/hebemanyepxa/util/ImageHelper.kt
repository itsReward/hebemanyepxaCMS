package com.hebe.hebemanyepxa.util

import com.hebe.hebemanyepxa.model.ApparelImage

class ImageHelper {
    /**
     * Gets the primary image path from a list of images.
     * Returns the first primary image if found, otherwise returns the first image.
     * Returns null if the list is empty.
     */
    fun getPrimaryImagePath(images: List<ApparelImage>?): String? {
        if (images.isNullOrEmpty()) {
            return null
        }

        // Find primary image
        val primaryImage = images.find { it.isPrimary }

        // Return primary image path or first image path
        return primaryImage?.filePath ?: images[0].filePath
    }
}