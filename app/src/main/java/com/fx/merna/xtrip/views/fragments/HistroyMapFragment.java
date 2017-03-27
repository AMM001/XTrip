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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.fx.merna.xtrip.R;
import com.fx.merna.xtrip.utils.MySingleTone;
import com.fx.merna.xtrip.utils.JsonParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;


public class HistroyMapFragment extends Fragment implements OnMapReadyCallback{

    MapView mMapView;
    private GoogleMap googleMap;
    MySingleTone mySingleTone;
    RequestQueue requestQueue;
    JsonParser jsonParser;
    Double latitudeSource, longitudeSource, latitudeDest, longitudeDest;
    PolylineOptions lineOptions;


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
        mySingleTone = MySingleTone.getInstance(getActivity().getApplicationContext());
        requestQueue = mySingleTone.getRequestQueue();
        jsonParser = new JsonParser();
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
        mMapView.onResume();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference tripsList = database.getReference("trips").child(user.getUid());
        Query query = tripsList.orderByChild("status").equalTo("Done");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot tripsnSnapshot : dataSnapshot.getChildren()) {
                    // For dropping a marker at a point on the Map
                    LatLng start = new LatLng(Double.parseDouble(tripsnSnapshot.child("startLat").getValue().toString())
                            , Double.parseDouble(tripsnSnapshot.child("startLong").getValue().toString()));
                    // For dropping a marker at a point on the Map
                    LatLng end = new LatLng(Double.parseDouble(tripsnSnapshot.child("endLat").getValue().toString())
                            , Double.parseDouble(tripsnSnapshot.child("endLong").getValue().toString()));
                    String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + start.latitude+","+start.longitude + "&destination=" + end.latitude+","+end.longitude + "&AIzaSyC_9uZBC_Sv84f4eAb4gZ2bsPHWBEi8NSY";
                    draw(url);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(start));
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

    public void draw(String url) {
        final ArrayList<LatLng> points= new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(JsonObjectRequest.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("success");
                System.out.println(response);
                System.out.println(jsonParser.parse(response));
                List<List<HashMap<String, String>>> roots = jsonParser.parse(response);
                // Traversing through all the routes
                for (int i = 0; i < roots.size(); i++) {
                    // Fetching i-th route
                    List<HashMap<String, String>> path = roots.get(i);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);
                        if (j == 0) {
                            latitudeSource = lat;
                            longitudeSource = lng;
                        }
                        if (j == path.size() - 1) {
                            latitudeDest = lat;
                            longitudeDest = lng;
                        }
                        points.add(position);
                    }

                }
                int color = new Random().nextInt(256);
                googleMap.addPolyline(new PolylineOptions().addAll(points).width(8).color(new Random().nextInt()+100));
                googleMap.addMarker(new MarkerOptions().position(new LatLng(latitudeSource, longitudeSource)).icon(BitmapDescriptorFactory.defaultMarker(color)));
                googleMap.addMarker(new MarkerOptions().position(new LatLng(latitudeDest, longitudeDest)).icon(BitmapDescriptorFactory.defaultMarker(color)));
                Toast.makeText(getActivity().getApplicationContext(), "load history successfully", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error   >" + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(), "error in loading history", Toast.LENGTH_SHORT).show();
            }
        });
        mySingleTone.addToRequestQueu(jsonObjectRequest);
    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        googleMap = mMap;
        //`mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(30.8206, 30.8025), 6));
    }
}
