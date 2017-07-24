package com.example.muchbeer.thebestway;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.muchbeer.thebestway.retrieve.RetrieveData;
import com.example.muchbeer.thebestway.retrieve.RetrieveImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private CollectDbHelper db;

   private String handshake = null;

    String IMEI_Number_Holder;
    TelephonyManager telephonyManager;
    public static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 999;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);

        telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.READ_PHONE_STATE)) {

            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.

        } else {

            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},

                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);

            // MY_PERMISSIONS_REQUEST_READ_PHONE_STATE is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new CollectDbHelper(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());



        // Check if user is already logged in or not
        /*if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(MainActivity.this, SuccessPage.class);
            startActivity(intent);
            finish();
        }*/



        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String headerKey = "ewogICJhdXRoTmFtZSI6ICJBSU8yMDE3IiwKICAiYXV0aFRva2VuIjogIjg2ZDRlYzhlNjViMjliYjRlOTE5ZTM0M2I1MWQxMmFjIgp9";

                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    // login user
                    try {
                        checkLogin(headerKey, email, password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {


                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();


                }
            }

        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                Log.d("Gadiel ", "Petering so go");

                //   String headerKey = "43db4b8ab355f9159e94548fc6bcc68a";

                String headerKey = "ewogICJhdXRoTmFtZSI6ICJBSU8yMDE3IiwKICAiYXV0aFRva2VuIjogIjg2ZDRlYzhlNjViMjliYjRlOTE5ZTM0M2I1MWQxMmFjIgp9";

                //  String headerKey = "George";
                String jsonApp = "application/json";

                checkHeaderLogin(headerKey, jsonApp, email, password);


            }
        });
    }

    private void checkPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_PHONE_STATE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);

                // MY_PERMISSIONS_REQUEST_READ_PHONE_STATE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    private void checkHeaderLogin(final String uiHeader, final String jsonApplication,
                                  final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN_WEB, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    //  boolean error = jObj.getBoolean("error");
                    boolean successJson = jObj.getBoolean("Success");

                    // Check for error node in json
                    if (!successJson) {
                        Toast.makeText(getApplication(), "Good very Good",Toast.LENGTH_LONG);

                        // Launch main activity
                        Intent intent = new Intent(MainActivity.this,
                                SuccessPage.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMessage = jObj.getString("colorCode");

                     /* //  String errorMessage = .getString("colorCode");
                        Toast.makeText(getApplicationContext(),
                                errorMessage +" My name is Johnson", Toast.LENGTH_LONG).show();*/
                        Log.d(TAG, "  Code error  " + errorMessage);
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),
                        "Login Error Reading from the server   "+ error.getMessage(), Toast.LENGTH_LONG).show();

                Log.d(TAG, "Login Error reading responses: " + error.getMessage());
                hideDialog();
            }
        }) {

            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                Map<String, Objects> paramsObject = new HashMap<String, Objects>();


                // put values into map
                //    params.put("uInputs", name);
                //   params.put("username", username);
                // map.put("password", password);


                return params;

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("ui-Header", uiHeader);
                headers.put("Content-Type", jsonApplication);

                return headers;
            }
        };

        //  Toast.makeText(getApplication(), "The response is "+ uiHeader, Toast.LENGTH_LONG).show();
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    /**
     * function to verify login details in mysql db
     * */

    private void checkLogin(
            final String uiHeader,
            final String email, final String password) throws JSONException {

        String IMEINumber=telephonyManager.getDeviceId();
        String SIMSerialNumber=telephonyManager.getSimSerialNumber();
        String softwareVersion=telephonyManager.getDeviceSoftwareVersion();
        String deviceName = android.os.Build.MODEL;

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("userName", email);
        jsonObject.put("passWord", password);
        jsonObject.put("Action", "routeController");
        jsonObject.put("apiRouteState", "accountSetup/userProfileLogin");
        jsonObject.put("timeStamp", "2017-07-20 12:41:45");
        jsonObject.put("latitude", "47.211");
        jsonObject.put("longitude", "0.21544");
        jsonObject.put("deviceName", deviceName);
        jsonObject.put("userOS", softwareVersion);
        jsonObject.put("userAgent", "gadiel");
        jsonObject.put("userBrowser", "chrome");

        String uInputs = jsonObject.toString();
       // handshake = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJ3d3cuYWlvdWkuY28udHoiLCJleHAiOjEzMDA4MTkzODAsInVpU3RhdGUiOnsiZnVsbE5hbWUiOiJXZWJieXN0YXIgTi4gTW56YXZhIiwidXNlclBpYyI6Ik1hbGUuZ2lmIiwidXNlckJpcnRoZGF0ZSI6IjIwMTctMDItMDYiLCJ1c2VyR2VuZGVyIjoiTWFsZSIsInBob25lTnVtYmVyIjoiMjU1NzE1MDE1NzU5IiwidXNlclN0YXRlIjoxLCJ1c2VyRGF0ZWQiOiIyMDE3LTA1LTA2IDAwOjUyOjIzIiwicGFzc1dvcmRTZWN1cmVkIjp0cnVlLCJvbmxpbmVTdGF0ZSI6MSwiZGl2TmFtZSI6IkFJTyAtIEhRIiwiZGl2U3RhdGUiOjEsImRvbWFpblN0YXRlIjoxLCJpZENvbXBhbnkiOjEsImNvbXBOYW1lIjoiQUlPVUkiLCJjb21wTG9nbyI6ImRlZmF1bHQuanBnIiwiY29tcFN0YXRlIjoxLCJyb2xlTmFtZSI6IlN5c3RlbSBUZWNobmljaWFucyIsInVpUm9sZVN0YXRlIjoidGVjaFN0YXRlIiwicm9sZVN0YXRlIjoxLCJjb21wbG9jYXRpb24iOiJBcnVzaGEsIFRhbnphbmlhLCBVbml0ZWQgUmVwdWJsaWMgb2YiLCJzZWNCbG9jayI6IjkiLCJzZWNTY29wZSI6MSwic2VjU3BhbiI6MSwiaXNBdXRoZW50aWNhdGVkIjp0cnVlfX0.GNcYTBsUN48q4i8bMCfqesLSzvAUVzPFp4A8_3AwzRM";

        Log.i("Check object " , uInputs);
        Log.i("MainActivity","Check object without stingfy" +jsonObject);

        // conn.setFixedLengthStreamingMode(uInputs.getBytes().length);
        // JSONArray jsonArray = new JSONArray();

        // jsonArray.put(jsonObject);

        // JSONObject nowThisSendRealObject = new JSONObject();
         final String finalObject = uInputs;
        Log.i("LoginActivity ", "Now ready to check connection for uInputs: " + finalObject);


        // Tag used to cancel the request
        String tag_string_req = "req_login";
//String URL_TEST = "http://pastebin.com/raw/2bW31yqa";
        pDialog.setMessage("Logging in ...");
        showDialog();



        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN_WEB, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();
                handshake = response.toString();
                Log.i("MainActivity ", "The handshake is: " + handshake);

                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("Success");

                    // boolean error = jObj.getBoolean("Err");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        //  session.setLogin(true);

                        // Launch main activity
                        Intent intent = new Intent(MainActivity.this,
                                SuccessPage.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error in login. Get the error message
                        String errorMessage = jObj.getString("colorCode");
                        Toast.makeText(getApplicationContext(),
                                errorMessage, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        })
        {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("uInputs", finalObject);
                // params.put("password", password);

                return params;
            }

            JSONObject headerVariables = new JSONObject();


           // String testValue = "Hello, world!";

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();



                if(handshake !=null ) {

                    try {
                        headerVariables.put("authName", "SALESFORCE");
                        headerVariables.put("authToken", "c533b98ca83536d406d08c6896c6a542");
                        headerVariables.put("handShake", handshake);

                       String headerContainer = headerVariables.toString();

                        //byte[] encodeValue = Base64.encode(headerContainer.getBytes(), "UTF-8");

                        byte[] data = headerContainer.getBytes(StandardCharsets.UTF_8);
                        String headerContainerWithThree = Base64.encodeToString(data, Base64.NO_WRAP);

                        //uIHeader must be incoded to base64 after we have the object combine authName and authToken
                        headers.put("ui-Header", headerContainerWithThree);
                        Log.i("MainActivity", "Encode uiHeader with all three "+ headerContainerWithThree);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if(handshake == null) {
                    try {
                        headerVariables.put("authName", "SALESFORCE");
                        headerVariables.put("authToken", "c533b98ca83536d406d08c6896c6a542");

                        String headerContainerWithoutHS = headerVariables.toString();
                        //uIHeader must be incoded to base64 after we have the object combine authName and authToken
                        byte[] encodeValueWHS = Base64.encode(headerContainerWithoutHS.getBytes(), Base64.DEFAULT);

                        String headerContainerWithTwo = Base64.encodeToString(encodeValueWHS, Base64.NO_WRAP);


                        headers.put("ui-Header", headerContainerWithTwo);
                        Log.i("MainActivity", "Encode uiHeader with only "+ headerContainerWithTwo);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                //  headers.put("Content-Type", jsonApplication);

                return headers;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }



    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    public void addUserContentProvider(String name, String email, String uid, String created_at) {

        ContentValues values = new ContentValues();
        values.put(CollectContract.UserEntry.KEY_NAME, name); // Name
        values.put(CollectContract.UserEntry.KEY_EMAIL, email); // Email
        values.put(CollectContract.UserEntry.KEY_UID, uid); // Email
        values.put(CollectContract.UserEntry.KEY_CREATED_AT, created_at); // Created At


        // Receive the new content URI that will allow us to access Toto's data in the future.
        //  Uri newUri = this.getContentResolver().insert(CollectContract.UserEntry.CONTENT_URI, values);

        // Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display_option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_retrieve) {
            Intent openRetrieve = new Intent(MainActivity.this, RetrieveData.class);
            startActivity(openRetrieve);
            return true;
        }

        if (id == R.id.action_retrieve_image) {
            Intent openRetrieveImage = new Intent(MainActivity.this, RetrieveImage.class);
            startActivity(openRetrieveImage);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
