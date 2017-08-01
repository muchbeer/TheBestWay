package com.example.muchbeer.thebestway.retrieve;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.muchbeer.thebestway.AppConfig;
import com.example.muchbeer.thebestway.AppController;
import com.example.muchbeer.thebestway.ItemPojo;
import com.example.muchbeer.thebestway.R;
import com.example.muchbeer.thebestway.util.RetrieveAdapterImage;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by muchbeer on 01/08/2017.
 */

public class RetrieveImageFragment extends Fragment {
    private ProgressDialog pDialog;
    private RecyclerView recyclerView;
    private RetrieveAdapterFragment mAdapter;
    private static final String TAG = RetrieveImageFragment.class.getSimpleName();
    ArrayList<ItemPojo> itemProductList = new ArrayList<ItemPojo>();
    private String imageDisplayToUrl = null;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.activity_retrieveimage, container, false);


// Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

//Finally initializing our adapter
        //  mAdapter = new RetrieveAdapterRecycler(th itemProductList,this);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new RetrieveAdapterFragment(getActivity(), itemProductList, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d(TAG, "clicked position:" + position);
                String title = itemProductList.get(position).getTitle();
                Toast.makeText(getActivity(),title + " is selected!", Toast.LENGTH_SHORT).show();

                //Fucking get the item long ID needed for the content Provider
                //  long postId = itemProductList.get(position).getID();
                // do what ever you want to do with it

                Bundle params = new Bundle();

                params.putString( FirebaseAnalytics.Param.ITEM_ID, title );
                //  params.putString( FirebaseAnalytics.Param.ITEM_CATEGORY, "icon" );
                //  params.putLong( FirebaseAnalytics.Param.VALUE, mItem.mPrice );
                FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(getActivity());
                analytics.logEvent( FirebaseAnalytics.Event.ADD_TO_CART, params );
               // analytics = FirebaseAnalytics.getInstance(getActivity());

                Intent openDetail = new Intent(getActivity(), DetailActivity.class);
                startActivity(openDetail);
            }
        });



        //Handle delete onlong press

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),

                recyclerView, new RecyclerTouchListener.ClickListener() {



            @Override
            public void onClick(View view, int position) {

                //    long postId = itemProductList.get(position).getID;

            }

            @Override
            public void onLongClick(View view, int position) {
                ItemPojo itemProductSelect = itemProductList.get(position);
                String title2 = itemProductList.get(position).getTitle();
                Toast.makeText(getActivity(), title2 + " Long press is selected!", Toast.LENGTH_SHORT).show();

                // ADD THESE LINES
                Bundle params = new Bundle();
                params.putString( FirebaseAnalytics.Param.VALUE, "12");
                FirebaseAnalytics.getInstance( getActivity() ).logEvent( FirebaseAnalytics.Event.ECOMMERCE_PURCHASE, params );

            }
        }));
        try {
            retrieveDataImage();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.setAdapter(mAdapter);
//Content Provider
        /* if (mLocation != null && !mLocation.equals(Utility.getPreferredLocation(getActivity()))) {
            getLoaderManager().restartLoader(FORECAST_LOADER, null, this);
        }*/
        setSwipeForRecyclerView();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
       // getLoaderManager().initLoader(FORECAST_LOADER, null, this);
        //Content Provider will use this one
        super.onActivityCreated(savedInstanceState);
    }



    private void setSwipeForRecyclerView() {

        SwipeUtil swipeHelper = new SwipeUtil(0, ItemTouchHelper.LEFT, getActivity()) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int swipedPosition = viewHolder.getAdapterPosition();
                RetrieveAdapterFragment adapter = (RetrieveAdapterFragment) recyclerView.getAdapter();
                adapter.pendingRemoval(swipedPosition);
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                RetrieveAdapterFragment adapter = (RetrieveAdapterFragment) recyclerView.getAdapter();
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
        swipeHelper.setLeftcolorCode( ContextCompat.getColor(getActivity(), R.color.bg_screen1));

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
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        });

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        //  NetworkController.getInstance(this).getRequestQueue();
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
