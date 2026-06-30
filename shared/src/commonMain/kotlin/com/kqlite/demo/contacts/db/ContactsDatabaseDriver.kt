package com.kqlite.demo.contacts.db

import androidx.sqlite.SQLiteConnection
import androidx.sqlite.SQLiteDriver
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.kqlite.database.KQLiteDriver

class ContactsDatabaseDriver(
    override val dbFile: String,
    override val version: Int,
    private val sqLiteDriver: SQLiteDriver,
) : KQLiteDriver {

    override fun open(flags: Int?): SQLiteConnection {
        val connection = if (flags != null && sqLiteDriver is BundledSQLiteDriver) {
            sqLiteDriver.open(dbFile, flags)
        } else {
            sqLiteDriver.open(dbFile)
        }
        return connection
    }
}

/*
class LoggingConnection(private val connection: SQLiteConnection) : SQLiteConnection {
    override fun prepare(sql: String): SQLiteStatement {
        println("Executing SQL: $sql")
        return connection.prepare(sql)
    }

    override fun close() {
        println("Closing connection")
        connection.close()
    }

}
*/