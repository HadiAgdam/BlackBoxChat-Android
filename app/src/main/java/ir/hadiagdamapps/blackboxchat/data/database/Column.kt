package ir.hadiagdamapps.blackboxchat.data.database

import android.content.ContentValues

interface Column {
    val columnName: String
    val definition: String
}

fun ContentValues.put(key: Column, text: String) {
    put(key.columnName, text)
}
