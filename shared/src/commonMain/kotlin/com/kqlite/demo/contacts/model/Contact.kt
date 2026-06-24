package com.kqlite.demo.contacts.model

import kotlin.time.Instant

data class Contact(
    val id: Int,
    val firstName: String,
    val lastName: String?,
    val phone: List<String>,
    val birthDate: Instant?,
    val email: String?,
    val image: ByteArray?,
    val type: ContactType,
    val deleted: Boolean = false,
)

enum class ContactType {
    Family,
    Friend,
    Work,
    Other
}