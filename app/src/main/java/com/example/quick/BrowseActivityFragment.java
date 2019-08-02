package com.example.quick;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quick.controller.PlaceController;
import com.example.quick.model.Place;


public class BrowseActivityFragment extends Fragment {


  RecyclerView placeRecycler;
  PlaceAdapter placeAdapter;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.activity_browse_fragment, container, false);
    placeRecycler = rootView.findViewById(R.id.placeRecycler);

    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
    placeAdapter = new PlaceAdapter(getContext());
    PlaceController.getPlaces(placeAdapter);
      placeAdapter.setOnItemClickListener(new PlaceAdapter.ClickListener() {
          @Override
          public void onItemClick(Place place, View v) {
              System.out.println("Clicked");
              Intent intent = new Intent(getContext(), PlaceInfoActivity.class);
              intent.putExtra("placeId", place.getPlaceId());
              startActivity(intent);
          }
      });
    placeRecycler.setLayoutManager(layoutManager);
    placeRecycler.setAdapter(placeAdapter);
    return rootView;
  }
}
