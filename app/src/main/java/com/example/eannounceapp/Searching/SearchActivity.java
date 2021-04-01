package com.example.eannounceapp.Searching;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;

import com.example.eannounceapp.EventsDetails;
import com.example.eannounceapp.Login;
import com.example.eannounceapp.MainActivity;
import com.example.eannounceapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;


public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";
    private FirebaseFirestore db =  FirebaseFirestore.getInstance();
    private SearchAdapter adapter;
    private ArrayList<EventsDetails> eventSearchList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Get ArrayList from Main Activity
        eventSearchList = (ArrayList<EventsDetails>) getIntent().getSerializableExtra("eventList");
        //Setup view for search layout
        setUpRecyclerView();
    }


    private void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.EventSearch_recycler_viewId);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new SearchAdapter(eventSearchList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        getSupportActionBar().setTitle("List Events");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.faculty:
                adapter.getFilter().filter("faculty");
                return true;
            case R.id.college:
                adapter.getFilter().filter("college");
                return true;
            case R.id.mpp:
                adapter.getFilter().filter("mpp");
                return true;
            case R.id.others:
                adapter.getFilter().filter("Others");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
