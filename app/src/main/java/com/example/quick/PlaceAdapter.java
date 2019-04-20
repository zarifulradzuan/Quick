package com.example.quick;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import model.Place;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder>{
    private ArrayList<Place> places;
    private Context context;
    public PlaceAdapter(Context context, ArrayList<Place> places){
        this.context = context;
        this.places = places;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_card,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Place place = places.get(position);
        holder.occupancyBar.setProgress(place.getFullness());
        if(place.getFullness()<50)
            holder.occupancyBar.setProgressTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.occupancyGreen)));
        else if(place.getFullness()<85)
            holder.occupancyBar.setProgressTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.occupancyYellow)));
        else
            holder.occupancyBar.setProgressTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.occupancyRed)));
        holder.lastUpdated.setText("Last updated: "+place.getLastUpdated());
        holder.placeName.setText(place.getPlaceName());
        if(place.isOpen()){
            holder.openingStatus.setText("Open");
            holder.openingStatus.setTextColor(Color.GREEN);
        }
        else{
            holder.openingStatus.setText("Closed");
            holder.openingStatus.setTextColor(Color.RED);
        }

    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView placeName, openingStatus, lastUpdated;
        public ProgressBar occupancyBar;
        public ViewHolder(View itemView) {
            super(itemView);
            placeName = (TextView) itemView.findViewById(R.id.placeName);
            openingStatus = (TextView) itemView.findViewById(R.id.openingStatus);
            lastUpdated = (TextView) itemView.findViewById(R.id.lastUpdated);
            occupancyBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }
}
