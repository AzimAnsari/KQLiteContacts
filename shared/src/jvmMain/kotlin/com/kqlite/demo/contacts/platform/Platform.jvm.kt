package com.kqlite.demo.contacts.platform

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.delight.db.DelightDatabase
import java.io.File

actual fun getDatabaseAbsolutePath(name: String): String {
    val userHome = System.getProperty("user.home")
    val databaseFolder = File(userHome, ".kqlite-contacts")
    if (!databaseFolder.exists()) {
        databaseFolder.mkdirs()
    }
    return File(databaseFolder, name).absolutePath
}

actual fun createDelightDriver(): SqlDriver {
    val driver = JdbcSqliteDriver("jdbc:sqlite:delight_database.db")
    DelightDatabase.Schema.create(driver)
    return driver
}