package com.example.eannounceapp.Searching;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eannounceapp.EventsDetails;
import com.example.eannounceapp.R;
import com.example.eannounceapp.ViewEventActivity;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> implements Filterable{
    private static final String TAG = "SearchAdapter";
    private List<EventsDetails> eventSearchList;
    private List<EventsDetails> eventSearchListFull;
    private String category;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView eventImage;
        TextView eventName;
        TextView eventDate;
        TextView eventTime;
        TextView eventCategory;
        TextView eventVenue;
        RelativeLayout parentLayout;
        public androidx.cardview.widget.CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            eventImage = (ImageView)itemView.findViewById(R.id.search_Image);
            eventName = (TextView)itemView.findViewById(R.id.search_Name);
            eventDate = (TextView)itemView.findViewById(R.id.search_Date);
            eventTime = (TextView)itemView.findViewById(R.id.search_Time);
            eventCategory = (TextView)itemView.findViewById(R.id.search_Category);
            eventVenue = (TextView)itemView.findViewById(R.id.search_Venue);
            parentLayout = (RelativeLayout)itemView.findViewById(R.id.searchLayout_id);
            cardView = (androidx.cardview.widget.CardView)itemView.findViewById(R.id.searchCardViewLayout);

        }
    }

    SearchAdapter(List<EventsDetails> eventSearchList) {
        this.eventSearchList = eventSearchList;
        eventSearchListFull = new ArrayList<>(eventSearchList);
    }


    @Override
    public Filter getFilter() {
        return eventFilter;
    }

    @NonNull
    @Override
    public SearchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list,
                parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchAdapter.MyViewHolder myViewHolder, int position) {

        final EventsDetails currentItem = eventSearchList.get(position);

        String timeConcat = currentItem.getEventTimeStart() +" - "+ currentItem.getEventTimeEnd();

        //Set card background color based on category of event
        category = currentItem.getEventCategory();
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

        //myViewHolder.eventImage.setImageResource(R.drawable.ic_launcher_foreground);
        Glide.with(myViewHolder.itemView.getContext())
                .asBitmap()
                .load(currentItem.getEventPoster())
                .into(myViewHolder.eventImage);

        myViewHolder.eventName.setText(currentItem.getEventName());

        myViewHolder.eventDate.setText(currentItem.getEventDateStart());

        if(currentItem.getEventTimeStart().equals(currentItem.getEventTimeEnd())){
            myViewHolder.eventTime.setText(currentItem.getEventTimeStart());}
        else{
            myViewHolder.eventTime.setText(timeConcat);}

        List<String> locationList = currentItem.getEventLocation();
        myViewHolder.eventVenue.setText(locationList.get(0));

        myViewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ID: "+currentItem.getEventID());
                Intent intent = new Intent(myViewHolder.itemView.getContext(), ViewEventActivity.class);
                intent.putExtra("EXTRA_EVENT_ID", currentItem.getEventID());
                intent.putExtra("EXTRA_EVENT_INTERESTED_NUM", currentItem.getEventInterested());
                myViewHolder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventSearchList.size();
    }

    private Filter eventFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<EventsDetails> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(eventSearchListFull);
            } else {
                String filterPattern = constraint.toString().toUpperCase().trim();
                //Encode the search pattern into Soundex code
                String keySoundex = Soundex.getGode(filterPattern);
                Log.d(TAG, "performFiltering: Soundex code: "+ keySoundex);

                //filter the search pattern using SoundEx implementation
                for (EventsDetails item : eventSearchListFull) {
                    if (item.getKeywordSoundex().toUpperCase().contains(keySoundex)) {
                        filteredList.add(item);
                    }
                    if (item.getEventCategory().toUpperCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            eventSearchList.clear();
            eventSearchList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
