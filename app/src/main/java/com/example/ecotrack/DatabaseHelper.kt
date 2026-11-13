package com.example.ecotrack

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "ecotrack.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_NAME = "water_entries"
        const val COLUMN_ID = "id"
        const val COLUMN_AMOUNT = "amount"
        const val COLUMN_DATE = "date"
        const val COLUMN_LATITUDE = "latitude"
        const val COLUMN_LONGITUDE = "longitude"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_AMOUNT INTEGER NOT NULL,
                $COLUMN_DATE TEXT NOT NULL,
                $COLUMN_LATITUDE REAL,
                $COLUMN_LONGITUDE REAL
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    /**
     * Inserts a new water entry into SQLite database.
     */
    fun insertEntry(amount: Int, date: String, latitude: Double?, longitude: Double?): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_AMOUNT, amount)
            put(COLUMN_DATE, date)
            if (latitude != null) put(COLUMN_LATITUDE, latitude) else putNull(COLUMN_LATITUDE)
            if (longitude != null) put(COLUMN_LONGITUDE, longitude) else putNull(COLUMN_LONGITUDE)
        }
        val result = db.insert(TABLE_NAME, null, values)
        db.close()
        return result
    }

    /**
     * Retrieves all water entries sorted newest first.
     */
    fun getAllEntries(): List<WaterEntry> {
        val entryList = mutableListOf<WaterEntry>()
        val selectQuery = "SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_ID DESC"
        val db = readableDatabase
        val cursor: Cursor? = db.rawQuery(selectQuery, null)

        cursor?.use {
            if (it.moveToFirst()) {
                val idIndex = it.getColumnIndexOrThrow(COLUMN_ID)
                val amountIndex = it.getColumnIndexOrThrow(COLUMN_AMOUNT)
                val dateIndex = it.getColumnIndexOrThrow(COLUMN_DATE)
                val latIndex = it.getColumnIndexOrThrow(COLUMN_LATITUDE)
                val lngIndex = it.getColumnIndexOrThrow(COLUMN_LONGITUDE)

                do {
                    val id = it.getLong(idIndex)
                    val amount = it.getInt(amountIndex)
                    val date = it.getString(dateIndex)
                    val lat = if (it.isNull(latIndex)) null else it.getDouble(latIndex)
                    val lng = if (it.isNull(lngIndex)) null else it.getDouble(lngIndex)

                    entryList.add(WaterEntry(id, amount, date, lat, lng))
                } while (it.moveToNext())
            }
        }
        db.close()
        return entryList
    }

    /**
     * Calculates the total water consumed today in ml.
     */
    fun getTodayTotalAmount(): Int {
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val db = readableDatabase
        val query = "SELECT SUM($COLUMN_AMOUNT) FROM $TABLE_NAME WHERE $COLUMN_DATE LIKE ?"
        val cursor = db.rawQuery(query, arrayOf("$todayStr%"))

        var total = 0
        cursor?.use {
            if (it.moveToFirst()) {
                total = it.getInt(0)
            }
        }
        db.close()
        return total
    }

    /**
     * Deletes a single entry by ID.
     */
    fun deleteEntry(id: Long): Boolean {
        val db = writableDatabase
        val rows = db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
        return rows > 0
    }

    /**
     * Clears all entries from database.
     */
    fun clearAllEntries(): Boolean {
        val db = writableDatabase
        val rows = db.delete(TABLE_NAME, null, null)
        db.close()
        return rows >= 0
    }
}
