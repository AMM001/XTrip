package com.fx.merna.xtrip.views.fragments;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fx.merna.xtrip.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Random;


public class HistroyMapFragment extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Tag", "onCreateView");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("Tag", "onStart");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("Tag", "onCreateView");
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_histroy_map, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference tripsList = database.getReference("trips").child(user.getUid());
                tripsList.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (getContext() != null) {
                            for (DataSnapshot tripsnSnapshot : dataSnapshot.getChildren()) {
                                // TODO: handle the post

                                //Geocoder coder = new Geocoder(getContext());

                                List<Address> endAddress;
                                List<Address> startAddress;

                                // For dropping a marker at a point on the Map
                                LatLng start = new LatLng(Double.parseDouble(tripsnSnapshot.child("startLong").getValue().toString())
                                        , Double.parseDouble(tripsnSnapshot.child("startLat").getValue().toString()));

                                googleMap.addMarker(new MarkerOptions().position(start).title("Marker Title").snippet("Marker Description"));

                                // For zooming automatically to the location of the marker
                                CameraPosition cameraPosition = new CameraPosition.Builder().target(start).zoom(12).build();
                                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                                // For dropping a marker at a point on the Map
                                LatLng end = new LatLng(Double.parseDouble(tripsnSnapshot.child("endLong").getValue().toString())
                                        , Double.parseDouble(tripsnSnapshot.child("endLat").getValue().toString()));

                                googleMap.addMarker(new MarkerOptions().position(end).title("Marker Title").snippet("Marker Description"));

                                // For zooming automatically to the location of the marker

                                Random rnd = new Random();
                                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

                                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                Polyline line = googleMap.addPolyline(new PolylineOptions()
                                        .add(start, end)
                                        .width(10)
                                        .color(color));
                                mMapView.onResume();


                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

}
