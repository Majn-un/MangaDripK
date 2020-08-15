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
class FavoriteDB(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, 2) {
    override fun onCreate(db: SQLiteDatabase) {
        val createTable =
            ("CREATE TABLE " + TABLE_NAME + " ("
                    + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME +
                    " TEXT, " + KEY_LINK + " TEXT, " + KEY_THUMB + " TEXT" + ")")
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
        manga: MangaModel
    ): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_LINK, manga.mangaUrl)
        contentValues.put(KEY_NAME, manga.title)
        contentValues.put(KEY_THUMB, manga.imageUrl)
        val result =
            db.insert(TABLE_NAME, null, contentValues)

        //if date as inserted incorrectly it will return -1
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

    fun isEmpty(): Int {
        val db = this.writableDatabase

        val NoOfRows = DatabaseUtils.queryNumEntries(db, TABLE_NAME).toInt()
        return(NoOfRows)

    }

    companion object {
        const val DATABASE_NAME = "mylist.db"
        const val TABLE_NAME = "mylist_data"
        const val KEY_ID = "ID"
        const val KEY_LINK = "LINK"
        const val KEY_NAME = "NAME"
        const val KEY_THUMB = "THUMB"
    }
}