package ir.hadiagdamapps.blackboxchat.data.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

open class DatabaseHelper(context: Context, val table: Table) :
    SQLiteOpenHelper(context, BlackBoxDatabase.DB_NAME, null, BlackBoxDatabase.DB_VERSION) {


    override fun onCreate(db: SQLiteDatabase?) {
        db?.let { database ->
            Table.entries.forEach { table ->
                database.execSQL(table.createQuery)
            }
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.let { database ->
            Table.entries.forEach { table ->
                database.execSQL(table.dropQuery)
            }
        }
        onCreate(db)
    }

}