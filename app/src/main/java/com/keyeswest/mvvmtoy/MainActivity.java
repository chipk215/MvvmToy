package com.keyeswest.mvvmtoy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.keyeswest.mvvmtoy.ui.TripListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add trip list fragment if this is first creation
        if (savedInstanceState == null) {
            TripListFragment fragment = new TripListFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment, TripListFragment.TAG).commit();
        }
    }
}
