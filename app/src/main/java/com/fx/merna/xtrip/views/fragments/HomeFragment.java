package com.fx.merna.xtrip.views.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fx.merna.xtrip.R;
import com.fx.merna.xtrip.adapters.TripFirebaseAdapter;
import com.fx.merna.xtrip.holders.UpcomingViewHolder;
import com.fx.merna.xtrip.models.Trip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class HomeFragment extends Fragment {

    private RecyclerView tripRecyclerview;
    private LinearLayoutManager linearLayoutManager;
    private TripFirebaseAdapter mTripFirebaseAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =inflater.inflate(R.layout.fragment_home, container, false);
        tripRecyclerview= (RecyclerView) root.findViewById(R.id.recycler_view);

        linearLayoutManager = new LinearLayoutManager(getActivity());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.i("MY_TAG_IN_HOME",user.getUid());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference tripsList = database.getReference("trips").child(user.getUid());
        Query query = tripsList.orderByChild("status").equalTo("upcoming");

        mTripFirebaseAdapter = new TripFirebaseAdapter(getActivity(), Trip.class, R.layout.trip_row, UpcomingViewHolder.class, query);
        tripRecyclerview.setLayoutManager(linearLayoutManager);

        //this line of code to add line divider between items  in recycle view ..
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                linearLayoutManager.getOrientation());
        tripRecyclerview.addItemDecoration(dividerItemDecoration);

        tripRecyclerview.setAdapter(mTripFirebaseAdapter);

        return root;
    }



}
