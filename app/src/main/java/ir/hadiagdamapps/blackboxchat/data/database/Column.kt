package ir.hadiagdamapps.blackboxchat.data.database

import android.content.ContentValues

interface Column {
    val columnName: String
    val definition: String
}

fun ContentValues.put(key: Column, text: String) =
    this.put(key.columnName, text)


fun ContentValues.put(key: Column, bool: Boolean) =
    this.put(key.columnName, bool)

fun ContentValues.put(key: Column, long: Long) =
    this.put(key.columnName, long)

