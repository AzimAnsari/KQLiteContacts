package com.kqlite.demo.contacts.delight

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.db.SqlDriver
import com.delight.db.DelightDatabase
import com.kqlite.demo.contacts.model.Contact
import com.kqlite.demo.contacts.model.ContactType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlin.time.Clock
import kotlin.time.Instant

class DelightDatabaseHelper(driver: SqlDriver) {
    private val database = DelightDatabase(driver)
    private val queries = database.contactsQueries

    fun selectContactsAsFlow(): Flow<List<Contact>> {
        return queries
            .selectAll(
                mapper =
                    { id,
                      first_name,
                      last_name,
                      phone,
                      birth_date,
                      created_time,
                      email,
                      image,
                      type,
                      deleted ->
                        Contact(
                            id.toInt(),
                            first_name,
                            last_name,
                            phone.split(","),
                            birth_date?.let { Instant.parse(it) },
                            Instant.parse(created_time),
                            email,
                            image,
                            ContactType.valueOf(type),
                            deleted == 1L
                        )
                    })
            .asFlow()
            .mapToList(Dispatchers.IO)
    }

    fun insertContact(contact: Contact): Long {
        return queries.insertContact(
            first_name = contact.firstName,
            last_name = contact.lastName,
            phone = contact.phone.toString(),
            birth_date = contact.birthDate?.toString(),
            created_time = Clock.System.now().toString(),
            email = contact.email,
            image = contact.image,
            type = contact.type.name,
            deleted = if (contact.deleted) 1L else 0L,
        ).value
    }
}