package com.kqlite.demo.contacts.platform

import java.io.File

actual fun getDatabaseAbsolutePath(name: String): String {
    val userHome = System.getProperty("user.home")
    val databaseFolder = File(userHome, ".kqlite-contacts")
    if (!databaseFolder.exists()) {
        databaseFolder.mkdirs()
    }
    return File(databaseFolder, name).absolutePath
}
