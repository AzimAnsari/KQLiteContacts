package com.kqlite.demo.contacts.db

import com.kqlite.database.KQLiteDatabase
import com.kqlite.migration.KQLiteMigration

class Migration12 : KQLiteMigration(fromVersion = 1, toVersion = 2) {

    override fun migrate(database: KQLiteDatabase) {
        val schema = database.schema()

        schema
            .alterTable(TblContact)
            .addColumn(TblContact.lastModified)
    }
}