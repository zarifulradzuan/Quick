package com.example.quick;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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


public class PlaceInfoFragment extends Fragment {
    private TextView placeName, openingStatus, lastUpdated;
    private ProgressBar occupancyBar;
    private Switch trackingSwitch;


    public PlaceInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String placeId = getArguments().getString("placeId");
        PlaceController.getPlace(placeId, this);
        final View itemView = inflater.inflate(R.layout.place_card, container, false);
        placeName = itemView.findViewById(R.id.placeName);
        openingStatus = itemView.findViewById(R.id.openingStatus);
        lastUpdated = itemView.findViewById(R.id.lastUpdated);
        occupancyBar = itemView.findViewById(R.id.progressBar);
        trackingSwitch = itemView.findViewById(R.id.trackSwitch);
        return itemView;
    }

    public void setPlace(final Place place) {
        PlaceController placeController = new PlaceController(place);
        final TrackingController trackingController = new TrackingController(getContext());
        occupancyBar.setProgress(placeController.getFullness());
        if (placeController.getFullness() < 50)
            occupancyBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.occupancyGreen)));
        else if (placeController.getFullness() < 85)
            occupancyBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.occupancyYellow)));
        else
            occupancyBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.occupancyRed)));
        lastUpdated.setText("Last updated: " + place.getLastUpdated());
        placeName.setText(place.getPlaceName());
        if (placeController.isOpen()) {
            openingStatus.setText("Open");
            openingStatus.setTextColor(Color.GREEN);
        } else {
            openingStatus.setText("Closed");
            openingStatus.setTextColor(Color.RED);
        }

        if (trackingController.fnGetTracking().contains(place.getPlaceId())) {
            trackingSwitch.setChecked(true);
        } else {
            trackingSwitch.setChecked(false);
        }


        trackingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                System.out.println("Check triggered: " + place.getPlaceId());
                if (isChecked)
                    trackingController.fnInsertTracking(place.getPlaceId());
                else
                    trackingController.fnDeleteSubject(place.getPlaceId());
            }
        });
    }
}
