package com.kqlite.demo.contacts.utils

import com.kqlite.demo.contacts.model.Contact
import com.kqlite.demo.contacts.model.ContactType
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Instant

fun Contact.fullName(): String = "$firstName $lastName"

fun Contact.initials(): String {
    val first = firstName.firstOrNull()?.uppercaseChar() ?: ""
    val last = lastName.firstOrNull()?.uppercaseChar() ?: ""
    return "$first$last"
}

fun Instant.toDate(): String {
    return this.toString().substringBefore('T')
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