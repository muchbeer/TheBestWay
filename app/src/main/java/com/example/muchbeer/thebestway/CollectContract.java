package com.example.muchbeer.thebestway;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by muchbeer on 3/13/2017.
 */

public class CollectContract  {

    private CollectContract() {

    }

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "muchbeer.king.datacollect.collectprovider";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.muchbeer.app/regional/ is a valid path for
    // looking at weather data. content://com.example.android.muchbeer.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_REGIONAL = "mikoa";
    public static final String PATH_BRAND = "bidhaa";
    public static final String PATH_RETRIEVE_REGIONAL="receivemikoa";
    //LOGIN CREDENTIAL
    public static final String PATH_USER = "user";


    public static final class RegionalEntry implements BaseColumns {


        public static final Uri CONTENT_URI2 =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REGIONAL).build();

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_REGIONAL);

     /*   alternative
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PETS);*/

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_REGIONAL;

        public static final String CONTENT_ITEM_TYPE2 =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_REGIONAL;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REGIONAL;

      /*  alternative
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PETS;
*/

        public final static String TABLE_NAME = "mikoa";

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REGIONAL;


        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_REGIONAL = "mkoa";
        public final static String COLUMN_DIRECTIONAL = "dira";
        public final static String COLUMN_STATUS_CITY = "city_selected";
        public final static String COLUMN_DELETE_LOCAL_DATA = "del_local_data";
        public final static String COLUMN_LATITUDE = "latitude";
        public final static String COLUMN_LONGITUDE = "longitude";
        // The regional setting string is what will be sent to our regional
        // as the regional query.
        public static final String COLUMN_REGIONAL_SETTING = "regional_setting";

        public static Uri buildRegionalUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);

        }


        public static String getRegionalSettingFromUri(Uri uri) {
            return uri.getQueryParameter(COLUMN_REGIONAL_SETTING);
        }

        // Gender value constants:
        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;
    }


    public static final class ReceiveRegionalEntry implements BaseColumns {


        public static final Uri CONTENT_URI2 =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RETRIEVE_REGIONAL).build();

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_RETRIEVE_REGIONAL);

     /*   alternative
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PETS);*/

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_RETRIEVE_REGIONAL;

        public static final String CONTENT_ITEM_TYPE2 =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_RETRIEVE_REGIONAL;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RETRIEVE_REGIONAL;

      /*  alternative
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PETS;
*/

        public final static String TABLE_NAME = "receivemkoa";

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RETRIEVE_REGIONAL;


        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_REGIONAL = "mkoa";
        public final static String COLUMN_DIRECTIONAL = "dira";
        public final static String COLUMN_STATUS_CITY = "city_selected";
        public final static String COLUMN_DELETE_LOCAL_DATA = "del_local_data";


        // The regional setting string is what will be sent to our regional
        // as the regional query.
        public static final String COLUMN_REGIONAL_SETTING = "regional_setting";

        public static Uri buildRegionalUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);

        }

        public static String getRegionalSettingFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String getSearchbyRegionalSetting(Uri uri) {
            return uri.getQueryParameter(COLUMN_REGIONAL_SETTING);
        }

        public static Uri buildRegionalDetailSetting(String regional_setting) {
            return CONTENT_URI.buildUpon().appendPath(regional_setting).build();
        }

        // Gender value constants:
        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;
    }
    /* Inner class that defines the table contents of the weather table */
    public static final class BrandEntry implements BaseColumns {


        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_BRAND).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_BRAND;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_BRAND;


        public static String TABLE_NAME = "bidhaa";

        public final static String _ID = "id";

        // Column with the foreign key into the regional table.
        public static final String COLUMN_REGIONAL_KEY = "regional_id";
        // Date, stored as Text with format yyyy-MM-dd
        public static final String COLUMN_DATETEXT = "regional_date";

        // Jina la bidbhaa
        public static final String COLUMN_BRAND_NAME = "jina_la_bidhaa";

        // Logistic
        public static final String COLUMN_MOST_TAKEN = "jinsia_pendeka";


        // Short description and long description of the brand
        // e.g "Inapendwa" vs "Haipendwi".
        public static final String COLUMN_DESC_PREFERENCES = "bidhaa_upendeleo";

        public static Uri buildBrandUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        //This is almight uri that is basically targeting location editing
        public static Uri buildCollectregional(String regionalSetting) {
            return CONTENT_URI.buildUpon().appendPath(regionalSetting).build();
        }

        public static Uri buildCollectRegionalWithStartDate(String regionalSetting, String startDate) {
            return CONTENT_URI.buildUpon().appendPath(regionalSetting)
                    .appendQueryParameter(COLUMN_DATETEXT, startDate).build();
        }

        public static Uri buildCollectRegionalWithBrand(String locationSetting, String date) {
            return CONTENT_URI.buildUpon().appendPath(locationSetting).appendPath(date).build();
        }

        public static String getRegionalSettingFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String getDateFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }

        public static String getStartDateFromUri(Uri uri) {
            return uri.getQueryParameter(COLUMN_DATETEXT);
        }
    }

    public static final class UserEntry implements BaseColumns {


        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER).build();
     /*   alternative
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PETS);*/

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_USER;

        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_USER;
      /*  alternative
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PETS;
*/

        public final static String TABLE_NAME = "users";

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;

        public static final String KEY_ID = "id";
        public static final String KEY_NAME = "name";
        public static final String KEY_EMAIL = "email";
        public static final String KEY_UID = "uid";
        public static final String KEY_CREATED_AT = "created_at";

         // The regional setting string is what will be sent to our regional


        public static Uri buildUserUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

           }
}
