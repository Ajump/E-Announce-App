package com.example.eannounceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.eannounceapp.Searching.SearchActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class UpcomingActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private ArrayList<EventsDetails> eventSearchList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming);

        //Get ArrayList from Main Activity
        eventSearchList = (ArrayList<EventsDetails>) getIntent().getSerializableExtra("eventListforUpcoming");

        viewPager = (ViewPager) findViewById(R.id.viewpager_id);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        //Add Fragment Here
        adapter.AddFragment(new FragmentUpcoming(),"UPCOMING");

        viewPager.setAdapter(adapter);


        //Navigation Bottom
        //Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.Navigation_btm);
        //Set Today Selected
        bottomNavigationView.setSelectedItemId(R.id.upcoming);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.upcoming:
                        return true;
                    case R.id.today:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.previous:
                        Intent upcomingIntent = new Intent(getApplicationContext(), PreviousActivity.class);
                        upcomingIntent.putExtra("eventListforUpcoming",eventSearchList);//Pass ArrayList to Search Activity
                        startActivity(upcomingIntent);
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                Intent searchActivity = new Intent(UpcomingActivity.this, SearchActivity.class);
                searchActivity.putExtra("eventList",eventSearchList);//Pass ArrayList to Search Activity
                startActivity(searchActivity);
                return true;
            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intentLogout = new Intent(UpcomingActivity.this, Login.class);
                startActivity(intentLogout);
                finishAffinity();//finish the activity so that the user cannot back to previous activity
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
