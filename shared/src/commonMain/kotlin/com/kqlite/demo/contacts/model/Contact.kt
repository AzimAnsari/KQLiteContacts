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
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Contact) return false

        return id == other.id &&
                firstName == other.firstName &&
                lastName == other.lastName &&
                phone == other.phone &&
                birthDate == other.birthDate &&
                email == other.email &&
                image?.decodeToString() == other.image?.decodeToString() &&
                type == other.type &&
                deleted == other.deleted
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + firstName.hashCode()
        result = 31 * result + (lastName?.hashCode() ?: 0)
        result = 31 * result + phone.hashCode()
        result = 31 * result + (birthDate?.hashCode() ?: 0)
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + (image?.decodeToString()?.hashCode() ?: 0)
        result = 31 * result + type.hashCode()
        result = 31 * result + deleted.hashCode()
        return result
    }
}

enum class ContactType {
    Family,
    Friend,
    Work,
    Other
}