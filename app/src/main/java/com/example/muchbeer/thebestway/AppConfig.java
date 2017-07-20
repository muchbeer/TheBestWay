package com.example.muchbeer.thebestway;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * Created by muchbeer on 3/15/2017.
 */

public class AppConfig {

    //Choose location
    public static String getPreferredLocation(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_location_key),
                context.getString(R.string.pref_location_default));
    }
    // Server user login url
    public static String URL_LOGIN = "http://32d3b373.ngrok.io/datacollect/login.php";

    // Server user login url
    public static String URL_LOGIN_URLCONNECTION = "http://32d3b373.ngrok.io/talking/login.inc.php";

    // Server user Webby url
    public static String URL_LOGIN_WEB = "http://192.168.43.124:8080/_AIOUI/matrix/neo.php";

    // Server user Webby url
    public static String URL_LOGIN_WEB_HEADER = "http://32d3b373.ngrok.io/restapi/v1/login";


    // Server user register url
    public static String URL_REGISTER = "http://32d3b373.ngrok.io/datacollect/register.php";

    // Server user register url
    public static String URL_SENDREGION = "http://32d3b373.ngrok.io/datacollect/sendregiontoserver.php";

    //Url for auto collect data from the server
    public static String URL_COLLECT_DATA = "http://32d3b373.ngrok.io/datacollect";

    //Url for auto collect data from the server
    public static String URL_COLLECT_DATA_ONLINE = "http://gdgexpertz.000webhostapp.com";


}
