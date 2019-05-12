package com.example.quick;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import com.example.quick.controller.PlaceController;
import com.example.quick.controller.TrackingController;
import com.example.quick.model.Place;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder>{
    private ArrayList<Place> places;
    private Context context;
    TrackingController trackingController;
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
        holder.lastUpdated.setText("Last updated: "+place.getLastUpdated());
        holder.placeName.setText(place.getPlaceName());
        if(placeController.isOpen()){
            holder.openingStatus.setText("Open");
            holder.openingStatus.setTextColor(Color.GREEN);
        }
        else {
            holder.openingStatus.setText("Closed");
            holder.openingStatus.setTextColor(Color.RED);
        }

        if(trackingController.fnGetTracking().contains(place.getPlaceId())){
            holder.trackingSwitch.setChecked(true);
        }
        else
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView placeName, openingStatus, lastUpdated;
        public ProgressBar occupancyBar;
        public Switch trackingSwitch;
        public ViewHolder(View itemView) {
            super(itemView);
            placeName = (TextView) itemView.findViewById(R.id.placeName);
            openingStatus = (TextView) itemView.findViewById(R.id.openingStatus);
            lastUpdated = (TextView) itemView.findViewById(R.id.lastUpdated);
            occupancyBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            trackingSwitch = itemView.findViewById(R.id.trackSwitch);
        }
    }
}
