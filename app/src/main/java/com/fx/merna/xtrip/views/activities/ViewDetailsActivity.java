package com.fx.merna.xtrip.views.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fx.merna.xtrip.R;
import com.fx.merna.xtrip.models.Trip;
import com.google.firebase.auth.FirebaseAuth;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        dateView= (TextView) findViewById(R.id.dateView);
        fromView= (TextView) findViewById(R.id.from);
        toView= (TextView) findViewById(R.id.to);
        statusUpcoming= (TextView) findViewById(R.id.upcomingView);
        statusDone= (TextView) findViewById(R.id.doneView);
        statusCanceled= (TextView) findViewById(R.id.cancelView);
        startTrip=(ImageView)findViewById(R.id.view_trip);
        check=(CheckBox)findViewById(R.id.checkBtn);
        final ImageView imageView;


        //Settings Image on Action Bar

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(actionBar.getDisplayOptions()|ActionBar.DISPLAY_SHOW_CUSTOM);
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



         bundleObject=this.getIntent().getExtras();
        if(bundleObject !=null){
            trip= (Trip) bundleObject.getSerializable("tripDetails");
            setTitle(trip.getName());
          //  getActionBar().setHomeButtonEnabled(true);
          //  getActionBar().setDisplayHomeAsUpEnabled(true);
            String f="From: ";
            fromView.setText(trip.getStartPoint());
            fromView.setTextColor(Color.RED);
          //  fromView.setTextSize(20);
            toView.setText("to: "+trip.getEndPoint());
            toView.setTextColor(Color.RED);
            if(trip.getStatus().equals("upcoming")){
                statusDone.setVisibility(View.INVISIBLE);
                statusCanceled.setVisibility(View.INVISIBLE);
                statusUpcoming.setVisibility(View.VISIBLE);
            }
            if(trip.getStatus().equals("canceled")){
                statusUpcoming.setVisibility(View.INVISIBLE);
                statusDone.setVisibility(View.INVISIBLE);
                statusCanceled.setVisibility(View.VISIBLE);

            }
            if(trip.getStatus().equals("done")){
                statusUpcoming.setVisibility(View.INVISIBLE);
                statusCanceled.setVisibility(View.INVISIBLE);
                statusDone.setVisibility(View.VISIBLE);
            }

            //popup menue and Settings action (update & Delete)

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PopupMenu popup=new PopupMenu(ViewDetailsActivity.this,imageView);
                    popup.getMenuInflater().inflate(R.menu.trip_item_menu, popup.getMenu());

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()){

                                case R.id.updateTripItem:
                                    Intent intent = new Intent(ViewDetailsActivity.this, AddTripActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("clickedItem",trip);
                                    intent.putExtras(bundle);
                                    startActivity(intent);

                                    Toast.makeText(ViewDetailsActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                                    break;
                                case R.id.deleteTripItem:
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
