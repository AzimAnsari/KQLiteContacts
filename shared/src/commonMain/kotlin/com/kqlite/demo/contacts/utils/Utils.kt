package com.kqlite.demo.contacts.utils

import com.kqlite.demo.contacts.model.Contact
import com.kqlite.demo.contacts.model.ContactType
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Instant

fun Contact.fullName(): String = "$firstName $lastName"

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

fun testContacts(): List<Contact> {
    val now = Clock.System.now()
    return listOf(
        Contact(
            id = 1,
            firstName = "Azim",
            lastName = "Ansari",
            phone = listOf("+91 9876543210", "+91 9376573159"),
            birthDate = now.minus(365.days * 25),
            createdTime = now.minus(10.days),
            email = "azim@example.com",
            image = null, // placeholder
            type = ContactType.Work
        ),
        Contact(
            id = 2,
            firstName = "Priya",
            lastName = "Sharma",
            phone = listOf("+91 9123456780"),
            birthDate = now.minus(365.days * 28),
            createdTime = now.minus(30.days),
            email = "priya@gmail.com",
            image = null,
            type = ContactType.Family
        ),
        Contact(
            id = 3,
            firstName = "Rahul",
            lastName = "Verma",
            phone = listOf("+91 9988776655"),
            birthDate = now.minus(365.days * 30),
            createdTime = now.minus(1.hours),
            email = null,
            image = null,
            type = ContactType.Friend
        )
    )
}