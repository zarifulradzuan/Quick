package com.example.quick;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.quick.adapter.CommentAdapter;
import com.example.quick.controller.CommentController;
import com.example.quick.controller.PlaceController;
import com.example.quick.model.Comment;
import com.example.quick.model.Place;
import com.github.mikephil.charting.charts.BarChart;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class PlaceInfoActivity extends AppCompatActivity implements PlaceController.PlaceListener, CommentAdapter.CommentSizeInterface {
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
    BarChart trendChartDaily;
    BarChart trendChartWeekly;
    LatLng premiseLocation;
    GoogleMap googleMap;
    MapView mapView;
    ProgressBar progressBar;
    CardView mapCard;
    CommentAdapter commentAdapter;
    RecyclerView commentRecycler;
    Button sendComment;
    EditText messageComment;
    TextView commentPlaceholder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_info);

        commentPlaceholder = findViewById(R.id.commentPlaceholder);
        messageComment = findViewById(R.id.messageCommentInfo);
        sendComment = findViewById(R.id.saveCommentButtonInfo);
        progressBar = findViewById(R.id.progressBarInfo);
        trendChartDaily = findViewById(R.id.trendChartDaily);
        trendChartWeekly = findViewById(R.id.trendChartWeekly);
        mapCard = findViewById(R.id.mapCardInfo);
        mapView = findViewById(R.id.mapViewInfo);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        commentRecycler = findViewById(R.id.commentRecycler);
        commentRecycler.setVisibility(View.GONE);

        MapsInitializer.initialize(getApplicationContext());
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                googleMap = map;
                try {
                    googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                    googleMap.getUiSettings().setAllGesturesEnabled(false);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        });

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
        commentAdapter = new CommentAdapter(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        commentRecycler.setLayoutManager(layoutManager);
        commentRecycler.setAdapter(commentAdapter);
        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!messageComment.getText().toString().equals("")) {
                    Comment toInsert = new Comment();
                    toInsert.setDateTime(LocalDateTime.now().toString());
                    toInsert.setMessage(messageComment.getText().toString());
                    toInsert.setPlaceId(placeId);
                    CommentController.insertComment(toInsert);
                    messageComment.setText("");
                }
            }
        });
        if (this.placeId != null) {
            progressBar.setVisibility(View.VISIBLE);

            PlaceController.getPlace(this.placeId, this);
        }
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void applyPlace(final Place place) {
        CommentController.getComments(commentAdapter, place.getPlaceId());


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


                final PlaceController placeController = new PlaceController(place, getApplicationContext());
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

                placeController.getTrendData(place.getPlaceId(), PlaceController.MODE_DAILY, trendChartDaily);
                placeController.getTrendData(place.getPlaceId(), PlaceController.MODE_WEEKLY, trendChartWeekly);


                progressBar.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });


            }
        };
        googleMap.addMarker(new MarkerOptions()
                .title(place.getPlaceName())
                .position(new LatLng(place.getPlaceLatitude(), place.getPlaceLongitude())));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place.getPlaceLatitude(), place.getPlaceLongitude()), 15));

        applyPlaceThread.start();
    }

    @Override
    public void commentSizeListener(int size) {
        if (size > 0) {
            commentRecycler.setVisibility(View.VISIBLE);
            commentPlaceholder.setVisibility(View.GONE);
        } else {
            commentRecycler.setVisibility(View.GONE);
            commentPlaceholder.setVisibility(View.VISIBLE);
        }
    }
}
