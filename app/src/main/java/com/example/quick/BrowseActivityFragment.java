package com.example.quick;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import model.OpeningHours;
import model.Place;

public class BrowseActivityFragment extends Fragment {


    RecyclerView placeRecycler;
    PlaceAdapter placeAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_browse_fragment, container, false);
        placeRecycler = rootView.findViewById(R.id.placeRecycler);
        if(placeRecycler==null)
            System.out.println("Recy null");

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

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        placeAdapter = new PlaceAdapter(getContext(), places);
        placeRecycler.setLayoutManager(layoutManager);
        placeRecycler.setAdapter(placeAdapter);
        placeAdapter.notifyDataSetChanged();
        return rootView;
    }
}
