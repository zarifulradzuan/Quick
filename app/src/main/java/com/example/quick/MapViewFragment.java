package com.example.quick;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quick.controller.PlaceController;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class MapViewFragment extends Fragment implements LocationListener {

    MapView mapView;
    Fragment fragment;
    private GoogleMap googleMap;
    protected LocationManager locationManager;
    protected double lat,lng;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_map_view_fragment, container, false);
        mapView = rootView.findViewById(R.id.mapView);
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
                map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        getActivity().findViewById(R.id.placeInfoFrame).setVisibility(View.GONE);
                    }
                });
                map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        String placeId = (String) marker.getTag();

                        getActivity().findViewById(R.id.placeInfoFrame).setVisibility(View.VISIBLE);
                        FragmentManager fragmentManager = getFragmentManager();
                        Fragment fragment = new PlaceInfoFragment();
                        Bundle args = new Bundle();
                        args.putString("placeId", placeId);
                        try {
                            fragment = PlaceInfoFragment.class.newInstance();
                            fragment.setArguments(args);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        fragmentManager.beginTransaction().replace(R.id.placeInfoFrame, fragment).commit();
                        return false;
                    }
                });
                PlaceController.getPlaces(map);
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
