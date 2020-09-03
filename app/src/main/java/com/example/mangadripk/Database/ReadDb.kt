package com.example.mangadripk.Database

import android.R.id
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.mangadripk.Classes.Chapter
import com.example.mangadripk.Classes.Recent


class ReadDb(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, 3) {
    override fun onCreate(db: SQLiteDatabase) {
        val createTable =
            ("CREATE TABLE " + TABLE_NAME + " ("
                    + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME +
                    " TEXT, " + KEY_OG + " TEXT, " + KEY_READ + " TEXT" + ")")
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
        chapter: Chapter
    ): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, chapter.name)
        contentValues.put(KEY_READ, chapter.read)
        contentValues.put(KEY_OG, chapter.OG_Name)
        val result = db.insert(TABLE_NAME, null, contentValues)

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
    fun getID( name: String): Cursor{
        val db = this.writableDatabase
        val query = " SELECT $KEY_ID FROM $TABLE_NAME WHERE $KEY_NAME= '$name'"
        return db.rawQuery(query, null)
    }
    fun deleteData( name:String){
        val db = this.writableDatabase
        val query = ("DELETE FROM " + TABLE_NAME + " WHERE "
                + KEY_NAME + "= '" + name+"'")
        db.execSQL(query)
    }


    companion object {
        const val DATABASE_NAME = "read.db"
        const val TABLE_NAME = "mylist_data"
        const val KEY_ID = "ID"
        const val KEY_NAME = "LINK"
        const val KEY_READ = "NAME"
        const val KEY_OG = "OG"


    }
}