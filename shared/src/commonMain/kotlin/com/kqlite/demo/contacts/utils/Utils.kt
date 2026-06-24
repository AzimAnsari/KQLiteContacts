package com.kqlite.demo.contacts.utils

import com.kqlite.demo.contacts.model.Contact
import kotlin.time.Instant

fun Contact.fullName(): String = if (lastName == null) firstName else "$firstName $lastName"

/**
 * Generates a string containing the uppercase initials of the contact's first and last name.
 *
 * @return A string consisting of the first character of [Contact.firstName] and
 * [Contact.lastName] in uppercase. If a name is empty, it is omitted from the result.
 */
fun Contact.initials(): String {
    val first = firstName.firstOrNull()?.uppercaseChar() ?: ""
    val last = lastName?.firstOrNull()?.uppercaseChar() ?: ""
    return "$first$last"
}

/**
 * Converts an [Instant] to a date string in ISO-8601 format (YYYY-MM-DD).
 *
 * @return A string representing the date portion of this instant.
 */
fun Instant.toDateString(): String {
    return this.toString().substringBefore('T')
}

/**
 * Converts a date string in ISO-8601 format (YYYY-MM-DD) to an [Instant] at the start of that day (UTC).
 *
 * @return An [Instant] representing midnight UTC on the specified date.
 */
fun String.toInstant(): Instant {
    return Instant.parse(this + "T00:00:00Z")
}

fun String.trimOrNull(): String? = if (isBlank()) null else this.trim()