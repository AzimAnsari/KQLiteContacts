package com.kqlite.demo.contacts.db

import com.kqlite.column.Bind
import com.kqlite.column.CURRENT_TIMESTAMP
import com.kqlite.column.notNull
import com.kqlite.cursor.KQLiteCursor
import com.kqlite.demo.contacts.model.Contact
import com.kqlite.demo.contacts.model.ContactType
import com.kqlite.demo.contacts.utils.toDateString
import com.kqlite.demo.contacts.utils.toInstant
import com.kqlite.functions.DATE
import com.kqlite.functions.JSON_ARRAY
import com.kqlite.table.KQLiteAdapter
import com.kqlite.table.KQLiteTable

object TblContact : KQLiteTable("contacts"), KQLiteAdapter<Contact> {
    val id = intColumn("id").notNull().primaryKey().autoIncrement()
    val firstName = textColumn("first_name").notNull()
    val lastName = textColumn("last_name")
    val phone = jsonArrayColumn("phone").notNull()
    val birthDate = dateColumn("birth_date")
    val email = textColumn("email").check { (it IS null) OR (it LIKE "%_@__%.__%") }
    val image = blobColumn("image")
    val type = enumColumn("type", ContactType.entries).notNull()
    val deleted = booleanColumn("deleted").notNull().default(false)
    val lastModified = dateTimeColumn("last_modified")

    override fun binder(
        bind: Bind,
        item: Contact
    ) {
        bind.apply {
            firstName.bind(item.firstName)
            lastName.bind(item.lastName)
            phone.bind(JSON_ARRAY(*item.phone.toTypedArray()))
            if (item.birthDate != null) birthDate.bind(DATE(item.birthDate.toDateString()))
            else birthDate.bind(null)
            email.bind(item.email)
            image.bind(item.image)
            type.bind(item.type)
            lastModified.bind(CURRENT_TIMESTAMP)
        }
    }

    override fun mapper(cursor: KQLiteCursor): Contact {
        return Contact(
            id = cursor[id],
            firstName = cursor[firstName],
            lastName = cursor[lastName],
            phone = cursor[phone].getText().trim('[', ']').split(',').map { it.trim('"', '"') },
            birthDate = cursor[birthDate]?.getText()?.toInstant(),
            email = cursor[email],
            image = cursor[image],
            type = cursor[type],
            deleted = cursor[deleted]
        )
    }
}