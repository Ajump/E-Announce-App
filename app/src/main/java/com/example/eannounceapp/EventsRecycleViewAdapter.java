package com.example.eannounceapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class EventsRecycleViewAdapter extends FirestoreRecyclerAdapter<EventsDetails, EventsRecycleViewAdapter.MyViewHolder> {

    private static final String TAG = "EventsRecycleViewAdapte";
    private Context myContext;
    private String EVENT_ID;
    private String category;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public EventsRecycleViewAdapter(@NonNull FirestoreRecyclerOptions<EventsDetails> options, Context myContext) {
        super(options);
        this.myContext = myContext;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i, @NonNull final EventsDetails eventsDetails) {

        DocumentSnapshot snapshot = getSnapshots().getSnapshot(myViewHolder.getAdapterPosition());
        EVENT_ID = snapshot.getId();
        eventsDetails.setEventID(EVENT_ID); // Get event document Unique ID from Firebase Firestore

        String timeConcat = eventsDetails.getEventTimeStart() +" - "+ eventsDetails.getEventTimeEnd();

        //Set card background color based on category of event
        category = eventsDetails.getEventCategory();
        myViewHolder.eventCategory.setText(category + " Event");

        if(category.equals("MPP")){
           myViewHolder.cardView.setCardBackgroundColor(Color.parseColor("#b39ddb"));
        }
        else if(category.equals("College")){
            myViewHolder.cardView.setCardBackgroundColor(Color.parseColor("#fff59d"));
        }
        else if(category.equals("Faculty")){
            myViewHolder.cardView.setCardBackgroundColor(Color.parseColor("#90caf9"));
        }
        else if(category.equals("Others")){
            myViewHolder.cardView.setCardBackgroundColor(Color.parseColor("#b0bec5"));
        }

//        myViewHolder.eventImage.setImageResource(R.drawable.ic_launcher_foreground);
        Glide.with(myViewHolder.itemView.getContext())
                .asBitmap()
                .load(eventsDetails.getEventPoster())
                .into(myViewHolder.eventImage);

        myViewHolder.eventName.setText(eventsDetails.getEventName());

        myViewHolder.eventDate.setText(eventsDetails.getEventDateStart());

        if(eventsDetails.getEventTimeStart().equals(eventsDetails.getEventTimeEnd())){
            myViewHolder.eventTime.setText(eventsDetails.getEventTimeStart());}
        else{
            myViewHolder.eventTime.setText(timeConcat);}

        List<String> locationList = eventsDetails.getEventLocation();
        myViewHolder.eventVenue.setText(locationList.get(0));

        myViewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Click On: " + eventsDetails);
                Toast.makeText(myContext,eventsDetails.getEventName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(myContext, ViewEventActivity.class);
                intent.putExtra("EXTRA_EVENT_ID", eventsDetails.getEventID());
                intent.putExtra("EXTRA_EVENT_INTERESTED_NUM", eventsDetails.getEventInterested());
                myContext.startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_details_list,parent,false);

        return new MyViewHolder(v);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public ImageView eventImage;
        public TextView eventName;
        public TextView eventDate;
        public TextView eventTime;
        public TextView eventVenue;
        public TextView eventCategory;
        public RelativeLayout parentLayout;
        public androidx.cardview.widget.CardView cardView;
        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            eventImage = (ImageView)itemView.findViewById(R.id.event_Image);
            eventName = (TextView)itemView.findViewById(R.id.event_Name);
            eventDate = (TextView)itemView.findViewById(R.id.event_Date);
            eventTime = (TextView)itemView.findViewById(R.id.event_Time);
            eventVenue = (TextView)itemView.findViewById(R.id.event_Venue);
            eventCategory = (TextView)itemView.findViewById(R.id.event_category);
            parentLayout = (RelativeLayout)itemView.findViewById(R.id.parentLayout_id);
            cardView = (androidx.cardview.widget.CardView)itemView.findViewById(R.id.cardViewLayout);
        }
    }

}
