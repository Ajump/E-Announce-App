package com.example.eannounceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FragmentMap extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap map;
    Double lat;
    Double lng;
    String locationName;
    String coords;
    Button mapBtn;
    ImageView back;
    Toolbar toobarMap;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        //Set the location of the event in the map
        LatLng eventCoords = new LatLng(lat,lng);
        map.addMarker(new MarkerOptions().position(eventCoords).title(locationName));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(eventCoords,17));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_map);

        toobarMap = findViewById(R.id.toolbar_map);
        back = findViewById(R.id.backButton_map);
        mapBtn = findViewById(R.id.map_button_id);

        lat = getIntent().getDoubleExtra("EXTRA_LAT_VALUE",0);
        lng = getIntent().getDoubleExtra("EXTRA_LNG_VALUE",0);
        locationName = getIntent().getStringExtra("EXTRA_LOCATION_NAME");

        coords =  lat.toString() +","+ lng.toString();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FragmentMap.this, ViewEventActivity.class);
                startActivity(intent);
            }
        });

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uriText = "google.navigation:q=" + coords;
                Uri gmmIntentUri = Uri.parse(uriText);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });

        //Call the FragmentMap layout
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_id);

        //Call the map
        mapFragment.getMapAsync(this);

    }


}
