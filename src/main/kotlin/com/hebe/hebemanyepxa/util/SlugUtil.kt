package com.hebe.hebemanyepxa.util

import java.text.Normalizer
import java.util.Locale
import java.util.regex.Pattern

object SlugUtil {
    private val NONLATIN = Pattern.compile("[^\\w-]")
    private val WHITESPACE = Pattern.compile("[\\s]")

    fun toSlug(input: String): String {
        val nowhitespace = WHITESPACE.matcher(input).replaceAll("-")
        val normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD)
        val slug = NONLATIN.matcher(normalized).replaceAll("")
        return slug.lowercase(Locale.ENGLISH)
    }
}