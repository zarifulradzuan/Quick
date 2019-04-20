package com.example.quick;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;


import java.util.ArrayList;

import model.OpeningHours;
import model.Place;

public class MapViewFragment extends Fragment implements LocationListener{

    MapView mapView;
    private GoogleMap googleMap;
    protected LocationManager locationManager;
    protected double lat,lng;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_map_view_fragment, container, false);
        mapView = (MapView) rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        try{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0,this);
        }catch (SecurityException e){
            e.printStackTrace();
        }
        MapsInitializer.initialize(getActivity().getApplicationContext());
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map){
                googleMap = map;
                try{
                    googleMap.setMyLocationEnabled(true);
                    googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                    Location initialLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(initialLocation.getLatitude(),initialLocation.getLongitude()),15));
                }catch(SecurityException e){
                    e.printStackTrace();
                }
                System.out.println(lat+" "+lng);

                //Below is a test marker to test model.
                ArrayList<OpeningHours> openingHours = new ArrayList<>();
                openingHours.add(new OpeningHours("08:00","21:00"));
                openingHours.add(new OpeningHours("08:00","21:00"));
                openingHours.add(new OpeningHours("08:00","21:00"));
                openingHours.add(new OpeningHours("08:00","21:00"));
                openingHours.add(new OpeningHours("08:00","21:00"));
                openingHours.add(new OpeningHours("08:00","21:00"));
                openingHours.add(new OpeningHours("08:00","21:00"));

                ArrayList<OpeningHours> openingHours2 = new ArrayList<>();
                openingHours2.add(new OpeningHours("24",null));
                openingHours2.add(new OpeningHours("24",null));
                openingHours2.add(new OpeningHours("24",null));
                openingHours2.add(new OpeningHours("24",null));
                openingHours2.add(new OpeningHours("24",null));
                openingHours2.add(new OpeningHours("24",null));
                openingHours2.add(new OpeningHours("24",null));

                ArrayList<OpeningHours> openingHours3 = new ArrayList<>();
                openingHours3.add(new OpeningHours(null,null));
                openingHours3.add(new OpeningHours(null,null));
                openingHours3.add(new OpeningHours(null,null));
                openingHours3.add(new OpeningHours(null,null));
                openingHours3.add(new OpeningHours(null,null));
                openingHours3.add(new OpeningHours(null,null));
                openingHours3.add(new OpeningHours(null,null));

                ArrayList<OpeningHours> openingHours4 = new ArrayList<>();
                openingHours4.add(new OpeningHours("21:00","08:00"));
                openingHours4.add(new OpeningHours("21:00","08:00"));
                openingHours4.add(new OpeningHours("21:00","08:00"));
                openingHours4.add(new OpeningHours("21:00","08:00"));
                openingHours4.add(new OpeningHours("21:00","08:00"));
                openingHours4.add(new OpeningHours("21:00","08:00"));
                openingHours4.add(new OpeningHours("21:00","08:00"));

                ArrayList<Place> places = new ArrayList<>();

                Place place = new Place("McDonald's MITC", "1", 2.273756,102.285736, 100);
                place.setCurrentOccupancy(0,"12:15");
                place.setOpeningHours(openingHours);
                places.add(place);

                Place place2 = new Place("Example Place 2", "2", 2.273256,103.285736, 100);
                place2.setCurrentOccupancy(30,"15:15");
                place2.setOpeningHours(openingHours2);
                places.add(place2);

                Place place3 = new Place("Example Place 3", "3", 2.173256,102.285736, 100);
                place3.setCurrentOccupancy(0,"05:15");
                place3.setOpeningHours(openingHours3);
                places.add(place3);

                Place place4 = new Place("Example Place 4", "4", 3.273256,103.285736, 100);
                place4.setCurrentOccupancy(100,"21:15");
                place4.setOpeningHours(openingHours4);
                places.add(place4);
                googleMap.addMarker(place.getPlaceMarker());
                googleMap.addMarker(place2.getPlaceMarker());
                googleMap.addMarker(place3.getPlaceMarker());
                googleMap.addMarker(place4.getPlaceMarker());
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onLocationChanged(Location location) {
        this.lat = location.getLatitude();
        this.lng = location.getLongitude();
        System.out.println(lat+" "+lng);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
