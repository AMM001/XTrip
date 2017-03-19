package com.fx.merna.xtrip.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
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
import com.google.firebase.database.Query;

import java.io.IOException;
import java.util.List;

/**
 * Created by Merna on 3/19/17.
 */

public class TripFirebaseAdapter extends FirebaseRecyclerAdapter<Trip, UpcomingViewHolder> {

    Activity activity;

    /**
     * @param modelClass      Firebase will marshall the data at a location into
     *                        an instance of a class that you provide
     * @param modelLayout     This is the layout used to represent a single item in the list.
     *                        You will be responsible for populating an instance of the corresponding
     *                        view with the data from an instance of modelClass.
     * @param viewHolderClass The class that hold references to all sub-views in an instance modelLayout.
     * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location,
     *                        using some combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */
    public TripFirebaseAdapter(Activity activity,Class<Trip> modelClass, int modelLayout, Class<UpcomingViewHolder> viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.activity=activity;
    }

    @Override
    protected void populateViewHolder(UpcomingViewHolder viewHolder, final Trip model, int position) {

            final UpcomingViewHolder holder=viewHolder;

            holder.getTitle().setText(model.getName());
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
                            Toast.makeText(activity,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                            return true;
                        }

                    });

                    popup.show();
                }
            });

        holder.getStartTrip().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.getStartPoint();
                model.getEndPoint();
                Geocoder coder = new Geocoder(activity);
                List<Address> address;
                try {
                    address = coder.getFromLocationName(model.getEndPoint(),5);
                    Address direction=address.get(0);
                    Uri uri= Uri.parse("google.navigation:q="+direction.getLatitude()+","+direction.getLongitude()+"&mode=d");
                    Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                    intent.setPackage("com.google.android.apps.maps");
                    activity.startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                }

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
