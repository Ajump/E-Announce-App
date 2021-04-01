package com.example.eannounceapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.eannounceapp.Searching.SearchActivity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ViewPager viewPager;
    GoogleSignInClient mGoogleSignInClient;
    private Toolbar toolbar;
    private ViewPagerAdapter adapter;
    private FirebaseFirestore db =  FirebaseFirestore.getInstance();
    private CollectionReference eventRef = db.collection("events");
    private ArrayList<EventsDetails> eventSearchList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Retrieve all event Data from Firestore
        eventRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                for (QueryDocumentSnapshot documentSnapshot : value) {

                    EventsDetails events = documentSnapshot.toObject(EventsDetails.class);
                    String eventID = documentSnapshot.getId();
                    events.setEventID(eventID);
                    Log.d(TAG, "onEvent: get event class: "+ events);
                    eventSearchList.add(events);
                }
            }
        });

        final Query query = eventRef.whereEqualTo("eventStatus","TODAY");

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

//                Query queryStatus = eventRef.whereEqualTo("eventStatus","ONGOING");
                FirestoreRecyclerOptions<EventsDetails> options = new FirestoreRecyclerOptions.Builder<EventsDetails>()
                        .setQuery(query,EventsDetails.class)
                        .build();

                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }


                if (queryDocumentSnapshots.isEmpty()) {
                    Log.d(TAG, "Current data: Null ");
                    viewPager = (ViewPager) findViewById(R.id.viewpager_cardview_id);
                    adapter = new ViewPagerAdapter(getSupportFragmentManager());
                    //Add Fragment Here
                    adapter.AddFragment(new FragmentEmptyTodayData(),"Empty Event Data");
                    //set the Adapter after add the Fragment
                    viewPager.setAdapter(adapter);

                } else {
                    Log.d(TAG, "Current data: Exist");
                    viewPager = (ViewPager) findViewById(R.id.viewpager_cardview_id);
                    adapter = new ViewPagerAdapter(getSupportFragmentManager());
                    adapter.AddFragment(new FragmentTodayEvent(options), "Today Event");
                    //set the Adapter after add the Fragment
                    viewPager.setAdapter(adapter);
                }
            }
        });

        //Navigation Bottom
        //Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.Navigation_btm);
        //Set Today Selected
        bottomNavigationView.setSelectedItemId(R.id.today);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.upcoming:
                        Intent upcomingIntent = new Intent(getApplicationContext(), UpcomingActivity.class);
                        upcomingIntent.putExtra("eventListforUpcoming",eventSearchList);//Pass ArrayList to Search Activity
                        startActivity(upcomingIntent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.today:
                        return true;
                    case R.id.previous:
                        Intent previousIntent = new Intent(getApplicationContext(), PreviousActivity.class);
                        previousIntent.putExtra("eventListforPrevious",eventSearchList);//Pass ArrayList to Search Activity
                        startActivity(previousIntent);
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });

//        LinearLayout listEvent = findViewById(R.id.listEvents_id);
//        listEvent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, EventListActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        ImageView arrowButton = findViewById(R.id.listEvents_button_id);
//        arrowButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, EventListActivity.class);
//                startActivity(intent);
//            }
//        });


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
                Intent searchActivity = new Intent(MainActivity.this, SearchActivity.class);
                searchActivity.putExtra("eventList",eventSearchList);//Pass ArrayList to Search Activity
                startActivity(searchActivity);
                return true;
            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intentLogout = new Intent(MainActivity.this, Login.class);
                startActivity(intentLogout);
                finishAffinity();//finish the activity so that the user cannot back to previous activity
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
