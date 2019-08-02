package com.example.quick;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
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


public class PlaceInfoFragment extends Fragment implements PlaceController.PlaceListener {
    private TextView placeName, openingStatus, lastUpdated;
    private ProgressBar occupancyBar;
    private Switch trackingSwitch;
    private Place place;
    private CardView cardView;
    public PlaceInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String placeId = getArguments().getString("placeId");
        final View itemView = inflater.inflate(R.layout.place_card, container, false);
        PlaceController.getPlace(placeId, this);
        cardView = itemView.findViewById(R.id.placeCard);
        placeName = itemView.findViewById(R.id.placeName);
        openingStatus = itemView.findViewById(R.id.openingStatus);
        lastUpdated = itemView.findViewById(R.id.lastUpdatedInfo);
        occupancyBar = itemView.findViewById(R.id.occupancyCircular);
        trackingSwitch = itemView.findViewById(R.id.trackSwitch);
        itemView.setVisibility(View.INVISIBLE);
        return itemView;
    }

    @Override
    public void applyPlace(final Place place) {
        this.place = place;
        cardView.setClickable(true);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Clicked");
                Intent intent = new Intent(getContext(), PlaceInfoActivity.class);
                intent.putExtra("placeId", place.getPlaceId());
                startActivity(intent);
            }
        });
        PlaceController placeController = new PlaceController(place);
        final TrackingController trackingController = new TrackingController(getContext());
        occupancyBar.setProgress(placeController.getFullness());
        if (placeController.getFullness() < 50)
            occupancyBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.occupancyGreen)));
        else if (placeController.getFullness() < 85)
            occupancyBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.occupancyYellow)));
        else
            occupancyBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.occupancyRed)));
        lastUpdated.setText(String.format("%s %s", getString(R.string.last_updated), place.getLastUpdated()));
        placeName.setText(place.getPlaceName());
        if (placeController.isOpen()) {
            openingStatus.setText("Open");
            openingStatus.setTextColor(getActivity().getResources().getColor(R.color.occupancyGreen));
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
        this.getView().setVisibility(View.VISIBLE);
    }
}
