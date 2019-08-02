package com.example.quick;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.quick.controller.PlaceController;
import com.example.quick.model.Place;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class PlaceInfoActivity extends AppCompatActivity implements PlaceController.PlaceListener {
    String placeId;
    ArrayList<TextView> openingHoursTextView;
    TextView maxOccupancy;
    TextView currentOccupancy;
    TextView premiseName;
    TextView lastUpdated;
    TextView address;
    TextView percentageOccupancy;
    TextView openStatus;
    ProgressBar occupancyCircular;
    LatLng premiseLocation;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_info);

        progressBar = findViewById(R.id.progressBarInfo);

        openingHoursTextView = new ArrayList<>();
        openingHoursTextView.add((TextView) findViewById(R.id.openingHour0));
        openingHoursTextView.add((TextView) findViewById(R.id.openingHour1));
        openingHoursTextView.add((TextView) findViewById(R.id.openingHour2));
        openingHoursTextView.add((TextView) findViewById(R.id.openingHour3));
        openingHoursTextView.add((TextView) findViewById(R.id.openingHour4));
        openingHoursTextView.add((TextView) findViewById(R.id.openingHour5));
        openingHoursTextView.add((TextView) findViewById(R.id.openingHour6));

        maxOccupancy = findViewById(R.id.maxOccupancyInfo);
        currentOccupancy = findViewById(R.id.currentOccupancyInfo);
        premiseName = findViewById(R.id.premiseNameInfo);
        lastUpdated = findViewById(R.id.lastUpdatedInfo);
        address = findViewById(R.id.addressInfo);
        percentageOccupancy = findViewById(R.id.percentageInfo);
        openStatus = findViewById(R.id.openStatusInfo);
        occupancyCircular = findViewById(R.id.occupancyCircular);


    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        this.placeId = intent.getStringExtra("placeId");
        if (this.placeId != null) {
            progressBar.setVisibility(View.VISIBLE);

            PlaceController.getPlace(this.placeId, this);
        }
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void applyPlace(final Place place) {
        Thread applyPlaceThread = new Thread() {
            @Override
            public void run() {
                premiseLocation = new LatLng(place.getPlaceLatitude(), place.getPlaceLongitude());
                //Set opening hours
                for (int i = 0; i < 7; i++) {
                    final int index = i;

                    openingHoursTextView.get(i).post(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println(place.getOpeningHours().get(index).getOpening() + " " + place.getOpeningHours().get(index).getClosing());
                            if (place.getOpeningHours().get(index).getOpening().equals(""))
                                openingHoursTextView.get(index).setText(getString(R.string.closed_text));
                            else if (place.getOpeningHours().get(index).getOpening().equals(place.getOpeningHours().get(index).getClosing()))
                                openingHoursTextView.get(index).setText(getString(R.string.fullday));
                            else
                                openingHoursTextView.get(index).setText(String.format("%s - %s", place.getOpeningHours().get(index).getOpening(), place.getOpeningHours().get(index).getClosing()));
                        }
                    });
                }

                //Set text views
                currentOccupancy.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            currentOccupancy.setText(String.format("%s %d", getString(R.string.current_occupancy), place.getCurrentOccupancy()));
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        }
                    }
                });

                maxOccupancy.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            maxOccupancy.setText(String.format("%s %d", getString(R.string.max), place.getMaxOccupancy()));
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        }
                    }
                });

                premiseName.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            premiseName.setText(place.getPlaceName());
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        }
                    }
                });

                address.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            address.setText(place.getAddress());
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        }
                    }
                });

                lastUpdated.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            lastUpdated.setText(String.format("%s %s", getString(R.string.last_updated), place.getLastUpdated()));
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        }
                    }
                });


                final PlaceController placeController = new PlaceController(place);
                if (placeController.isOpen()) {
                    openStatus.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                openStatus.setText(getString(R.string.open_text));
                                openStatus.setTextColor(getResources().getColor(R.color.occupancyGreen));
                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    openStatus.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                openStatus.setText(getString(R.string.closed_text));
                                openStatus.setTextColor(getResources().getColor(R.color.occupancyRed));
                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                percentageOccupancy.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            percentageOccupancy.setText(placeController.getFullness() + "%");
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        }
                    }
                });
                occupancyCircular.setProgress(placeController.getFullness());
                if (placeController.getFullness() < 50)
                    occupancyCircular.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.occupancyGreen)));
                else if (placeController.getFullness() < 85)
                    occupancyCircular.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.occupancyYellow)));
                else
                    occupancyCircular.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.occupancyRed)));
                progressBar.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        };
        applyPlaceThread.start();
    }
}
