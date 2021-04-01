package com.example.eannounceapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.github.davidmoten.geo.GeoHash;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EventListActivity extends AppCompatActivity {

    private static final String TAG = "EventListActivity";
    private TabLayout tablayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);


//        tablayout = (TabLayout) findViewById(R.id.tablayout_id);
//        viewPager = (ViewPager) findViewById(R.id.viewpager_id);
//        adapter = new ViewPagerAdapter(getSupportFragmentManager());


//        //Add Fragment Here
//        adapter.AddFragment(new FragmentUpcoming(),"UPCOMING");
//        adapter.AddFragment(new FragmentOngoing(), "ONGOING");
//        adapter.AddFragment(new FragmentPrevious(), "PREVIOUS");

//
//        viewPager.setAdapter(adapter);
//        tablayout.setupWithViewPager(viewPager);

    }
}
