package com.example.muchbeer.thebestway;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.util.HashMap;

import static android.content.ContentValues.TAG;

/**
 * Created by muchbeer on 3/13/2017.
 */

public class CollectDbHelper extends SQLiteOpenHelper {

 //   private static final HashMap<String,String> mColumnMap = buildColumnMap();

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;

    public static final String DATABASE_NAME = "collectinfo.db";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";

    // Login table name
    private static final String TABLE_USER = "users";

    public static final String LOG_TAG = CollectDbHelper.class.getSimpleName();

    public CollectDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }





    //MainActivity callregister = new MainActivity();

    //  MainActivity getCallregister= (MainActivity)

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create a String that contains the SQL statement to create the Regional table
        String SQL_CREATE_REGIONAL_TABLE =  "CREATE TABLE " + CollectContract.RegionalEntry.TABLE_NAME + " ("
                + CollectContract.RegionalEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CollectContract.RegionalEntry.COLUMN_REGIONAL + " TEXT NOT NULL, "
                + CollectContract.RegionalEntry.COLUMN_DIRECTIONAL + " TEXT NOT NULL, "
                +  CollectContract.RegionalEntry.COLUMN_STATUS_CITY + " TEXT NOT NULL, "
                + CollectContract.RegionalEntry.COLUMN_REGIONAL_SETTING + " TEXT NOT NULL, "
                + CollectContract.RegionalEntry.COLUMN_DELETE_LOCAL_DATA + " TEXT NULL, "
                + CollectContract.RegionalEntry.COLUMN_LATITUDE + " TEXT NULL, "
                + CollectContract.RegionalEntry.COLUMN_LONGITUDE + " TEXT NULL, "  +
                "UNIQUE (" + CollectContract.RegionalEntry.COLUMN_REGIONAL +") ON CONFLICT IGNORE"+
                " );";
                /*+ DbEntry.COLUMN_DB_WEIGHT + " INTEGER NOT NULL DEFAULT 0);";*/


        // Create a String that contains the SQL statement to create the Regional table
        String SQL_CREATE_REGIONAL_RETRIEVING_TABLE =  "CREATE TABLE " + CollectContract.ReceiveRegionalEntry.TABLE_NAME + " ("
                + CollectContract.ReceiveRegionalEntry._ID + " INTEGER PRIMARY KEY, "
                + CollectContract.ReceiveRegionalEntry.COLUMN_REGIONAL + " TEXT NOT NULL, "
                + CollectContract.ReceiveRegionalEntry.COLUMN_DIRECTIONAL + " TEXT NOT NULL, "
                +  CollectContract.ReceiveRegionalEntry.COLUMN_STATUS_CITY + " TEXT NOT NULL, "
                + CollectContract.ReceiveRegionalEntry.COLUMN_REGIONAL_SETTING + " TEXT UNIQUE NOT NULL, "
                + CollectContract.ReceiveRegionalEntry.COLUMN_DELETE_LOCAL_DATA + " TEXT NULL, "  +

                "UNIQUE (" + CollectContract.ReceiveRegionalEntry.COLUMN_REGIONAL +") ON CONFLICT IGNORE"+
                " );";

        final String SQL_CREATE_LOGIN_TABLE = "CREATE VIRTUAL TABLE " + CollectContract.UserEntry.TABLE_NAME + " USING fts3("
                + CollectContract.UserEntry.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CollectContract.UserEntry.KEY_NAME + " TEXT, "
                + CollectContract.UserEntry.KEY_EMAIL + " TEXT UNIQUE, "
                + CollectContract.UserEntry.KEY_UID + " TEXT, "
                + CollectContract.UserEntry.KEY_CREATED_AT + " TEXT" + ")";


        final String SQL_CREATE_BRAND_TABLE = "CREATE VIRTUAL TABLE " + CollectContract.BrandEntry.TABLE_NAME + " USING fts3(" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                CollectContract.BrandEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with this weather data
                CollectContract.BrandEntry.COLUMN_REGIONAL_KEY + " INTERGER NOT NULL, " +
                CollectContract.BrandEntry.COLUMN_DATETEXT + " TEXT NULL, " +

                CollectContract.BrandEntry.COLUMN_DESC_PREFERENCES + " TEXT NULL, " +
                CollectContract.BrandEntry.COLUMN_MOST_TAKEN + " TEXT NULL," +
                CollectContract.BrandEntry.COLUMN_BRAND_NAME + " TEXT NULL, " +


                // Set up the regional column as a foreign key to regional table.
                " FOREIGN KEY (" + CollectContract.BrandEntry.COLUMN_REGIONAL_KEY + ") REFERENCES " +
                CollectContract.RegionalEntry.TABLE_NAME + " (" + CollectContract.RegionalEntry._ID + ") " +

                // To assure the application have just one data entry per day
                // per regional, it's created a UNIQUE constraint with REPLACE strategy
                " UNIQUE (" + CollectContract.BrandEntry.COLUMN_DATETEXT + ", " +
                CollectContract.BrandEntry.COLUMN_REGIONAL_KEY + ") ON CONFLICT REPLACE);";


        // Execute the SQL statement
        db.execSQL(SQL_CREATE_REGIONAL_TABLE);
        db.execSQL(SQL_CREATE_BRAND_TABLE);
        db.execSQL(SQL_CREATE_LOGIN_TABLE);
        db.execSQL(SQL_CREATE_REGIONAL_RETRIEVING_TABLE);

        Log.d(TAG, "Database tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        db.execSQL("DROP TABLE IF EXISTS " + CollectContract.RegionalEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CollectContract.BrandEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CollectContract.ReceiveRegionalEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CollectContract.UserEntry.TABLE_NAME);
        onCreate(db);
    }


    //USER LOGIN AND REGISTER CREDENTIAL
    /**
     * Storing user details in database
     */
    public void addUser(String name, String email, String uid, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_UID, uid); // Email
        values.put(KEY_CREATED_AT, created_at); // Created At


        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }


    /**
     * Getting user data from database
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + CollectContract.UserEntry.TABLE_NAME;


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // Move to first row of user data
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put(CollectContract.UserEntry.KEY_NAME, cursor.getString(cursor.getColumnIndex(CollectContract.UserEntry.KEY_NAME)));
            user.put(CollectContract.UserEntry.KEY_EMAIL, cursor.getString(cursor.getColumnIndex(CollectContract.UserEntry.KEY_EMAIL)));
            user.put(CollectContract.UserEntry.KEY_UID, cursor.getString(cursor.getColumnIndex(CollectContract.UserEntry.KEY_UID)));
            user.put(CollectContract.UserEntry.KEY_CREATED_AT, cursor.getString(cursor.getColumnIndex(CollectContract.UserEntry.KEY_CREATED_AT)));
        }
        cursor.close();



        db.close();
        // return user
        Log.d("GET USER DETAIL ","Fetching user from sqlite" + user.toString());
        // Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Re crate database Delete all tables and create them again
     */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }


   /* Now implement search functionality*/

    /**
     * Builds a map for all columns that may be requested, which will be given to the
     * SQLiteQueryBuilder. This is a good way to define aliases for column names, but must include
     * all columns, even if the value is the key. This allows the ContentProvider to request
     * columns w/o the need to know real column names and create the alias itself.
     */

    /**
     * Returns a Cursor positioned at the word specified by rowId
     *
     * @param query id of regional to retrieve
     * @param columns The columns to include, if null then all are included
     * @return Cursor positioned to matching word, or null if not found.
     */
    public Cursor getRegion(String query, String[] columns) {
        String selection = CollectContract.RegionalEntry.COLUMN_REGIONAL + " MATCH ?";
        String[] selectionArgs = new String[] {query+"*"};

        return query(selection, selectionArgs, columns);
    }



    /**
     * Returns a Cursor over all words that match the given query
     *
     * @param query The string to search for
     * @param columns The columns to include, if null then all are included
     * @return Cursor over all words that match, or null if none found.
     */


    /**
     * Performs a database query.
     * @param selection The selection clause
     * @param selectionArgs Selection arguments for "?" components in the selection
     * @param columns The columns to return
     * @return A Cursor over all rows matching the query
     */
   private Cursor query(String selection, String[] selectionArgs, String[] columns) {

       SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
       builder.setTables(CollectContract.RegionalEntry.TABLE_NAME);

       Cursor cursor = builder.query(this.getReadableDatabase(),
               columns, selection, selectionArgs, null, null, null);

       if (cursor == null) {
           return null;
       } else if (!cursor.moveToFirst()) {
           cursor.close();
           return null;
       }
       return cursor;
    }



}