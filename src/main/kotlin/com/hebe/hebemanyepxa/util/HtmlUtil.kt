package com.hebe.hebemanyepxa.util

import org.jsoup.Jsoup
import org.jsoup.safety.Safelist

/**
 * Utility class for handling HTML content
 */
class HtmlUtil {

    /**
     * Cleans HTML content by removing tags while preserving line breaks
     *
     * @param html The HTML content to clean
     * @return Plain text with line breaks preserved
     */
    fun cleanHtml(html: String?): String {
        if (html.isNullOrEmpty()) {
            return ""
        }

        // Replace <br> tags with newlines before removing all HTML
        val withLineBreaks = html.replace("<br[^>]*>".toRegex(), "\n")

        // Replace style tags and attributes
        val withoutStyle = withLineBreaks.replace("<span[^>]*>".toRegex(), "")
            .replace("</span>".toRegex(), "")

        // Use Jsoup to remove all HTML tags
        val cleanText = Jsoup.clean(withoutStyle, Safelist.none())

        return cleanText
    }

    /**
     * Safely converts HTML to plain text, preserving paragraphs and line breaks
     * for poetry display
     *
     * @param html The HTML content to process
     * @return Formatted text suitable for poetry display
     */
    fun formatPoetryContent(html: String?): String {
        if (html.isNullOrEmpty()) {
            return ""
        }

        // Parse the HTML
        val doc = Jsoup.parse(html)

        // Replace <p> tags with double newlines and <br> tags with newlines
        val content = html.replace("<p[^>]*>".toRegex(), "\n\n")
            .replace("<br[^>]*>".toRegex(), "\n")
            .replace("<span[^>]*>".toRegex(), "")
            .replace("</span>".toRegex(), "")

        // Remove any remaining HTML tags
        return Jsoup.clean(content, Safelist.none())
    }
}