package formationapps.helper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by caliber fashion on 5/2/2017.
 */

public class BgJsonDB extends SQLiteOpenHelper {  // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "bgjsondb";

    // Contacts table name
    private static final String TABLE_TEMPLATE = "bg";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";

    public BgJsonDB(Context context) {
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public BgJsonDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_TEMPLATE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEMPLATE);

        // Create tables again
        onCreate(db);
    }

    public long BG_insertJson(String json) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, 0);
        values.put(KEY_NAME, json + "");
        long l = db.insertWithOnConflict(TABLE_TEMPLATE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        return l;
    }

    public String BG_getJsonString() {
        String selectQuery = "SELECT  * FROM " + TABLE_TEMPLATE;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String json = null;
        if (cursor.moveToFirst())
            json = cursor.getString(1);
        return json;
    }
}