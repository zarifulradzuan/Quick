package model;


import android.text.format.Time;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.time.*;
import java.util.ArrayList;


public class Place {
    String placeName, idOwner, lastUpdated;
    double placeLatitude,placeLongitude;
    ArrayList<OpeningHours> openingHours;
    int currentOccupancy, maxOccupancy;


    public Place(String placeName, String idOwner, double placeLatitude, double placeLongitude, int maxOccupancy){
        this.placeName = placeName;
        this.idOwner = idOwner;
        this.placeLatitude = placeLatitude;
        this.placeLongitude = placeLongitude;
        this.maxOccupancy = maxOccupancy;
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

    public MarkerOptions getPlaceMarker(){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(this.placeName);
        markerOptions.position(new LatLng(this.placeLatitude,this.placeLongitude));
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
        currentOccupancy = this.currentOccupancy;
        maxOccupancy = this.maxOccupancy;
        double fullnessInDouble = currentOccupancy/maxOccupancy;
        fullnessInDouble*=100;
        return (int) fullnessInDouble;
    }

    public void setOpeningHours(ArrayList<OpeningHours> openingHours){
        this.openingHours = openingHours;
    }

    public boolean isOpen(){
        if(openingHours.isEmpty()){
            return false;
        }
        LocalTime currentTime = LocalTime.now();
        LocalDate currentDate = LocalDate.now();
        OpeningHours today = openingHours.get(currentDate.getDayOfWeek().getValue()-1);
        if(today.closeDay) {
            return false;
        }
        else if (today.open24){
            return true;
        }
        else if(today.opening.isAfter(today.closing)){
            if(currentTime.isAfter(today.closing) && currentTime.isBefore(today.opening)) {
                return false;
            }
            else
                return true;
        }
        else
            if(currentTime.isAfter(today.opening) && currentTime.isBefore(today.closing))
                return true;
        return false;
    }

}
