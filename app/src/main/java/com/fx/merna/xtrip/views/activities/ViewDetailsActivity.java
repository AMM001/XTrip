package com.fx.merna.xtrip.views.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fx.merna.xtrip.R;
import com.fx.merna.xtrip.models.Trip;
import com.fx.merna.xtrip.utils.DateParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Date;

public class ViewDetailsActivity extends AppCompatActivity {

    TextView dateView;
    TextView fromView;
    TextView toView;
    TextView statusUpcoming;
    TextView statusDone;
    TextView statusCanceled;
    ImageView startTrip;
    Trip trip;
    CheckBox check;
    Bundle bundleObject;
    TextView statusView;
    TextView typeView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        dateView = (TextView) findViewById(R.id.dateView);
        fromView = (TextView) findViewById(R.id.from);
        toView = (TextView) findViewById(R.id.to);
        statusView = (TextView) findViewById(R.id.statusView);
        startTrip = (ImageView) findViewById(R.id.view_trip);
        check = (CheckBox) findViewById(R.id.checkBtn);
        typeView=(TextView)findViewById(R.id.typeView);
        final ImageView imageView;
        Button startTrip=(Button)findViewById(R.id.startBtn);
        //Set Font to button

        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/comic.ttf");
        startTrip.setTypeface(face);

        startTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("trips").child(user.getUid())
                        .child(trip.getId()).child("status");
                myRef.setValue("Done");
                Uri uri = Uri.parse("google.navigation:q=" + trip.getEndLat() + "," + trip.getEndLong() + "&mode=d");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(actionBar.getDisplayOptions() | ActionBar.DISPLAY_SHOW_CUSTOM);
        imageView = new ImageView(actionBar.getThemedContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setImageResource(R.drawable.settings);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.RIGHT
                | Gravity.CENTER_VERTICAL);
        layoutParams.rightMargin = 40;
        imageView.setLayoutParams(layoutParams);
        actionBar.setCustomView(imageView);
        bundleObject = this.getIntent().getExtras();
        if (bundleObject != null) {
            trip = (Trip) bundleObject.getSerializable("tripDetails");
            setTitle(trip.getName());
            //  getActionBar().setHomeButtonEnabled(true);
            //  getActionBar().setDisplayHomeAsUpEnabled(true);
            String f = "From: ";
            fromView.setText(trip.getStartPoint());
            fromView.setTextColor(Color.parseColor("#009688"));
            //  fromView.setTextSize(20);
            toView.setText(trip.getEndPoint());
            toView.setTextColor(Color.parseColor("#009688"));
            statusView.setText(trip.getStatus());
            String typeOneDirection="2131689669";
            if(trip.getType().equals(typeOneDirection)){
                typeView.setText("OneDirection");
            }
            else{
                typeView.setText("RoundTrip");
            }
            String date[] = DateParser.parseLongDateToStrings(trip.getDate());
            dateView.setText(date[0] + " " + date[1]);

            //check box change states to done

            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {

                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();

                        DatabaseReference myRef = database.getReference("trips").child(user.getUid())
                                .child(trip.getId()).child("status");
                        myRef.setValue("Done");
                        statusView.setText("Done");
                    }
                }
            });

            //popup menue and Settings action (update & Delete)
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PopupMenu popup = new PopupMenu(ViewDetailsActivity.this, imageView);
                    popup.getMenuInflater().inflate(R.menu.trip_item_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {
                                case R.id.updateTripItem:
                                    Intent intent = new Intent(ViewDetailsActivity.this, AddTripActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("clickedItem", trip);
                                    intent.putExtras(bundle);
                                    startActivity(intent);

                                    Toast.makeText(ViewDetailsActivity.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                                    break;
                                case R.id.deleteTripItem:
                                    final AlertDialog.Builder alert = new AlertDialog.Builder(ViewDetailsActivity.this);
                                    alert.setTitle("Delete");
                                    alert.setMessage("Are you sure you want to delete this trip ?");
                                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                            final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                            DatabaseReference myRef = database.getReference("trips").child(user.getUid())
                                                    .child(trip.getId());
                                            myRef.removeValue();
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
        }

    }
}
