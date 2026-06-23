package com.kqlite.demo.contacts.platform

import app.cash.sqldelight.db.SqlDriver

expect fun getDatabaseAbsolutePath(name: String): String

expect fun createDelightDriver(): SqlDriver