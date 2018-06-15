package com.example.viram.echo.Databases

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.viram.echo.Songs

/**
 * Created by Viram on 5/26/2018.
 */
class EchoDatabase : SQLiteOpenHelper {
    var _songList: ArrayList<Songs> = arrayListOf()
    var count: Int = 0

    object Staticated {
        var DB_VERSION = 1
        val DB_NAME = "FavoriteDatabase"
        val TABLE_NAME = "FavoriteTable"
        val COLUMN_ID = "SongId"
        val COLUMN_SONG_TITLE = "SongTitle"
        val COLUMN_SONG_ARTIST = "SongArtist"
        val COLUMN_SONG_PATH = "SongPath"
    }

    override fun onCreate(sqlliteDatabase: SQLiteDatabase) {
        sqlliteDatabase.execSQL("CREATE TABLE " + Staticated.TABLE_NAME + "( " + Staticated.COLUMN_ID + " LONG," + Staticated.COLUMN_SONG_ARTIST + " STRING," + Staticated.COLUMN_SONG_TITLE +
                " STRING," + Staticated.COLUMN_SONG_PATH + " STRING);")

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    constructor(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : super(context, name, factory, version)
    constructor(context: Context?) : super(context, Staticated.DB_NAME, null, Staticated.DB_VERSION)

    fun storeAsFavorite(id: Int, artist: String, songTitle: String, path: String) {
        if (checkifIdExists(id)) {

        } else {
            var db = this.writableDatabase
            var contentValues = ContentValues()
            contentValues.put(Staticated.COLUMN_ID, id)
            contentValues.put(Staticated.COLUMN_SONG_ARTIST, artist)
            contentValues.put(Staticated.COLUMN_SONG_TITLE, songTitle)
            contentValues.put(Staticated.COLUMN_SONG_PATH, path)

            db.insert(Staticated.TABLE_NAME, null, contentValues)
            db.close()
        }
    }

    fun queryDBList(): ArrayList<Songs> {
        val db = this.readableDatabase
        val query_params = "SELECT * FROM " + Staticated.TABLE_NAME
        val cursor = db.rawQuery(query_params, null)

        if (cursor.moveToFirst()) {
            do {
                val _id = cursor.getInt(cursor.getColumnIndexOrThrow(Staticated.COLUMN_ID))
                val _artist = cursor.getString(cursor.getColumnIndexOrThrow(Staticated.COLUMN_SONG_ARTIST))
                val _title = cursor.getString(cursor.getColumnIndexOrThrow(Staticated.COLUMN_SONG_TITLE))
                val _songPath = cursor.getString(cursor.getColumnIndexOrThrow(Staticated.COLUMN_SONG_PATH))
                _songList.add(Songs(_id.toLong(), _title, _artist, _songPath, 0))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return _songList
    }
    fun queryDBList(i: Int): Int {
        var res: Int=0
        var _id:Int=0
        val db = this.readableDatabase
        val query_params = "SELECT * FROM " + Staticated.TABLE_NAME
        val cursor = db.rawQuery(query_params, null)

        if (cursor.moveToFirst()) {
            do {
                _id = cursor.getInt(cursor.getColumnIndexOrThrow(Staticated.COLUMN_ID))
                val _artist = cursor.getString(cursor.getColumnIndexOrThrow(Staticated.COLUMN_SONG_ARTIST))
                val _title = cursor.getString(cursor.getColumnIndexOrThrow(Staticated.COLUMN_SONG_TITLE))
                val _songPath = cursor.getString(cursor.getColumnIndexOrThrow(Staticated.COLUMN_SONG_PATH))
                res=res+1
            } while (cursor.moveToNext() && res!=i)
        }
        cursor.close()
        return _id
    }
    fun checkifpathExists(_id: String): Boolean {
        var storepath = "t"
        val db = this.readableDatabase
        val queryparams = "SELECT * FROM " + Staticated.TABLE_NAME + " WHERE " + Staticated.COLUMN_SONG_PATH + " = " + _id
        val cursor = db.rawQuery(queryparams, null)
        if (cursor.moveToFirst()) {
            do {
                storepath = cursor.getString(cursor.getColumnIndexOrThrow(Staticated.COLUMN_SONG_PATH))
            } while (cursor.moveToNext())

        } else {
            cursor.close()
            return false
        }
        return storepath != "t"
    }

    fun checkifIdExists(_id: Int): Boolean {
        var storeId = -1306
        val db = this.readableDatabase
        val queryparams = "SELECT * FROM " + Staticated.TABLE_NAME + " WHERE " + Staticated.COLUMN_ID + " = " + _id
        val cursor = db.rawQuery(queryparams, null)
        if (cursor.moveToFirst()) {
            do {
                storeId = cursor.getInt(cursor.getColumnIndexOrThrow(Staticated.COLUMN_ID))
            } while (cursor.moveToNext())

        } else {
            cursor.close()
            return false
        }
        return storeId != -1306
    }

    fun howmany(id: Int): Int {
        var storeId = id

        val db = this.readableDatabase
        val queryparams = "SELECT DISTINCT(*) FROM " + Staticated.TABLE_NAME + " WHERE " + Staticated.COLUMN_ID + " = " + id
        val cursor = db.rawQuery(queryparams, null)
        if (cursor.moveToFirst()) {
            do {
                if (storeId == cursor.getInt(cursor.getColumnIndexOrThrow(Staticated.COLUMN_ID))) {
                    count = count + 1
                }
            } while (cursor.moveToNext())

        }
        return count
    }


    fun deleteFavorite(_id: Int) {
        var db = this.writableDatabase
        db.delete(Staticated.TABLE_NAME, Staticated.COLUMN_ID + " = " + _id, null)
        db.close()
    }

    fun checkSize(): Int {
        var c: Int = 0
        var db = this.readableDatabase
        var queryparams = "SELECT * FROM " + Staticated.TABLE_NAME
        var cursor = db.rawQuery(queryparams, null)
        if (cursor.moveToFirst()) {
            do {
                c = c + 1
            } while (cursor.moveToNext())
        } else {
            return c
        }
        return c
    }

}