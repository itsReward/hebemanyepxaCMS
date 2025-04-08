package com.hebe.hebemanyepxa.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    @Value("\${file.upload-dir}") private val uploadDir: String
) : WebMvcConfigurer {

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        // Map URL "/uploads/**" to the file storage directory
        registry.addResourceHandler("/uploads/**")
            .addResourceLocations("file:$uploadDir/")

        // Add standard resource handlers for static assets
        registry.addResourceHandler("/assets/**")
            .addResourceLocations("classpath:/static/assets/")
    }
}