package com.hebe.hebemanyepxa.util

/**
 * A simple utility class for cleaning HTML content without external dependencies
 * that properly preserves line breaks for poetry
 */
class SimpleHtmlUtil {

    /**
     * Removes HTML tags and converts common HTML entities to their text equivalents
     * while carefully preserving line breaks for poetry formatting
     *
     * @param html HTML content to clean
     * @return Plain text with line breaks properly preserved
     */
    fun cleanHtml(html: String?): String {
        if (html.isNullOrEmpty()) {
            return ""
        }

        // First replace <br> tags with special marker
        val withLineBreaks = html.replace("<br[^>]*>".toRegex(), "##LINEBREAK##")

        // Replace paragraph tags with double markers
        val withParagraphs = withLineBreaks
            .replace("<p[^>]*>".toRegex(), "")
            .replace("</p>".toRegex(), "##PARAGRAPH##")

        // Remove style and span tags which are common in copied content
        val withoutStyle = withParagraphs
            .replace("<span[^>]*>".toRegex(), "")
            .replace("</span>".toRegex(), "")
            .replace("<style[^>]*>.*?</style>".toRegex(RegexOption.DOT_MATCHES_ALL), "")

        // Remove all other HTML tags
        val noTags = withoutStyle.replace("<[^>]*>".toRegex(), "")

        // Replace common HTML entities
        val withEntities = noTags
            .replace("&nbsp;", " ")
            .replace("&amp;", "&")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&quot;", "\"")
            .replace("&#39;", "'")

        // Restore line breaks and paragraphs
        val withRestoredBreaks = withEntities
            .replace("##LINEBREAK##", "\n")
            .replace("##PARAGRAPH##", "\n\n")

        // Clean up extra whitespace without removing intentional line breaks
        return withRestoredBreaks
            .replace("\r\n", "\n")
            .replace("\t", "    ")  // Convert tabs to spaces
            .replace(" {2,}".toRegex(), " ")  // Collapse multiple spaces
            .replace(" \n", "\n")  // Remove spaces at end of lines
            .replace("\n ", "\n")  // Remove spaces at start of lines
            .trim()
    }
}