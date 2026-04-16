package org.getscol.gscol.feature.course_details.presentation.components

/**
 * Opens Google Maps at the given coordinates (browser or Maps app when installed).
 */
fun googleMapsOpenUrl(latitude: Double, longitude: Double): String =
    "https://www.google.com/maps/search/?api=1&query=$latitude,$longitude"

/**
 * Extracts lat/lng from common Google Maps URLs (e.g. `...&ll=51.51,-0.10&...`).
 */
/**
 * When the API concatenates multiple URLs (e.g. maps link + accidental second URL),
 * keeps the first HTTP(S) segment so opening the map still works.
 */
fun normalizeCoordinatesLink(link: String): String {
    val decoded = link.trim().replace("%20", " ").replace("%0A", " ").replace("%09", " ")
    val first = decoded.split(Regex("\\s+")).firstOrNull { segment ->
        segment.startsWith("http://", ignoreCase = true) || segment.startsWith("https://", ignoreCase = true)
    }
    return first ?: link.trim()
}

fun parseLatLngFromMapsUrl(url: String): Pair<Double, Double>? {
    val regex = Regex("""[?&]ll=(-?\d+(?:\.\d+)?),(-?\d+(?:\.\d+)?)""", RegexOption.IGNORE_CASE)
    val match = regex.find(url) ?: return null
    val lat = match.groupValues[1].toDoubleOrNull() ?: return null
    val lng = match.groupValues[2].toDoubleOrNull() ?: return null
    return lat to lng
}

/**
 * Google Static Maps image URL for a thumbnail. Enable Maps Static API for your key.
 * Returns null when [apiKey] is blank (thumbnail falls back to placeholder).
 */
fun googleStaticMapImageUrl(
    latitude: Double,
    longitude: Double,
    apiKey: String,
    width: Int = 600,
    height: Int = 360,
): String? {
    if (apiKey.isBlank()) return null
    return buildString {
        append("https://maps.googleapis.com/maps/api/staticmap?")
        append("center=").append(latitude).append(',').append(longitude)
        append("&zoom=15")
        append("&size=").append(width).append('x').append(height)
        append("&scale=2")
        append("&markers=color:red%7C").append(latitude).append(',').append(longitude)
        append("&key=").append(apiKey)
    }
}
