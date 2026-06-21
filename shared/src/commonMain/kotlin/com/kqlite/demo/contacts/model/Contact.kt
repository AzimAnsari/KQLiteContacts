package com.kqlite.demo.contacts.model

import kotlin.time.Instant

data class Contact(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val phone: List<String>,
    val birthDate: Instant?,
    val createdTime: Instant,
    val email: String?,
    val image: ByteArray?,
    val type: ContactType,
) {
    override fun equals(other: Any?): Boolean {
        return this.id == (other as? Contact)?.id
    }

    override fun hashCode(): Int = id
}

enum class ContactType {
    Family,
    Friend,
    Work,
    Other
}