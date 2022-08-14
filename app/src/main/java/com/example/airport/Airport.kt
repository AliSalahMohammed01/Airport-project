package com.example.airport
import android.content.*
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.text.TextUtils
import com.example.airport.Airport.Companion.CONTENT_URI
import com.example.airport.Airport.Companion.CREATE_DB_TABLE
import com.example.airport.Airport.Companion.DATABASE_NAME
import com.example.airport.Airport.Companion.DATABASE_VERSION
import com.example.airport.Airport.Companion.RESERVATIONS_TABLE_NAME
import com.example.airport.Airport.Companion.uriMatcher

class Airport():ContentProvider()
{
    companion object
    {
        val PROVIDER_NAME = "com.example.airport.Airport"
        val URL = "content://" + PROVIDER_NAME + "/airports"
        val CONTENT_URI = Uri.parse(URL)
        val id = "id"
        val NAME = "name"
        val pass_id = "pass_id"
        private val AIRPORT_PROJECTION_MAP: HashMap<String, String>? = null
        val USER =1
        val USER_ID=2
        val uriMatcher: UriMatcher? = null
        val DATABASE_NAME = "reservation"
        val RESERVATIONS_TABLE_NAME = "airports"
        val DATABASE_VERSION = 1
        val CREATE_DB_TABLE = " CREATE TABLE " + RESERVATIONS_TABLE_NAME +   " (id INTEGER PRIMARY KEY AUTOINCREMENT, " + " name TEXT NOT NULL, " +
                " pass_id TEXT NOT NULL);"


    }
    private var sUriMatcher = UriMatcher(UriMatcher.NO_MATCH);
    init
    {       sUriMatcher.addURI(PROVIDER_NAME, "airports", USER);
        sUriMatcher.addURI(PROVIDER_NAME, "airports/#", USER_ID);

    }
    private var db: SQLiteDatabase? = null
    /**
     *
     */
/* Helper class that actually creates and manages * the provider's underlying data repository.
*/
    private class DatabaseHelper internal constructor(context: Context?) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(CREATE_DB_TABLE)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS " + RESERVATIONS_TABLE_NAME)
            onCreate(db)
        }
    }
    override fun onCreate(): Boolean {
        val context = context
        val dbHelper = DatabaseHelper(context)
        db = dbHelper.writableDatabase
        return if (db == null) false else true
    }


    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val rowID = db!!.insert(RESERVATIONS_TABLE_NAME, "", values)
        if (rowID > 0) {
            val _uri = ContentUris.withAppendedId(CONTENT_URI, rowID)
            context!!.contentResolver.notifyChange(_uri, null)
            return _uri
        }
        throw SQLException("Failed to add a record into $uri")
    }




    override fun query(
        uri: Uri, projection: Array<String>?,
        selection: String?, selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        var sortOrder = sortOrder
        val qb = SQLiteQueryBuilder()
        qb.tables = RESERVATIONS_TABLE_NAME
        when (uriMatcher!!.match(uri)) {
            USER_ID -> qb.appendWhere(id + "=" + uri.pathSegments[1])
            else -> { null
            }
        }
        if (sortOrder == null || sortOrder === "") {
            /*** By default sort on student names*/
            sortOrder = NAME
        }
        val c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder)
        c.setNotificationUri(context!!.contentResolver, uri)
        return c
    }
    override fun getType(uri: Uri): String? {
        when (uriMatcher!!.match(uri)) {
            USER -> return "vnd.android.cursor.dir/vnd.example.airports"
            USER_ID -> return "vnd.android.cursor.item/vnd.example.airports"
            else -> throw IllegalArgumentException("Unsupported URI: $uri")
        }
    }
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        var count = 0
        when (uriMatcher!!.match(uri)) {
            USER -> count = db!!.delete(
                RESERVATIONS_TABLE_NAME, selection,
                selectionArgs
            )
            USER_ID -> {
                val id = uri.pathSegments[1]
                count = db!!.delete(
                    RESERVATIONS_TABLE_NAME,
                    id + " = " + id +
                            if (!TextUtils.isEmpty(selection)) " AND ($selection)" else "",
                    selectionArgs
                )
            }
            else -> throw IllegalArgumentException("Unknown URI $uri")
        }
        context!!.contentResolver.notifyChange(uri, null)
        return count
    }


    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        var count = 0
        when (uriMatcher!!.match(uri)) {
            USER -> count = db!!.update(
                RESERVATIONS_TABLE_NAME
                , values, selection,
                selectionArgs
            )
            USER_ID -> count = db!!.update(
                RESERVATIONS_TABLE_NAME,
                values,
                id + " = " + uri.pathSegments[1] + (if (!TextUtils.isEmpty(selection)) " AND ($selection)" else ""),
                selectionArgs
            )
            else -> throw IllegalArgumentException("Unknown URI $uri")
        }
        context!!.contentResolver.notifyChange(uri, null)
        return count
    }







}





