package com.hebe.hebemanyepxa.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.UUID

@Service
class FileStorageService(
    @Value("\${file.upload-dir}") private val uploadDir: String
) {
    private val fileStorageLocation: Path = Paths.get(uploadDir).toAbsolutePath().normalize()

    init {
        try {
            Files.createDirectories(fileStorageLocation)
        } catch (ex: IOException) {
            throw RuntimeException("Could not create the directory where the uploaded files will be stored", ex)
        }
    }

    fun storeFile(file: MultipartFile, subDirectory: String): String {
        // Normalize file name
        val originalFileName = file.originalFilename ?: "file"
        val fileExtension = originalFileName.substringAfterLast('.', "")
        val fileName = "${UUID.randomUUID()}.$fileExtension"

        try {
            val targetDirectory = fileStorageLocation.resolve(subDirectory)
            Files.createDirectories(targetDirectory)

            val targetLocation = targetDirectory.resolve(fileName)
            Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)

            return "$subDirectory/$fileName"
        } catch (ex: IOException) {
            throw RuntimeException("Could not store file $fileName. Please try again!", ex)
        }
    }

    fun loadFileAsResource(filePath: String): Resource {
        try {
            val resource = UrlResource(fileStorageLocation.resolve(filePath).toUri())
            if (resource.exists()) {
                return resource
            } else {
                throw RuntimeException("File not found: $filePath")
            }
        } catch (ex: MalformedURLException) {
            throw RuntimeException("File not found: $filePath", ex)
        }
    }

    fun deleteFile(filePath: String): Boolean {
        return try {
            val file = fileStorageLocation.resolve(filePath)
            Files.deleteIfExists(file)
        } catch (ex: IOException) {
            false
        }
    }
}