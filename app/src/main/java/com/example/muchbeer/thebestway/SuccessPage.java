package com.example.muchbeer.thebestway;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class SuccessPage extends AppCompatActivity {

    private static final int PAYLOAD = 1;
    private static final int JWT_PARTS = 3;
    TextView displayData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_page);

        displayData =(TextView) findViewById(R.id.txt_name);
String ourTokenIs = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJ3d3cuYWlvdWkuY28udHoiLCJleHAiOjEzMDA4MTkzODAsInVpU3RhdGUiOnsiZnVsbE5hbWUiOiJXZWJieXN0YXIgTi4gTW56YXZhIiwidXNlclBpYyI6Ik1hbGUuZ2lmIiwidXNlckJpcnRoZGF0ZSI6IjIwMTctMDItMDYiLCJ1c2VyR2VuZGVyIjoiTWFsZSIsInBob25lTnVtYmVyIjoiMjU1NzE1MDE1NzU5IiwidXNlckRhdGVkIjoiMjAxNy0wNS0wNiAwMDo1MjoyMyIsInBhc3NXb3JkU2VjdXJlZCI6dHJ1ZSwib25saW5lU3RhdGUiOjEsImRpdk5hbWUiOiJBSU8gLSBIUSIsImNvbXBOYW1lIjoiQUlPVUkiLCJjb21wTG9nbyI6ImRlZmF1bHQuanBnIiwicm9sZU5hbWUiOiJTeXN0ZW0gVGVjaG5pY2lhbnMiLCJ1aVJvbGVTdGF0ZSI6InRlY2hTdGF0ZSIsImNvbXBsb2NhdGlvbiI6IkFydXNoYSwgVGFuemFuaWEsIFVuaXRlZCBSZXB1YmxpYyBvZiIsInNlY0Jsb2NrIjoiOSIsInNlY1Njb3BlIjoxLCJzZWNTcGFuIjoxLCJpc0F1dGhlbnRpY2F0ZWQiOnRydWV9fQ.sIuP8m-8f5MPFVO8f_xtC1i2iMzTjypZ1grpfqWUDsY";
String receiveToken = getPayload(ourTokenIs);
        JSONObject readWebbyName, insideState;
        String readName = null;
        try {
           readWebbyName  = new JSONObject(receiveToken);
            insideState = readWebbyName.getJSONObject("uiState");
            readName = insideState.getString("fullName");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.d( "Our data decrypt is: ", receiveToken );

        displayData.setText("Mr. " + readName);
    }

    /**
     * Returns payload of a JWT as a JSON object.
     *
     * @param jwt       REQUIRED: valid JSON Web Token as String.
     * @return payload as a JSONObject.
     */
    public static String getPayload(String jwt) {
        String jsonData= null;
        try {
            validateJWT(jwt);
             String payload = jwt.split("\\.")[PAYLOAD];
             byte[] sectionDecoded = Base64.decode(payload, Base64.URL_SAFE);

             String jwtSection = new String(sectionDecoded, "UTF-8");
            Log.i("Decoded data is: " , jwtSection);
           jsonData = new JSONObject(jwtSection).toString();
            Log.i("Decoded JSON is: ", jsonData);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return  jsonData;
    }


    /**
     * Checks if {@code JWT} is a valid JSON Web Token.
     *
     * @param jwt REQUIRED: The JWT as a {@link String}.
     */
    public static void validateJWT(String jwt) {
        // Check if the the JWT has the three parts
        final String[] jwtParts = jwt.split("\\.");
        if (jwtParts.length != JWT_PARTS) {
           Log.d("Errors: " , "not a JSON Web Token");

        }
    }
}
