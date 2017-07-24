package com.example.muchbeer.thebestway.retrieve;

import android.content.ClipData;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.muchbeer.thebestway.ItemPojo;
import com.example.muchbeer.thebestway.MainActivity;
import com.example.muchbeer.thebestway.R;

import java.util.ArrayList;
import java.util.List;

public class RetrieveData extends AppCompatActivity {

    private List<ItemPojo> itemProduct = new ArrayList<>();
    private RecyclerView recyclerView;
    private RetrieveAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_data);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new RetrieveAdapter(itemProduct);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),

                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ItemPojo itemProductSelect = itemProduct.get(position);
                Toast.makeText(getApplicationContext(), itemProductSelect.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        prepareMovieData();

    }

    private void prepareMovieData() {
       /* ItemPojo itemProductAdd = new ItemPojo("Mad Max: Fury Road", "Action & Adventure", "2015");
        itemProduct.add(itemProductAdd);

        itemProductAdd = new ItemPojo("Inside Out", "Animation, Kids & Family", "2015");
        itemProduct.add(itemProductAdd);

        itemProductAdd = new ItemPojo("Star Wars: Episode VII - The Force Awakens", "Action", "2015");
        itemProduct.add(itemProductAdd);

        itemProductAdd = new ItemPojo("Shaun the Sheep", "Animation", "2015");
        itemProduct.add(itemProductAdd);*/

      //  Log.i("Retrieve", itemProductAdd.toString());
        mAdapter.notifyDataSetChanged();
    }

}
