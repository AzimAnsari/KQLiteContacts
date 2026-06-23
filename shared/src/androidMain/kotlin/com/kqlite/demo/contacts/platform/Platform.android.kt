package com.kqlite.demo.contacts.platform

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.delight.db.DelightDatabase
import org.koin.java.KoinJavaComponent.inject

private val context: Context by inject(Context::class.java)

actual fun getDatabaseAbsolutePath(name: String): String {
    return context.getDatabasePath(name).absolutePath
}

actual fun createDelightDriver(): SqlDriver {
    return AndroidSqliteDriver(
        DelightDatabase.Schema,
        context,
        "delight_database.db"
    )
}