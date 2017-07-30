package com.example.muchbeer.thebestway.retrieve;

import android.app.ProgressDialog;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Movie;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;

import com.example.muchbeer.thebestway.AppConfig;
import com.example.muchbeer.thebestway.AppController;
import com.example.muchbeer.thebestway.ItemPojo;
import com.example.muchbeer.thebestway.R;
import com.example.muchbeer.thebestway.util.NetworkController;
import com.example.muchbeer.thebestway.util.RetrieveAdapterImage;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;


public class RetrieveImage extends AppCompatActivity {


    private static final String TAG = "RetrieveImage debug" ;
    private ProgressDialog pDialog;
    private RetrieveAdapterRecycler mAdapter;
    private RecyclerView recyclerView;
     ArrayList<ItemPojo> itemProductList = new ArrayList<ItemPojo>();
    public String imageDisplayToUrl = null;
    public Bitmap getfromUrl;

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

//Finally initializing our adapter
      //  mAdapter = new RetrieveAdapterRecycler(th itemProductList,this);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
       recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new RetrieveAdapterRecycler(this, itemProductList, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d(TAG, "clicked position:" + position);
                String title = itemProductList.get(position).getTitle();
                Toast.makeText(getApplicationContext(),title + " is selected!", Toast.LENGTH_SHORT).show();

                //Fucking get the item long ID needed for the content Provider
             //  long postId = itemProductList.get(position).getID();
                // do what ever you want to do with it

                Bundle params = new Bundle();

               params.putString( FirebaseAnalytics.Param.ITEM_ID, title );
              //  params.putString( FirebaseAnalytics.Param.ITEM_CATEGORY, "icon" );
              //  params.putLong( FirebaseAnalytics.Param.VALUE, mItem.mPrice );
                FirebaseAnalytics analytics = FirebaseAnalytics.getInstance( RetrieveImage.this );
                analytics.logEvent( FirebaseAnalytics.Event.ADD_TO_CART, params );

            }
        });
        recyclerView.setAdapter(mAdapter);


        //Handle delete onlong press

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),

                recyclerView, new RecyclerTouchListener.ClickListener() {



            @Override
            public void onClick(View view, int position) {

                //    long postId = itemProductList.get(position).getID;

            }

            @Override
            public void onLongClick(View view, int position) {
                ItemPojo itemProductSelect = itemProductList.get(position);
                String title2 = itemProductList.get(position).getTitle();
                Toast.makeText(getApplicationContext(), title2 + " Long press is selected!", Toast.LENGTH_SHORT).show();

                // ADD THESE LINES
                Bundle params = new Bundle();
                params.putString( FirebaseAnalytics.Param.VALUE, "12");
                FirebaseAnalytics.getInstance( RetrieveImage.this ).logEvent( FirebaseAnalytics.Event.ECOMMERCE_PURCHASE, params );

            }
        }));
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
         JsonArrayRequest strReq = new JsonArrayRequest(AppConfig.URL_COLLECT_DATA, new Response.Listener<JSONArray>() {


             public void setUrl(final String url) {

             }
        @Override
        public void onResponse(JSONArray response) {
            Log.d("RetrieveImage", "Login Response: " + response.toString());
            hideDialog();

          //  Log.i("RetrieveImage", "Login Response is: " + response);

            try {

                     for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        ItemPojo itemProduct = new ItemPojo();

                        itemProduct.setTitle(jsonObject.getString("title"));
                        itemProduct.setThumbnailUrl(jsonObject.getString("image"));
                        itemProduct.setPrice(jsonObject.getString("title"));
                        itemProduct.setLocation(jsonObject.getString("title"));

                         String titleDisplay = itemProduct.setTitle(jsonObject.getString("title"));
                        imageDisplayToUrl= itemProduct.setThumbnailUrl(jsonObject.getString("image"));
                         String title2 =  itemProduct.setPrice(jsonObject.getString("title")) ;
                         String title3 =   itemProduct.setLocation(jsonObject.getString("title"));

                         Log.i("title Display ", titleDisplay);
                         Log.i("ImageDisplay ", imageDisplayToUrl);
                         Log.i("title2: ", title2);
                         Log.i("title3: ", title3);
                        itemProductList.add(itemProduct);
                    }

            } catch (JSONException e) {
                e.printStackTrace();
            }
// notifying list adapter about data changes
            // so that it renders the list view with updated data
            mAdapter.notifyDataSetChanged();
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

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
      //  NetworkController.getInstance(this).getRequestQueue();
     }


    public void getImage(String url, final ImageView v) {
        if (TextUtils.isEmpty(url))
        {
            Picasso.with(this).load(R.drawable.recycler).into(v);
            return; // don't fetch a null url
        }

        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                v.setImageBitmap(response);
            }
        }, 0, 0, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error- " + error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(imageRequest);
    }
public Bitmap downloadImage() {

    // Initialize a new ImageRequest
    ImageRequest imageRequest = new ImageRequest(
            imageDisplayToUrl, // Image URL
            new Response.Listener<Bitmap>() { // Bitmap listener
                @Override
                public void onResponse(Bitmap response) {
                    // Do something with response
                    //mImageView display image downloaded from the server
                  //  mImageView.setImageBitmap(response);

                    getfromUrl = response;
                    // Save this downloaded bitmap to internal storage
                    Uri uri = saveImageToInternalStorage(response);

                    // Display the internal storage saved image to image view
                    //mImageViewInternal is the ImageView View obtained locally
                  //  mImageViewInternal.setImageURI(uri);
                }
            },
            0, // Image width
            0, // Image height
            ImageView.ScaleType.CENTER_CROP, // Image scale type
            Bitmap.Config.RGB_565, //Image decode configuration
            new Response.ErrorListener() { // Error listener
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Do something with error response
                    error.printStackTrace();

                Log.d("ImageError: ", error.getMessage());
                }
            }
    );

    return getfromUrl;
}


    // Custom method to save a bitmap into internal storage
    protected Uri saveImageToInternalStorage(Bitmap bitmap){
        // Initialize ContextWrapper
        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());

        // Initializing a new file
        // The bellow line return a directory in internal storage
        File file = wrapper.getDir("Images",MODE_PRIVATE);

        // Create a file to save the image
        file = new File(file, "UniqueFileName"+".jpg");

        try{
            // Initialize a new OutputStream
            OutputStream stream = null;

            // If the output file exists, it can be replaced or appended to it
            stream = new FileOutputStream(file);

            // Compress the bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);

            // Flushes the stream
            stream.flush();

            // Closes the stream
            stream.close();

        }catch (IOException e) // Catch the exception
        {
            e.printStackTrace();
        }

        // Parse the gallery image url to uri
        Uri savedImageURI = Uri.parse(file.getAbsolutePath());

        // Return the saved image Uri
        return savedImageURI;
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
