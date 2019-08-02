package com.example.quick;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.example.quick.controller.PlaceController;
import com.example.quick.controller.TrackingController;
import com.example.quick.model.Place;

import java.util.ArrayList;
import java.util.HashMap;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder>{
    private ArrayList<Place> places;
    private Context context;
    private TrackingController trackingController;

    private ClickListener clickListener;

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(Place place, View v);
    }

    public PlaceAdapter() {
    }
    public PlaceAdapter(Context context){
        places = new ArrayList<>();
        trackingController = new TrackingController(context);
        this.context = context;
    }

    public void setPlaces(HashMap<String,Place> places){
        this.places = new ArrayList<>(places.values());
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_card,parent,false);
        return new ViewHolder(itemView);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Place place = places.get(position);
        PlaceController placeController = new PlaceController(place);
        holder.occupancyBar.setProgress(placeController.getFullness());
        if(placeController.getFullness()<50)
            holder.occupancyBar.setProgressTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.occupancyGreen)));
        else if(placeController.getFullness()<85)
            holder.occupancyBar.setProgressTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.occupancyYellow)));
        else
            holder.occupancyBar.setProgressTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.occupancyRed)));
        String lastUpdatedText = context.getString(R.string.last_updated);
        holder.lastUpdated.setText(lastUpdatedText + place.getLastUpdated());
        holder.placeName.setText(place.getPlaceName());
        if(placeController.isOpen()){
            holder.openingStatus.setText(R.string.open_text);
            holder.openingStatus.setTextColor((context.getResources().getColor(R.color.occupancyGreen)));
        } else {
            holder.openingStatus.setText(R.string.closed_text);
            holder.openingStatus.setTextColor(Color.RED);
        }

        if(trackingController.fnGetTracking().contains(place.getPlaceId())){
            holder.trackingSwitch.setChecked(true);
        } else
            holder.trackingSwitch.setChecked(false);
        holder.trackingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    trackingController.fnInsertTracking(place.getPlaceId());
                else
                    trackingController.fnDeleteSubject(place.getPlaceId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView placeName, openingStatus, lastUpdated;
        public CardView cardView;
        public ProgressBar occupancyBar;
        public Switch trackingSwitch;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.placeCard);
            cardView.setOnClickListener(this);
            placeName = itemView.findViewById(R.id.placeName);
            openingStatus = itemView.findViewById(R.id.openingStatus);
            lastUpdated = itemView.findViewById(R.id.lastUpdatedInfo);
            occupancyBar = itemView.findViewById(R.id.occupancyCircular);
            trackingSwitch = itemView.findViewById(R.id.trackSwitch);
        }


        @Override
        public void onClick(View v) {
            clickListener.onItemClick(places.get(getAdapterPosition()), v);
        }
    }
}
