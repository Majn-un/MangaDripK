package com.example.mangadripk.Database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.programmersbox.manga_sources.mangasources.MangaModel


/**
 * Created by Mitch on 2016-05-13.
 */
class Source(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, 2) {
    override fun onCreate(db: SQLiteDatabase) {
        val createTable =
            ("CREATE TABLE " + TABLE_NAME + " ("
                    + KEY_ID + " INTEGER PRIMARY KEY," + KEY_SOURCE + " TEXT" + ")")
        db.execSQL(createTable)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addData(
        source: String
    ): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_SOURCE, source)

        val result =
            db.insert(TABLE_NAME, null, contentValues)
        return result != -1L
    }

    val listContents: Cursor
        get() {
            val db = this.writableDatabase
            return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        }

    fun clearDatabase() {
        val db = this.writableDatabase
        val clearDBQuery = "DELETE FROM $TABLE_NAME"
        db.execSQL(clearDBQuery)
    }

    companion object {
        const val DATABASE_NAME = "source.db"
        const val TABLE_NAME = "mylist_data"
        const val KEY_ID = "ID"
        const val KEY_SOURCE = "LINK"

    }
}