package com.example.noteapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder

class VoiceNotesDbManager(val context: Context): SQLiteOpenHelper(context, Constants().V_DB_NAME, null, Constants().V_DB_VERSION) {

    var sqlDb: SQLiteDatabase? = null
    private val createTable = "CREATE TABLE IF NOT EXISTS ${Constants().V_TABLE_NAME}" +
            "( "+Constants().V_ID+" INTEGER PRIMARY KEY, "+
            Constants().V_COL_TITLE+" TEXT, "+
            Constants().V_COL_DIR+" TEXT, " +
            Constants().V_COL_DATE+" TEXT);"

    init {
        sqlDb = this.writableDatabase
    }
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS ${Constants().V_TABLE_NAME}")
    }

    fun insertVoiceNote(title: String, directory: String, date: String): Long{
        val contentValues = ContentValues()
        contentValues.put(Constants().V_COL_TITLE, title)
        contentValues.put(Constants().V_COL_DIR, directory)
        contentValues.put(Constants().V_COL_DATE, date)
        return sqlDb!!.insert(Constants().V_TABLE_NAME, null, contentValues)
    }

    fun query(projections: Array<String>, selection: String, selectionArgs: Array<String>):Cursor{
        val db = SQLiteQueryBuilder()
        db.tables = Constants().V_TABLE_NAME
        return db.query(sqlDb, projections, selection, selectionArgs, null, null, null)
    }
}