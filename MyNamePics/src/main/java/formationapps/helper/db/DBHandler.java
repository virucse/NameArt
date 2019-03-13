package formationapps.helper.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.formationapps.nameart.BuildConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caliber fashion on 4/27/2017.
 */

public class DBHandler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "myNameDatabase";

    // Contacts table name
    private static final String TABLE_CONTACTS = "contacts";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_NO = "phone_number";
    public static String TABLENAME = "";
    private SharedPreferences mSharedPreferences;

    public DBHandler(Context context, String tableName) {
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
        TABLENAME = tableName;
    }

    public DBHandler(Context context, String dbname, String tableName) {
        this(context, dbname, null, DATABASE_VERSION);
        TABLENAME = tableName;
    }

    public DBHandler(Context context, String dbName, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dbName, factory, version);
        mSharedPreferences = context.getSharedPreferences(context.getPackageName() + "db", Context.MODE_PRIVATE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (TABLENAME.equals(DBUtil.SERVER_FONT_TABLE)) {
            String CREATE_CONTACTS_TABLE = "CREATE TABLE " + DBUtil.SERVER_FONT_TABLE + "("
                    + DBUtil.SERVER_FONT_ID + " INTEGER PRIMARY KEY," + DBUtil.SERVER_FONT_PNGNAME + " TEXT,"
                    + DBUtil.SERVER_FONT_TTFNAME + " TEXT" + ")";
            db.execSQL(CREATE_CONTACTS_TABLE);
        }/*else  if(TABLENAME.equals(DBUtil.TABLE_TEMPLATE_NAME)){
            String CREATE_CONTACTS_TABLE = "CREATE TABLE " + DBUtil.TABLE_TEMPLATE_NAME + "("
                    + KEY_ID + " INTEGER PRIMARY KEY," + DBUtil.TEMPLATE_FILE_NAME + " TEXT," + ")";
            db.execSQL(CREATE_CONTACTS_TABLE);
        }else  if(TABLENAME.equals(DBUtil.TEMPLATE_TABLE_JSON)){
            String CREATE_CONTACTS_TABLE = "CREATE TABLE " + DBUtil.TEMPLATE_TABLE_JSON + "("
                    + KEY_ID + " INTEGER PRIMARY KEY," + DBUtil.TEMPLATE_JSON_STRING + " TEXT," + ")";
            db.execSQL(CREATE_CONTACTS_TABLE);
        }else  if(TABLENAME.equals(DBUtil.STICKER_TABLE_JSON)){
            String CREATE_CONTACTS_TABLE = "CREATE TABLE " + DBUtil.STICKER_TABLE_JSON + "("
                    + KEY_ID + " INTEGER PRIMARY KEY," + DBUtil.STICKER_JSON_STRING + " TEXT," + ")";
            db.execSQL(CREATE_CONTACTS_TABLE);
        }else  if(TABLENAME.equals(DBUtil.STICKERS_TABLE_NAME)){
            String CREATE_CONTACTS_TABLE = "CREATE TABLE " + DBUtil.STICKERS_TABLE_NAME + "("
                    + KEY_ID + " INTEGER PRIMARY KEY," + DBUtil.STICKER_FILE_NAME + " TEXT," + ")";
            db.execSQL(CREATE_CONTACTS_TABLE);
        }*/ else {
            if (BuildConfig.DEBUG) {
                Log.i("DBHandler.onCreate", "No table Name match");
            }
            String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                    + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                    + KEY_PH_NO + " TEXT" + ")";
            db.execSQL(CREATE_CONTACTS_TABLE);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(db);
    }

    public void addServerFontName(List<ServerFont> serverFonts) {
        if (mSharedPreferences.getBoolean(DBUtil.SERVER_FONT_FIRST_LAUNCH_KEY, true)) {
            insertFontNameToDB(serverFonts);
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(DBUtil.SERVER_FONT_FIRST_LAUNCH_KEY, false);
            editor.putInt(DBUtil.SERVER_FONT_TOTAL_COUNT, serverFonts.size());
            editor.commit();
        } else if (serverFonts.size() > mSharedPreferences.getInt(DBUtil.SERVER_FONT_TOTAL_COUNT, 0)) {
            insertFontNameToDB(serverFonts);
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putInt(DBUtil.SERVER_FONT_TOTAL_COUNT, serverFonts.size());
            editor.commit();
        }

    }

    private void insertFontNameToDB(List<ServerFont> serverFonts) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (ServerFont serverFont : serverFonts) {
            ContentValues values = new ContentValues();
            values.put(DBUtil.SERVER_FONT_PNGNAME, serverFont.getPngName());
            values.put(DBUtil.SERVER_FONT_TTFNAME, serverFont.geTttfName());
            db.insertWithOnConflict(DBUtil.SERVER_FONT_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        }
        db.close();
    }

    public List<ServerFont> getServerFontList() {
        List<ServerFont> mServerFontList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + DBUtil.SERVER_FONT_TABLE;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ServerFont sf = new ServerFont();
                sf.setID(Integer.parseInt(cursor.getString(0)));
                sf.setPngName(cursor.getString(1));
                sf.setTtfName(cursor.getString(2));
                mServerFontList.add(sf);

            } while (cursor.moveToNext());
        }
        return mServerFontList;
    }

    public void Template_insertJson(String json) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBUtil.TEMPLATE_JSON_STRING, json);
        db.insertWithOnConflict(DBUtil.TEMPLATE_TABLE_JSON, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public String Template_getJsonString() {
        String selectQuery = "SELECT  * FROM " + DBUtil.TEMPLATE_TABLE_JSON;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String json = null;
        if (cursor.moveToFirst())
            json = cursor.getColumnName(1);
        return json;
    }

    public void Template_insertFileName(TemplateData td) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues val = new ContentValues();
        val.put(DBUtil.TEMPLATE_FILE_NAME, td.getTemplateFile());
        db.insertWithOnConflict(DBUtil.TABLE_TEMPLATE_NAME, null, val, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public List<TemplateData> Template_getAllFile() {
        String selectQuery = "SELECT  * FROM " + DBUtil.TEMPLATE_TABLE_JSON;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<TemplateData> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                TemplateData td = new TemplateData();
                td.setTemplateId(cursor.getColumnName(0) + "");
                td.setTemplateFile(cursor.getColumnName(1));
                list.add(td);
            } while (cursor.moveToNext());
        }

        return list;
    }

    public TemplateData Template_getFile(String id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBUtil.TABLE_TEMPLATE_NAME, new String[]{KEY_ID,
                        DBUtil.TEMPLATE_FILE_NAME}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        TemplateData td = null;
        if (cursor != null) {
            cursor.moveToFirst();
            td = new TemplateData();
            td.setTemplateId(cursor.getString(0) + "");
            td.setTemplateFile(cursor.getColumnName(1));
        }
        return td;
    }

}
