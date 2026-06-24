package com.kqlite.demo.contacts.db

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.sqlite.driver.bundled.SQLITE_OPEN_READONLY
import com.kqlite.database.KQLiteDatabase
import com.kqlite.database.KQLiteDriver
import com.kqlite.demo.contacts.platform.getDatabaseAbsolutePath
import com.kqlite.table.KQLiteTable

class ContactsDatabase(
    file: String = getDatabaseAbsolutePath(NAME),
    version: Int = VERSION,
    kqLiteDriver: KQLiteDriver = ContactsDatabaseDriver(sqLiteDriver = BundledSQLiteDriver())
) : KQLiteDatabase(
    file = file,
    version = version,
    kqLiteDriver = kqLiteDriver
) {

    companion object {
        private const val NAME = "contacts.db"
        private const val VERSION = 1
    }

    override fun getKQLiteTables(): List<KQLiteTable> {
        return listOf(TblContact)
    }

    fun openReadOnly() = this.open(SQLITE_OPEN_READONLY)
}