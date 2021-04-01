package com.example.eannounceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.davidmoten.geo.GeoHash;
import com.github.davidmoten.geo.LatLong;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ViewEventActivity extends AppCompatActivity {

    private static final String TAG = "ViewEventActivity";
    Toolbar toolbar;
    private ImageView interestedIcon;
    private RelativeLayout layoutLocation;
    private Toast toast;
    private String coords;
    private ImageView back;

    private Double lat;
    private Double lng;

    private String data_eventPoster;
    private String data_eventName;
    private String data_eventOrganizer;
    private String data_eventCategory;
    private String data_eventDate;
    private String data_eventTime;
    private String data_eventLocationName;
    private String data_eventLocationAddress;
    private String data_eventSpeaker;
    private String data_eventContactName1;
    private String data_eventContactNum1;
    private String data_eventContactName2;
    private String data_eventContactNum2;
    private String data_eventDescription;
    private Boolean data_eventCoupon;
    private Boolean data_eventFood;
    private Integer eventInterested;

    private Integer intentInterestedNum;
    private String intentEventID;
    private String imgRes;
    private String imgTag;
    private ShimmerFrameLayout mShimmerViewContainer;
    private LinearLayout viewEventLayout;
    private DocumentReference docEventRef;
    private FirebaseFirestore db =  FirebaseFirestore.getInstance();
    private CollectionReference eventRef = db.collection("events");


    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    protected void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        back = findViewById(R.id.backButton_eventDetails);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                onBackPressed();
            }
        });

//        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.spinKit_id);
        viewEventLayout = (LinearLayout) findViewById(R.id.viewEventLayout_id);
        viewEventLayout.setVisibility(View.INVISIBLE);
//        Sprite cubeGrid = new CubeGrid();
//        progressBar.setIndeterminateDrawable(cubeGrid);

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
//                progressBar.setVisibility(View.INVISIBLE);
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                viewEventLayout.setVisibility(View.VISIBLE);

            }

        }, 2000); //2000 milisec delay

        final SharedPreferences prefs = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        Log.d(TAG, "onCreate: SharedPreferences: "+ prefs);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = user.getEmail();

        intentEventID = getIntent().getStringExtra("EXTRA_EVENT_ID");
        intentInterestedNum = getIntent().getIntExtra("EXTRA_EVENT_INTERESTED_NUM",0);

        Log.d(TAG, "onCreate: intentEventID: "+ intentEventID);
        Log.d(TAG, "onCreate: currentUser: "+ userEmail);
        docEventRef = eventRef.document(intentEventID);


        toolbar = (Toolbar) findViewById(R.id.view_event_toolbar);
        interestedIcon = (ImageView) findViewById(R.id.interested_icon);
        layoutLocation = (RelativeLayout) findViewById(R.id.layout_location_id);

        eventInterested = intentInterestedNum;
        Log.d(TAG, "onCreate: intentInterestedNum: "+ intentInterestedNum);

        imgRes = "isStarIconBorderRes" + userEmail;
        imgTag = "isStarIconBorderTag" + userEmail;

        interestedIcon.setImageResource(prefs.getBoolean(imgRes, false) ? R.drawable.ic_star_24dp : R.drawable.ic_star_border_24dp);
        interestedIcon.setTag(prefs.getBoolean(imgTag, false) ? R.drawable.ic_star_24dp : R.drawable.ic_star_border_24dp);

        //get event display id and setText changes to layout
        final ImageView eventPoster = findViewById(R.id.view_poster_id);
        final TextView eventName = findViewById(R.id.viewEventName_id);
        final TextView eventOrganizer = findViewById(R.id.view_Organizer_id);
        final TextView eventCategory = findViewById(R.id.view_Category_id);
        final TextView eventDate = findViewById(R.id.viewDate_id);
        final TextView eventTime = findViewById(R.id.viewTime_id);
        final TextView eventLocationName = findViewById(R.id.viewLocName_id);
//        final TextView eventLocationAddress = findViewById(R.id.viewLocAddress_id);
        final TextView eventSpeaker = findViewById(R.id.viewSpeaker_id);
        final TextView eventContactName1 = findViewById(R.id.viewContactName1_id);
        final TextView eventContactNum1 = findViewById(R.id.view_ContactNum1_id);
        final TextView eventContactName2 = findViewById(R.id.viewContactName2_id);
        final TextView eventContactNum2 = findViewById(R.id.view_ContactNum2_id);
        final TextView eventDescription = findViewById(R.id.viewDescription_id);
        final ImageView eventCoupon = findViewById(R.id.viewCoupon_id);
        final ImageView eventFood = findViewById(R.id.viewFood_id);

        final RelativeLayout speakerLayout = findViewById(R.id.speakerLayout);
        final RelativeLayout contactLayout = findViewById(R.id.contactLayout);
        final RelativeLayout descriptionLayout = findViewById(R.id.descriptionLayout);


        //get data from the selected document and set to the layout
        docEventRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " +  document.getData());
                        List<String> contactPerson1List = (List<String>) document.get("contactPerson1");
                        List<String> contactPerson2List = (List<String>) document.get("contactPerson2");
                        List<String> eventLocationList = (List<String>) document.get("eventLocation");
                        String dateStart = document.getString("eventDateStart");
                        String dateEnd = document.getString("eventDateEnd");
                        String timeStart = document.getString("eventTimeStart");
                        String timeEnd = document.getString("eventTimeEnd");
                        String organizer = document.getString("eventOrganizer");


                        //DISPLAY ALL DATA INTO LAYOUT
                        data_eventPoster = document.getString("eventPoster");
                        Picasso.get().load(data_eventPoster)
                                .fit()
                                .centerCrop()
                                .into(eventPoster);

                        data_eventName = document.getString("eventName");
                        eventName.setText(data_eventName);

                        String organizerText = "By "+ organizer;
                        data_eventOrganizer = organizerText;
                        eventOrganizer.setText(data_eventOrganizer);

                        data_eventCategory = document.getString("eventCategory");
                        eventCategory.setText(data_eventCategory);


                        String dateStartEndText = dateStart +" - "+ dateEnd;
                        if(dateStart.equals(dateEnd)){
                            data_eventDate = dateStart;
                            eventDate.setText(data_eventDate);
                        }
                        else{
                            data_eventDate = dateStartEndText;
                            eventDate.setText(data_eventDate);
                        }

                        String timeStartEndText = timeStart+" - "+timeEnd;
                        data_eventTime = timeStartEndText;
                        eventTime.setText(data_eventTime);

                        //get the location Name
                        data_eventLocationName = eventLocationList.get(0);
                        eventLocationName.setText(data_eventLocationName);

                        //Get the location Coords
                        data_eventLocationAddress = eventLocationList.get(1);
                        LatLong locationCoords = GeoHash.decodeHash(data_eventLocationAddress);
                        lat = locationCoords.getLat();
                        lng = locationCoords.getLon();
//                        coords = lat.toString() +","+ lng.toString();
//                        eventLocationAddress.setText(coords);


                        data_eventSpeaker = document.getString("eventSpeaker");
                        eventSpeaker.setText(data_eventSpeaker);
                        //Set the layout to invisible when the data is empty
                        if(data_eventSpeaker.isEmpty() ){
                            speakerLayout.setVisibility(View.INVISIBLE);
                            speakerLayout.getLayoutParams().height = 0;
                        }

                        data_eventContactName1 = contactPerson1List.get(0);
                        eventContactName1.setText(data_eventContactName1);
                        data_eventContactNum1 = contactPerson1List.get(1);
                        eventContactNum1.setText(data_eventContactNum1);


                        data_eventContactName2 = contactPerson2List.get(0);
                        eventContactName2.setText(data_eventContactName2);
                        data_eventContactNum2 = contactPerson2List.get(1);
                        eventContactNum2.setText(data_eventContactNum2);
                        //Set the layout to invisible when the data is empty
                        if(data_eventContactName1.isEmpty() && data_eventContactName2.isEmpty()){
                            contactLayout.setVisibility(View.INVISIBLE);
                            contactLayout.getLayoutParams().height = 0;
                        }


                        data_eventDescription = document.getString("eventDescription");
                        eventDescription.setText(data_eventDescription);
                        //Set the layout to invisible when the data is empty
                        if(data_eventDescription.isEmpty()){
                            descriptionLayout.setVisibility(View.INVISIBLE);
                            descriptionLayout.getLayoutParams().height = 0;
                        }

                        data_eventCoupon = document.getBoolean("couponAvailability");
                        if(data_eventCoupon == true){
                            eventCoupon.setImageResource(R.drawable.ic_local_play_true_24dp);
                            eventCoupon.setTag(R.drawable.ic_local_play_true_24dp);
                        }

                        data_eventFood = document.getBoolean("foodAvailability");
                        if(data_eventFood == true){
                            eventFood.setImageResource(R.drawable.ic_restaurant_true_24dp);
                            eventFood.setTag(R.drawable.ic_restaurant_true_24dp);
                        }

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        //listener for interested icon click
        interestedIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImageView imageView = (ImageView) view;
                assert(R.id.interested_icon == imageView.getId());

                // See here
                Integer integer = (Integer) imageView.getTag();
                integer = integer == null ? 0 : integer;

                switch(integer) {
                    case R.drawable.ic_star_border_24dp:
                    default:
                        prefs.edit().putBoolean(imgRes, true).apply();
                        prefs.edit().putBoolean(imgTag, true).apply();

                        imageView.setImageResource(R.drawable.ic_star_24dp);
                        imageView.setTag(R.drawable.ic_star_24dp);

                        eventInterested++;
                        docEventRef.update("eventInterested", eventInterested)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error updating document", e);
                                    }
                                });

                        toast = Toast.makeText(getApplicationContext(), "Interested!", Toast.LENGTH_SHORT);
                        toast.show();
                        break;

                    case R.drawable.ic_star_24dp:
                        prefs.edit().putBoolean(imgRes, false).apply();
                        prefs.edit().putBoolean(imgTag, false).apply();

                        imageView.setImageResource(R.drawable.ic_star_border_24dp);
                        imageView.setTag(R.drawable.ic_star_border_24dp);

                        eventInterested--;
                        docEventRef.update("eventInterested", eventInterested)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error updating document", e);
                                    }
                                });

                        toast = Toast.makeText(getApplicationContext(), "Not Interested!", Toast.LENGTH_SHORT);
                        toast.show();
                        break;
                }
            }
        });


        layoutLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewEventActivity.this, FragmentMap.class);
                intent.putExtra("EXTRA_LAT_VALUE", lat);
                intent.putExtra("EXTRA_LNG_VALUE", lng);
                intent.putExtra("EXTRA_LOCATION_NAME", data_eventLocationName);
                startActivity(intent);
            }
        });

    }


}
