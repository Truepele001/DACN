package com.bitchat.android.ui

import android.util.Patterns

/**
 * Utilities for parsing special tokens in chat messages (geohashes, location coordinates, URLs).
 */
object MessageSpecialParser {
    // Standalone geohash pattern like "#9q" or longer. Word boundaries enforced.
    // Geohash alphabet is base32: 0123456789bcdefghjkmnpqrstuvwxyz
    private val standaloneGeohashRegex = Regex("(^|[^A-Za-z0-9_#])#([0-9bcdefghjkmnpqrstuvwxyz]{2,})($|[^A-Za-z0-9_])", RegexOption.IGNORE_CASE)

    // Location coordinates pattern (e.g. Lat: 37.7749, Lon: -122.4194 or 37.7749, -122.4194)
    private val locationRegex = Regex("(?:Lat:\\s*|geo:)?(-?\\d+\\.\\d+)[,\\s]+(?:Lon:\\s*)?(-?\\d+\\.\\d+)", RegexOption.IGNORE_CASE)

    data class GeohashMatch(val start: Int, val endExclusive: Int, val geohash: String)
    data class UrlMatch(val start: Int, val endExclusive: Int, val url: String)
    data class LocationMatch(val start: Int, val endExclusive: Int, val rawText: String, val lat: Double, val lon: Double)

    /**
     * Finds standalone geohashes within [text].
     */
    fun findStandaloneGeohashes(text: String): List<GeohashMatch> {
        if (text.isEmpty()) return emptyList()
        val matches = mutableListOf<GeohashMatch>()
        var index = 0
        while (index < text.length) {
            val m = standaloneGeohashRegex.find(text, index) ?: break
            val fullRange = m.range
            val sub = text.substring(fullRange)
            val hashPos = sub.indexOf('#')
            if (hashPos >= 0) {
                val tokenStart = fullRange.first + hashPos
                var cursor = tokenStart + 1
                while (cursor < text.length) {
                    val ch = text[cursor].lowercaseChar()
                    val isGeoChar = (ch in '0'..'9') || (ch in "bcdefghjkmnpqrstuvwxyz")
                    if (!isGeoChar) break
                    cursor++
                }
                val token = text.substring(tokenStart + 1, cursor)
                if (token.length >= 2) {
                    matches.add(GeohashMatch(tokenStart, cursor, token.lowercase()))
                }
                index = cursor
            } else {
                index = fullRange.last + 1
            }
        }
        return matches
    }

    /**
     * Detects location coordinates (Lat, Lon) in text for map navigation.
     */
    fun findLocationMatches(text: String): List<LocationMatch> {
        if (text.isEmpty()) return emptyList()
        val results = mutableListOf<LocationMatch>()
        for (m in locationRegex.findAll(text)) {
            val lat = m.groupValues[1].toDoubleOrNull()
            val lon = m.groupValues[2].toDoubleOrNull()
            if (lat != null && lon != null && lat in -90.0..90.0 && lon in -180.0..180.0) {
                results.add(LocationMatch(m.range.first, m.range.last + 1, m.value, lat, lon))
            }
        }
        return results
    }

    /**
     * Detect URLs in text.
     */
    fun findUrls(text: String): List<UrlMatch> {
        if (text.isEmpty()) return emptyList()
        val results = mutableListOf<UrlMatch>()

        val webUrl = Patterns.WEB_URL
        val matcher = webUrl.matcher(text)
        while (matcher.find()) {
            var start = matcher.start()
            var endExclusive = matcher.end()
            var token = text.substring(start, endExclusive)

            while (token.isNotEmpty() && token.last() in setOf('.', ',', ';', ':', '!', '?', '\'', '"')) {
                endExclusive -= 1
                token = text.substring(start, endExclusive)
            }
            results.add(UrlMatch(start, endExclusive, token))
        }

        val bare = Regex("(?<!@)(?<![A-Za-z0-9_-])([A-Za-z0-9-]+\\.[A-Za-z]{2,}(?:/[A-Za-z0-9@:%._+~#=/?&!$'()*,-]*)?)")
        for (m in bare.findAll(text)) {
            val start = m.range.first
            var endExclusive = m.range.last + 1
            var token = text.substring(start, endExclusive)

            val overlapsExisting = results.any { start < it.endExclusive && endExclusive > it.start }
            if (overlapsExisting) continue

            if (token.contains('@')) continue

            while (token.isNotEmpty() && token.last() in setOf('.', ',', ';', ':', '!', '?', '\'', '"')) {
                endExclusive -= 1
                token = text.substring(start, endExclusive)
            }

            if (!token.contains('.')) continue

            results.add(UrlMatch(start, endExclusive, token))
        }

        results.sortBy { it.start }
        return results
    }
}
