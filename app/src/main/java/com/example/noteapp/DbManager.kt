package com.example.noteapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder

class DbManager(context: Context) {
    val dbName = "MyNotes"
    val tableName = "notes"
    private val colId = "id"
    private val colTitle = "title"
    private val colDesc = "description"
    val createTable = "CREATE TABLE IF NOT EXISTS "+tableName+" ("+colId+" INTEGER PRIMARY KEY, "+
            colTitle+" TEXT, "+colDesc+" TEXT);"
    var sqlDb: SQLiteDatabase? = null
    val dbVersion = 1

    init {
        val db = NotesDatabaseHelper(context)
        sqlDb = db.writableDatabase
    }

    inner class NotesDatabaseHelper(context: Context) :
        SQLiteOpenHelper(context, dbName, null, dbVersion) {

        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(createTable)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("DROP TABLE IF EXISTS $tableName")
        }
    }

    fun insertRecord(contentValues: ContentValues): Long{
        return sqlDb!!.insert(tableName, null, contentValues)
    }

    fun query(projections: Array<String>, selections: String, selectionArgs: Array<String>): Cursor{
        val db = SQLiteQueryBuilder()
        db.tables = tableName
        return db.query(sqlDb, projections, selections, selectionArgs, null, null, null)
    }

    fun deleteRecord(ids: Array<String>){
        sqlDb!!.delete(tableName, "?=id", ids)
    }

    fun editRecord(ids: Array<String>, title: String, desc: String){
        val contentValues = ContentValues()
        contentValues.put(colTitle, title)
        contentValues.put(colDesc, desc)
        sqlDb!!.update(tableName, contentValues, "?=id", ids)
    }
}