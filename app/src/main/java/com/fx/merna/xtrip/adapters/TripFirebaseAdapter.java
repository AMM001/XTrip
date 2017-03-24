package com.fx.merna.xtrip.adapters;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.fx.merna.xtrip.R;
import com.fx.merna.xtrip.holders.UpcomingViewHolder;
import com.fx.merna.xtrip.models.Trip;
import com.fx.merna.xtrip.utils.Alarm;
import com.fx.merna.xtrip.utils.DateParser;
import com.fx.merna.xtrip.utils.SHA;
import com.fx.merna.xtrip.views.activities.AddTripActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.fx.merna.xtrip.views.activities.ViewDetailsActivity;
import com.google.firebase.database.Query;

import java.io.IOException;
import java.util.List;

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

        String[] arrDate = DateParser.parseLongDateToStrings(model.getDate());
        holder.getYear().setText(arrDate[0] + " " + arrDate[1]);

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

                                final AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                                alert.setTitle("Delete");
                                alert.setMessage("Are you sure you want to delete this trip ?");
                                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        //------- delete trip from DB ---------
                                        DatabaseReference myRef = database.getReference("trips").child(user.getUid())
                                                .child(model.getId());
                                        myRef.removeValue();

                                        //-------- cancel alarm for this trip
                                        Alarm.cancelAlarm(activity.getApplicationContext(), SHA.getIntegerID(model.getId()));


                                    }
                                });

                                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }
                                );

                                alert.show();

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

        //View Details
        holder.getViewDetails().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ViewDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("tripDetails", model);
                intent.putExtras(bundle);
                activity.startActivity(intent);
                Toast.makeText(activity, "You Clicked : Details View", Toast.LENGTH_LONG).show();
                System.out.println("Details Act/////////////////////////////////////");
            }
        });
    }


}
