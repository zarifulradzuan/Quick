package com.example.quick.controller;

import android.app.NotificationManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.quick.PlaceAdapter;
import com.example.quick.model.OpeningHours;
import com.example.quick.model.Place;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaceController {
    Place place;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    public PlaceController(Place place) {
        this.place = place;
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("places");
    }

    public static void getPlaces(final PlaceAdapter placeAdapter){
        final HashMap<String, Place> places = new HashMap<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("places");
        databaseReference.addChildEventListener(new ChildEventListener() {

            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                Place place = dataSnapshot.getValue(Place.class);
                places.put(place.getPlaceId(),place);
                placeAdapter.setPlaces(places);
                placeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Place place = dataSnapshot.getValue(Place.class);
                places.put(place.getPlaceId(),place);
                placeAdapter.setPlaces(places);
                placeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
    }

    public static void getPlaces(final PlaceAdapter placeAdapter, final List<String> tracking, final Context context){
        final HashMap<String, Place> places = new HashMap<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("places");
        databaseReference.addChildEventListener(new ChildEventListener() {

            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                Place place = dataSnapshot.getValue(Place.class);
                PlaceController placeController = new PlaceController(place);
                if(tracking.contains(place.getPlaceId())) {
                    places.put(place.getPlaceId(), place);
                    placeAdapter.setPlaces(places);
                    placeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Place place = dataSnapshot.getValue(Place.class);
                if(tracking.contains(place.getPlaceId())) {
                    places.put(place.getPlaceId(), place);
                    placeAdapter.setPlaces(places);
                    placeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
    }

    public static void getPlaces(final GoogleMap googleMap){
        final HashMap<String, Marker> markers = new HashMap<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("places");

        databaseReference.addChildEventListener(new ChildEventListener() {

            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                Place place = dataSnapshot.getValue(Place.class);
                PlaceController placeController = new PlaceController(place);
                markers.put(place.getPlaceId(),googleMap.addMarker(placeController.getPlaceMarker()));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Place place = dataSnapshot.getValue(Place.class);
                PlaceController placeController = new PlaceController(place);
                if(markers.containsKey(place.getPlaceId()))
                    markers.get(place.getPlaceId()).remove();
                markers.put(place.getPlaceId(),googleMap.addMarker(placeController.getPlaceMarker()));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
    }

    public static void insertPlace(Place place){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("places");
        DatabaseReference placeRef = databaseReference.child(place.getPlaceId());
        placeRef.setValue(place);
    }

    public MarkerOptions getPlaceMarker(){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(this.place.getPlaceName());
        markerOptions.position(new LatLng(this.place.getPlaceLatitude(),this.place.getPlaceLongitude()));
        if(!isOpen()){
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            return markerOptions;
        }
        if(getFullness()<49)
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        else if(getFullness()<85)
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        else
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        return markerOptions;
    }

    public int getFullness(){
        double currentOccupancy, maxOccupancy;
        currentOccupancy = this.place.getCurrentOccupancy();
        maxOccupancy = this.place.getMaxOccupancy();
        double fullnessInDouble = currentOccupancy/maxOccupancy;
        fullnessInDouble*=100;
        return (int) fullnessInDouble;
    }

    public boolean isOpen(){
        if(place.getOpeningHours().isEmpty()){
            return false;
        }
        LocalTime currentTime = LocalTime.now();
        LocalDate currentDate = LocalDate.now();
        OpeningHours today = place.getOpeningHours().get(currentDate.getDayOfWeek().getValue()-1);
        System.out.println("Opening: "+today.getOpening()+"  Closing:"+today.getClosing());
        if(today.getOpening().equals(today.getClosing())) {
            return true;
        }
        else if (today.getOpening()==null){
            return false;
        }
        else if(LocalTime.parse(today.getOpening()).isAfter(LocalTime.parse(today.getClosing()))){
            if(currentTime.isAfter(LocalTime.parse(today.getClosing())) && currentTime.isBefore(LocalTime.parse(today.getOpening()))) {
                return false;
            }
            else
                return true;
        }
        else
        if(currentTime.isAfter(LocalTime.parse(today.getOpening())) && currentTime.isBefore(LocalTime.parse(today.getClosing())))
            return true;
        return false;
    }

}
