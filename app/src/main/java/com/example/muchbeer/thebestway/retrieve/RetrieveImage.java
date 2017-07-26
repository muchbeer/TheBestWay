package com.example.muchbeer.thebestway.retrieve;

import android.app.ProgressDialog;
import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.muchbeer.thebestway.AppConfig;
import com.example.muchbeer.thebestway.AppController;
import com.example.muchbeer.thebestway.ItemPojo;
import com.example.muchbeer.thebestway.R;
import com.example.muchbeer.thebestway.util.NetworkController;
import com.example.muchbeer.thebestway.util.RetrieveAdapterImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;


public class RetrieveImage extends AppCompatActivity {

    private List<ItemPojo> itemProductList = new ArrayList<>();
    private ProgressDialog pDialog;
    private RetrieveAdapter mAdapter;
    private RecyclerView recyclerView;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_data);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);



        mAdapter = new RetrieveAdapter(this, itemProductList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
      //  recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        //Getting Instance of Volley Request Queue
      //  queue = NetworkController.getInstance(this).getRequestQueue();



        try {
            retrieveDataImage();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private void retrieveDataImage() throws JSONException {

  // Tag used to cancel the request
    String tag_string_req = "req_login";
        pDialog.setMessage("Fetching data ...");
    showDialog();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        JsonArrayRequest strReq = new JsonArrayRequest(AppConfig.URL_COLLECT_DATA, new Response.Listener<JSONArray>() {

        @Override
        public void onResponse(JSONArray response) {
            Log.d("RetrieveImage", "Login Response: " + response.toString());
            hideDialog();

            Log.i("RetrieveImage", "Login Response is: " + response);

            try {
                if (response.length() > 0) {
                    itemProductList.clear();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        ItemPojo itemProduct = new ItemPojo();

                        itemProduct.setTitle(jsonObject.getString("title"));
                        itemProduct.setThumbnailUrl(jsonObject.getString("image"));
                        itemProduct.setPrice(jsonObject.getString("title"));
                        itemProduct.setLocation(jsonObject.getString("title"));
                      /*  if (!jsonObject.isNull("name")) {
                            person.name = jsonObject.getString("name");
                        }
                        if (!jsonObject.isNull("age")) {
                            person.age = jsonObject.getInt("age");
                        }*/

                      //  Log.i("ImageData is: ",itemProduct.setThumbnailUrl(jsonObject.getString("image"));

                        itemProductList.add(i, itemProduct);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }, new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("RetrieveImage", "Login Error: " + error.getMessage());
            Toast.makeText(getApplicationContext(),
                    error.getMessage(), Toast.LENGTH_LONG).show();
            hideDialog();
        }
    });


      //  requestQueue.add(strReq);

        //Adding request to the queue
      //  requestQueue.add(strReq);
    // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
      //  NetworkController.getInstance(this).getRequestQueue();
      //  queue = NetworkController.getInstance(this).getRequestQueue();

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        hideDialog();
    }
    private void hideDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
}
