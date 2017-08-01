package com.example.muchbeer.thebestway.retrieve;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.muchbeer.thebestway.R;

public class TwoPaneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_pane);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new RetrieveImageFragment())
                    .commit();
        }
    }
}
