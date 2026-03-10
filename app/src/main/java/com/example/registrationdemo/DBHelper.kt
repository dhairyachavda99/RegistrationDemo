package com.example.registrationdemo

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, "UserDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL(
            "CREATE TABLE users(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "fname TEXT," +
                    "lname TEXT," +
                    "mobile TEXT," +
                    "gender TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        db.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    fun insertUser(fname:String,lname:String,mobile:String,gender:String):Boolean{

        val db = writableDatabase
        val values = ContentValues()

        values.put("fname",fname)
        values.put("lname",lname)
        values.put("mobile",mobile)
        values.put("gender",gender)

        val result = db.insert("users",null,values)

        return result != -1L
    }

    fun getUsers():Cursor{

        val db = readableDatabase
        return db.rawQuery("SELECT * FROM users",null)
    }

    fun updateUser(id:Int,fname:String,lname:String,mobile:String,gender:String):Boolean{

        val db = writableDatabase
        val values = ContentValues()

        values.put("fname",fname)
        values.put("lname",lname)
        values.put("mobile",mobile)
        values.put("gender",gender)

        val result = db.update("users",values,"id=?", arrayOf(id.toString()))

        return result>0
    }

    fun deleteUser(id:Int):Boolean{

        val db = writableDatabase
        val result = db.delete("users","id=?", arrayOf(id.toString()))

        return result>0
    }
}
