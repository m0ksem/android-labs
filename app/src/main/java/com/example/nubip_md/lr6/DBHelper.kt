package com.example.nubip_md.lr6

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import java.sql.SQLDataException

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 3
        private val DATABASE_NAME = "LR6.db"

        private val TABLE_USER = "user"
        private val COLUMN_USER_ID = "user_id"
        private val COLUMN_USER_NAME = "user_name"
        private val COLUMN_USER_PASSWORD = "user_password"

        private val TABLE_GOODS = "good"
        // var name: String, var price: String, var count: Int
        private val COLUMN_GOOD_ID = "good_id"
        private val COLUMN_GOOD_NAME = "good_name"
        private val COLUMN_GOOD_PRICE = "good_price"
        private val COLUMN_GOOD_COUNT = "good_count"
    }

    var _db: SQLiteDatabase? = null
    var db: SQLiteDatabase
        get() {
            return this._db ?: this.writableDatabase
        }
        set(value) {
            this._db = value
        }

    override fun onCreate(db: SQLiteDatabase) {
        this.db = db

        val CREATE_USER_TABLE_SQL_COMMAND = ("CREATE TABLE " + TABLE_USER + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
                + COLUMN_USER_PASSWORD + " TEXT" + ")")

        db.execSQL(CREATE_USER_TABLE_SQL_COMMAND)

        this.addUser(User(0, "root", "root"))

        val CREATE_GOOD_TABLE_SQL_COMMAND = ("CREATE TABLE " + TABLE_GOODS + "("
            + COLUMN_GOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_GOOD_NAME + " TEXT,"
            + COLUMN_GOOD_PRICE + " INTEGER," + COLUMN_GOOD_COUNT + " INTEGER" + ")")

        db.execSQL(CREATE_GOOD_TABLE_SQL_COMMAND)

        this.addGood(Good(name = "Ryzen 5600", price = 11000, count = 12))
        this.addGood(Good(name = "Ryzen 5800", price = 15000, count = 11))
        this.addGood(Good(name = "Ryzen 5900", price = 23000, count = 6))
        this.addGood(Good(name = "Nvidia 3060", price = 15000, count = 0))
        this.addGood(Good(name = "Nvidia 3070", price = 21000, count = 0))
        this.addGood(Good(name = "Nvidia 3080", price = 30000, count = 0))
        this.addGood(Good(name = "Intel i9 9900k", price = 16000, count = 24))
        this.addGood(Good(name = "Intel i5 9600k", price = 8700, count = 14))
        this.addGood(Good(name = "Intel i7 9700k", price = 11000, count = 4))
        this.addGood(Good(name = "Crucial CX400", price = 2000, count = 16))
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_GOODS")
        onCreate(db)
    }

    fun getAllUser(): ArrayList<User> {
        val columns = arrayOf(COLUMN_USER_ID, COLUMN_USER_NAME, COLUMN_USER_PASSWORD)
        val sortOrder = "$COLUMN_USER_NAME ASC"
        val userList = arrayListOf<User>()

        val cursor = db.query(TABLE_USER,
            columns,
            null,
            null,
            null,
            null,
            sortOrder)
        if (cursor.moveToFirst()) {
            do {
                userList.add(User(id = cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID)).toInt(),
                        name = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)),
                        password = cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD))))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return userList
    }

    fun checkUser(email: String, password: String): Boolean {
        val columns = arrayOf(COLUMN_USER_ID)

        val selection = "$COLUMN_USER_NAME = ? AND $COLUMN_USER_PASSWORD = ?"

        val selectionArgs = arrayOf(email, password)

        val cursor = db.query(TABLE_USER, //Table to query
            columns,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        val cursorCount = cursor.count
        cursor.close()

        if (cursorCount > 0)
            return true

        return false
    }

    fun addUser(user: User) {
        val values = ContentValues()
        values.put(COLUMN_USER_NAME, user.name)
        values.put(COLUMN_USER_PASSWORD, user.password)

        db.insert(TABLE_USER, null, values)
    }

    fun addGood(good: Good) {
        val values = ContentValues()
        values.put(COLUMN_GOOD_NAME, good.name)
        values.put(COLUMN_GOOD_PRICE, good.price)
        values.put(COLUMN_GOOD_COUNT, good.count)

        db.insert(TABLE_GOODS, null, values)
    }

    fun getAllGood(): ArrayList<Good> {
        val columns = arrayOf(COLUMN_GOOD_ID, COLUMN_GOOD_NAME, COLUMN_GOOD_PRICE, COLUMN_GOOD_COUNT)
        val sortOrder = "$COLUMN_GOOD_ID ASC"
        val goodsList = arrayListOf<Good>()

        val cursor = db.query(TABLE_GOODS,
                columns,
                null,
                null,
                null,
                null,
                sortOrder
        )

        if (cursor.moveToFirst()) {
            do {
                goodsList.add(Good(id = cursor.getInt(cursor.getColumnIndex(COLUMN_GOOD_ID)),
                    name = cursor.getString(cursor.getColumnIndex(COLUMN_GOOD_NAME)),
                    price = cursor.getInt(cursor.getColumnIndex(COLUMN_GOOD_PRICE)),
                    count = cursor.getInt(cursor.getColumnIndex(COLUMN_GOOD_COUNT))
                ))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return goodsList
    }

    fun getFilteredGoods(): List<Good> {
        val selectQuery = "SELECT  * FROM $TABLE_GOODS WHERE $COLUMN_GOOD_COUNT < ?"
        val cursor = db.rawQuery(selectQuery, arrayOf("5"))
        val goodsList = arrayListOf<Good>()
        if (cursor.moveToFirst()) {
            do {
                goodsList.add(Good(id = cursor.getInt(cursor.getColumnIndex(COLUMN_GOOD_ID)),
                    name = cursor.getString(cursor.getColumnIndex(COLUMN_GOOD_NAME)),
                    price = cursor.getInt(cursor.getColumnIndex(COLUMN_GOOD_PRICE)),
                    count = cursor.getInt(cursor.getColumnIndex(COLUMN_GOOD_COUNT))
                ))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return goodsList
    }
}