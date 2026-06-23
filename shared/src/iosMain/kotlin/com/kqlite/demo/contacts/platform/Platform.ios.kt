package com.kqlite.demo.contacts.platform

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.delight.db.DelightDatabase
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

actual fun getDatabaseAbsolutePath(name: String): String {
    val documents = NSFileManager.defaultManager.URLsForDirectory(
        NSDocumentDirectory,
        NSUserDomainMask
    ).firstOrNull()

    val databaseUrl = (documents as? NSURL)?.URLByAppendingPathComponent(name)

    return databaseUrl?.path ?: throw Exception("Failed to get database path")
}

actual fun createDelightDriver(): SqlDriver {
    return NativeSqliteDriver(
        DelightDatabase.Schema,
        "delight_database.db"
    )
}