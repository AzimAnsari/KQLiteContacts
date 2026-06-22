package com.kqlite.demo.contacts.platform

import android.content.Context
import org.koin.java.KoinJavaComponent.inject

private val context: Context by inject(Context::class.java)

actual fun getDatabaseAbsolutePath(name: String): String {
    return context.getDatabasePath(name).absolutePath
}