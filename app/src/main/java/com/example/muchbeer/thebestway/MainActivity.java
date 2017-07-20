package com.example.muchbeer.thebestway;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        JSONObject jsonObject = new JSONObject();

        JSONObject parentData = new JSONObject();
        JSONObject childData = new JSONObject();

        jsonObject.put("userName", email);
        jsonObject.put("passWord", password);
        jsonObject.put("Action", "routeController");
        jsonObject.put("apiRouteState", "accountSetup/userProfileLogin");
        jsonObject.put("timeStamp", "2017-07-20 12:41:45");
        jsonObject.put("latitude", "47.211");
        jsonObject.put("longitude", "0.21544");
        jsonObject.put("deviceName", "Nexus");
        jsonObject.put("userOS", "Lolipop");
        jsonObject.put("userAgent", "gadiel");
        jsonObject.put("userBrowser", "chrome");


        String uInputs = jsonObject.toString();
        Log.i("Check object " , uInputs);

        // conn.setFixedLengthStreamingMode(uInputs.getBytes().length);
        // JSONArray jsonArray = new JSONArray();

        // jsonArray.put(jsonObject);

        // JSONObject nowThisSendRealObject = new JSONObject();
        parentData.put("uInputs", jsonObject);

        final String finalObject = parentData.toString();
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

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("ui-Header", uiHeader);
                //  headers.put("Content-Type", jsonApplication);

                return headers;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
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


}
