package com.example.eannounceapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EventRecyclerTodayAdapter extends FirestoreRecyclerAdapter<EventsDetails, EventRecyclerTodayAdapter.MyViewHolder> {

    private static final String TAG = "EventRecyclerTodayAdapt";
    private Context myContext;
    private String EVENT_ID;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public EventRecyclerTodayAdapter(@NonNull FirestoreRecyclerOptions<EventsDetails> options, Context myContext) {
        super(options);
        this.myContext = myContext;
    }

    @Override
    protected void onBindViewHolder(@NonNull EventRecyclerTodayAdapter.MyViewHolder myViewHolder, int i, @NonNull final EventsDetails eventsDetails) {

        DocumentSnapshot snapshot = getSnapshots().getSnapshot(myViewHolder.getAdapterPosition());
        EVENT_ID = snapshot.getId();
        eventsDetails.setEventID(EVENT_ID); // Get event document Unique ID from Firebase Firestore

        String timeConcat = eventsDetails.getEventTimeStart() +" - "+ eventsDetails.getEventTimeEnd();

        //Set image
        Picasso.get().load(eventsDetails.getEventPoster()).into(myViewHolder.eventImage);

        //Set event name
        myViewHolder.eventName.setText(eventsDetails.getEventName());

        //Set event time
        if(eventsDetails.getEventTimeStart().equals(eventsDetails.getEventTimeEnd())){
            myViewHolder.eventTime.setText(eventsDetails.getEventTimeStart());}
        else{
            myViewHolder.eventTime.setText(timeConcat);}

        //Set event location
        List<String> locationList = eventsDetails.getEventLocation();
        myViewHolder.eventVenue.setText(locationList.get(0));

        //button view
        myViewHolder.viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: "+eventsDetails);
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card_list,parent,false);
        return new MyViewHolder(v);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        public ImageView eventImage;
        public TextView eventName;
        public TextView eventTime;
        public TextView eventVenue;
        public Button viewButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            eventImage = (ImageView)itemView.findViewById(R.id.card_view_event_image);
            eventName = (TextView)itemView.findViewById(R.id.card_view_event_title);
            eventTime = (TextView)itemView.findViewById(R.id.card_view_event_time);
            eventVenue = (TextView)itemView.findViewById(R.id.card_view_event_location);
            viewButton = (Button)itemView.findViewById(R.id.card_view_event_button);
        }
    }
}
