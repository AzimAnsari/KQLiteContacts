package com.kqlite.demo.contacts.db

import androidx.sqlite.driver.bundled.SQLITE_OPEN_READONLY
import com.kqlite.database.KQLiteDatabase
import com.kqlite.database.KQLiteDriver
import com.kqlite.demo.contacts.platform.getDatabaseAbsolutePath
import com.kqlite.table.KQLiteTable

class ContactsDatabase(
    kqLiteDriver: KQLiteDriver = ContactsDatabaseDriver(getDatabaseAbsolutePath(NAME))
) : KQLiteDatabase(kqLiteDriver = kqLiteDriver) {

    companion object {
        const val NAME = "contacts.db"
        const val VERSION = 1
    }

    override fun getKQLiteTables(): List<KQLiteTable> {
        return listOf(TblContact)
    }

    fun openReadOnly() = this.open(SQLITE_OPEN_READONLY)
}