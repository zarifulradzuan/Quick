package com.example.quick.model;


import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.time.*;
import java.util.ArrayList;
import java.util.List;


public class Place {
    private String placeId, placeName, idOwner, lastUpdated, address;
    private double placeLatitude,placeLongitude;
    private List<OpeningHours> openingHours;
    private int currentOccupancy, maxOccupancy;

    public  Place(){
        openingHours = new ArrayList<OpeningHours>();
    }
    public Place(String placeId,String placeName, String idOwner, double placeLatitude, double placeLongitude, String address, int maxOccupancy){
        openingHours = new ArrayList<OpeningHours>();
        this.placeId = placeId;
        this.placeName = placeName;
        this.idOwner = idOwner;
        this.placeLatitude = placeLatitude;
        this.placeLongitude = placeLongitude;
        this.maxOccupancy = maxOccupancy;
        this.address = address;
    }

    public void setIdOwner(String idOwner) {
        this.idOwner = idOwner;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public void setCurrentOccupancy(int currentOccupancy) {
        this.currentOccupancy = currentOccupancy;
    }

    public void setMaxOccupancy(int maxOccupancy) {
        this.maxOccupancy = maxOccupancy;
    }

    public void setPlaceLatitude(double placeLatitude) {
        this.placeLatitude = placeLatitude;
    }

    public void setPlaceLongitude(double placeLongitude) {
        this.placeLongitude = placeLongitude;
    }

    public List<OpeningHours> getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(List<OpeningHours> openingHours){
        this.openingHours = openingHours;
    }

    public double getPlaceLatitude() {
        return placeLatitude;
    }

    public String getAddress() {
        return address;
    }

    public double getPlaceLongitude() {
        return placeLongitude;
    }

    public int getCurrentOccupancy() {
        return currentOccupancy;
    }

    public int getMaxOccupancy() {
        return maxOccupancy;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getIdOwner() {
        return idOwner;
    }

    public void setCurrentOccupancy(int currentOccupancy, String lastUpdated){
        this.currentOccupancy = currentOccupancy;
        this.lastUpdated = lastUpdated;
    }

}
