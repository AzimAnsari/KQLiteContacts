package com.kqlite.demo.contacts.db

import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.kqlite.database.KQLiteDriver

class ContactsDatabaseDriver : KQLiteDriver {
    private val driver = BundledSQLiteDriver()
    override fun open(file: String, flags: Int?): SQLiteConnection {
        val connection = flags?.let {
            driver.open(file, it)
        } ?: driver.open(file)

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