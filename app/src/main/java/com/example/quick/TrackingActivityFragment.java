package com.example.quick;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quick.controller.PlaceController;
import com.example.quick.controller.TrackingController;

public class TrackingActivityFragment extends Fragment {


  RecyclerView placeRecycler;
  PlaceAdapter placeAdapter;
  TrackingController trackingController;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.activity_browse_fragment, container, false);
    placeRecycler = rootView.findViewById(R.id.placeRecycler);

    trackingController = new TrackingController(getContext());
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

    placeAdapter = new PlaceAdapter(getContext());
    PlaceController.getPlaces(placeAdapter, trackingController.fnGetTracking(), getContext());
    placeRecycler.setLayoutManager(layoutManager);
    placeRecycler.setAdapter(placeAdapter);

    placeAdapter.notifyDataSetChanged();
    return rootView;
  }
}
