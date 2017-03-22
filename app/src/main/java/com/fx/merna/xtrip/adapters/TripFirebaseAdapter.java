package com.fx.merna.xtrip.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.fx.merna.xtrip.R;
import com.fx.merna.xtrip.holders.UpcomingViewHolder;
import com.fx.merna.xtrip.models.Trip;
import com.fx.merna.xtrip.views.activities.AddTripActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.IOException;
import java.util.List;

import java.io.Serializable;

/**
 * Created by Merna on 3/19/17.
 */

public class TripFirebaseAdapter extends FirebaseRecyclerAdapter<Trip, UpcomingViewHolder> {

    Activity activity;


    public TripFirebaseAdapter(Activity activity, Class<Trip> modelClass, int modelLayout, Class<UpcomingViewHolder> viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.activity = activity;
    }

    @Override
    protected void populateViewHolder(UpcomingViewHolder viewHolder, final Trip model, int position) {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final UpcomingViewHolder holder = viewHolder;


        holder.getTitle().setText(model.getName().toUpperCase());
        holder.getFrom().setText(model.getStartPoint());
        holder.getTo().setText(model.getEndPoint());

        holder.getImgSetting().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(activity, holder.getImgSetting());
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.trip_item_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {

                            case R.id.updateTripItem:
                                Intent intent = new Intent(activity, AddTripActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("clickedItem", model);
                                intent.putExtras(bundle);
                                activity.startActivity(intent);
                                Toast.makeText(activity, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();

                                break;
                            case R.id.deleteTripItem:

                                DatabaseReference myRef = database.getReference("trips").child(user.getUid())
                                        .child(model.getId());
                                myRef.removeValue();

                                break;
                        }
                        return true;
                    }

                });

                popup.show();
            }
        });

        holder.getStartTrip().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //----------- change status to Done in DB  when start trip ----------
                DatabaseReference myRef = database.getReference("trips").child(user.getUid())
                        .child(model.getId()).child("status");
                myRef.setValue("Done");


                Uri uri = Uri.parse("google.navigation:q=" + model.getEndLat() + "," + model.getEndLong() + "&mode=d");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setPackage("com.google.android.apps.maps");
                activity.startActivity(intent);

            }
        });
    }

//    static class MyViewHolder extends RecyclerView.ViewHolder {
//        TextView title, year, from, to;
//        ImageView viewDetails, startTrip, imgSetting;
//
//        MyViewHolder(View itemView) {
//            super(itemView);
//            title = (TextView) itemView.findViewById(R.id.title);
//            year = (TextView) itemView.findViewById(R.id.txtDate);
//            from = (TextView) itemView.findViewById(R.id.from);
//            to = (TextView) itemView.findViewById(R.id.to);
//            viewDetails = (ImageView) itemView.findViewById(R.id.view_trip);
//            startTrip = (ImageView) itemView.findViewById(R.id.start_trip);
//            imgSetting = (ImageView) itemView.findViewById(R.id.settings);
//        }
//    }
}
