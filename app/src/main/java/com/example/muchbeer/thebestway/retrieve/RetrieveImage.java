package com.example.muchbeer.thebestway.retrieve;

import android.app.ProgressDialog;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
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


    private static final String TAG = "RetrieveImage debug " ;
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

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.setAdapter(mAdapter);

        setSwipeForRecyclerView();
    }

    private void setSwipeForRecyclerView() {

        SwipeUtil swipeHelper = new SwipeUtil(0, ItemTouchHelper.LEFT, getApplication()) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int swipedPosition = viewHolder.getAdapterPosition();
                RetrieveAdapterRecycler adapter = (RetrieveAdapterRecycler) recyclerView.getAdapter();
                adapter.pendingRemoval(swipedPosition);
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                RetrieveAdapterRecycler adapter = (RetrieveAdapterRecycler) recyclerView.getAdapter();
                if (adapter.isPendingRemoval(position)) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }
        };

        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(swipeHelper);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

        //set swipe label
        swipeHelper.setLeftSwipeLable("Archive");
        //set swipe background-Color
        swipeHelper.setLeftcolorCode( ContextCompat.getColor(RetrieveImage.this, R.color.bg_screen1));

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

    /**
     * This is the standard support library way of implementing "swipe to delete" feature. You can do custom drawing in onChildDraw method
     * but whatever you draw will disappear once the swipe is over, and while the items are animating to their new position the recycler view
     * background will be visible. That is rarely an desired effect.
     */
    private void setUpItemTouchHelper() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // we want to cache these and not allocate anything repeatedly in the onChildDraw method
            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                xMark = ContextCompat.getDrawable(RetrieveImage.this, R.drawable.ic_clear_24dp);
                xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) RetrieveImage.this.getResources().getDimension(R.dimen.ic_clear_margin);
                initiated = true;
            }

            // not important, we don't want drag & drop
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                RetrieveAdapterRecycler removeAdapter = (RetrieveAdapterRecycler)recyclerView.getAdapter();
                if (removeAdapter.isUndoOn() && removeAdapter.isPendingRemoval(position)) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();
                RetrieveAdapterRecycler adapter = (RetrieveAdapterRecycler
                        )recyclerView.getAdapter();
                boolean undoOn = adapter.isUndoOn();
                if (undoOn) {
                    adapter.pendingRemoval(swipedPosition);
                } else {
                    adapter.remove(swipedPosition);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.getAdapterPosition() == -1) {
                    // not interested in those
                    return;
                }

                if (!initiated) {
                    init();
                }

                // draw red background
                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    /**
     * We're gonna setup another ItemDecorator that will draw the red background in the empty space while the items are animating to thier new positions
     * after an item is removed.
     */
    private void setUpAnimationDecoratorHelper() {
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            // we want to cache this and not allocate anything repeatedly in the onDraw method
            Drawable background;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                initiated = true;
            }

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

                if (!initiated) {
                    init();
                }

                // only if animation is in progress
                if (parent.getItemAnimator().isRunning()) {

                    // some items might be animating down and some items might be animating up to close the gap left by the removed item
                    // this is not exclusive, both movement can be happening at the same time
                    // to reproduce this leave just enough items so the first one and the last one would be just a little off screen
                    // then remove one from the middle

                    // find first child with translationY > 0
                    // and last one with translationY < 0
                    // we're after a rect that is not covered in recycler-view views at this point in time
                    View lastViewComingDown = null;
                    View firstViewComingUp = null;

                    // this is fixed
                    int left = 0;
                    int right = parent.getWidth();

                    // this we need to find out
                    int top = 0;
                    int bottom = 0;

                    // find relevant translating views
                    int childCount = parent.getLayoutManager().getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View child = parent.getLayoutManager().getChildAt(i);
                        if (child.getTranslationY() < 0) {
                            // view is coming down
                            lastViewComingDown = child;
                        } else if (child.getTranslationY() > 0) {
                            // view is coming up
                            if (firstViewComingUp == null) {
                                firstViewComingUp = child;
                            }
                        }
                    }

                    if (lastViewComingDown != null && firstViewComingUp != null) {
                        // views are coming down AND going up to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    } else if (lastViewComingDown != null) {
                        // views are going down to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = lastViewComingDown.getBottom();
                    } else if (firstViewComingUp != null) {
                        // views are coming up to fill the void
                        top = firstViewComingUp.getTop();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    }

                    background.setBounds(left, top, right, bottom);
                    background.draw(c);

                }
                super.onDraw(c, parent, state);
            }

        });
    }
}
