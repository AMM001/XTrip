package com.fx.merna.xtrip.views.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fx.merna.xtrip.R;
import com.fx.merna.xtrip.adapters.TripHistoryFirebaseAdapter;
import com.fx.merna.xtrip.holders.UpcomingViewHolder;
import com.fx.merna.xtrip.models.Trip;
import com.fx.merna.xtrip.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Random;

public class ReminderActivity extends Activity {

    TextView txtDialogTitle, txtMore;
    Button btnStart, btnLater, btnCancel;
    View convertView;
    Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        txtDialogTitle = (TextView) findViewById(R.id.txtDialogTitle);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnLater = (Button) findViewById(R.id.btnLater);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        txtMore = (TextView) findViewById(R.id.txtMore);


        Bundle bundle = this.getIntent().getExtras();
        final Trip trip = (Trip) bundle.getSerializable(Constants.reminderBundle);
        txtDialogTitle.setText(trip.getName());

        //------- start notes  code ---------
        LinearLayout notesLinearLayout = (LinearLayout) findViewById(R.id.linearNotesList);
        if (trip.getNotes() != null) {

            for (int i = 0; i < trip.getNotes().size(); i++) {
                TextView note = new TextView(this);
                note.setText("- " + trip.getNotes().get(i));

                notesLinearLayout.addView(note);
            }
        }

        txtMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("tripDetails", trip);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        //--------- end notes code ----------

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* DatabaseReference myRef = database.getReference("trips").child(user.getUid())
                        .child(trip.getId()).child("status");
                myRef.setValue("Done");

                Uri uri = Uri.parse("google.navigation:q=" + trip.getEndLat() + "," + trip.getEndLong() + "&mode=d");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
                finish();*/

                //----------- change status to Done in DB  when start trip ----------

                DatabaseReference myRef = database.getReference("trips").child(user.getUid())
                        .child(trip.getId()).child("status");

                DatabaseReference ref = database.getReference("trips").child(user.getUid())
                        .child(trip.getId());

                //Round Trip Handling
                if (trip.getType().equals(Constants.roundTrip)) {
                  //  UpcomingViewHolder holder=new UpcomingViewHolder(convertView);
                    //  ImageView roundImg=holder.getTripStatus();
                   // roundImg.setImageResource(R.drawable.roundtrip);
                   // holder.setTripStatus(roundImg);
                   // roundImg.setVisibility(View.VISIBLE);
                    String newEndLat, newEndLong, newStartLat, newStartLong, newStartPoint, newEndPoint;
                    newStartLat = trip.getEndLat();
                    newEndLat = trip.getStartLat();
                    newStartLong = trip.getEndLong();
                    newEndLong = trip.getStartLong();
                    newStartPoint = trip.getEndPoint();
                    newEndPoint = trip.getStartPoint();

                    trip.setStartLat(newStartLat);
                    trip.setStartLong(newStartLong);
                    trip.setStartPoint(newStartPoint);
                    trip.setEndPoint(newEndPoint);
                    trip.setEndLong(newEndLong);
                    trip.setEndLat(newEndLat);
                    trip.setType(Constants.onDirectionTrip);


                    uri = Uri.parse("google.navigation:q=" + trip.getEndLat() + "," + trip.getEndLong() + "&mode=d");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setPackage("com.google.android.apps.maps");
                    startActivity(intent);
                    ref.setValue(trip);


                } else {
                    myRef.setValue("Done");
                    Uri uri = Uri.parse("google.navigation:q=" + trip.getEndLat() + "," + trip.getEndLong() + "&mode=d");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }

            }
        });

       /* btnLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Notification code here

                NotificationCompat.Builder mBuilder=
                        (NotificationCompat.Builder) new NotificationCompat
                                .Builder(ReminderActivity.this).setSmallIcon(R.mipmap.alarm_clock).setContentTitle(trip.getName())
                                .setContentText("Start your trip now :)").setAutoCancel(true);

              //  Intent resultIntent=new Intent(ReminderActivity.this,ResultActivity.class);

                DatabaseReference myRef = database.getReference("trips").child(user.getUid())
                        .child(trip.getId()).child("status");
                myRef.setValue("Done");

                Uri uri = Uri.parse("google.navigation:q=" + trip.getEndLat() + "," + trip.getEndLong() + "&mode=d");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setPackage("com.google.android.apps.maps");
                TaskStackBuilder stackBuilder=TaskStackBuilder.create(ReminderActivity.this);
                //stackBuilder.addParentStack();
                stackBuilder.addNextIntent(intent);

                PendingIntent pendingIntent=stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(pendingIntent);
                NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                Random random = new Random();
                int m = random.nextInt(9999 - 1000) + 1000;
                notificationManager.notify(m, mBuilder.build());
                //  notificationManager.notify(m,mBuilder.build());



                Toast.makeText(getApplicationContext(), " See Notification", Toast.LENGTH_LONG).show();
                finish();
            }
        });*/

        btnLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PendingIntent pIntent;


                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("trips").child(user.getUid())
                        .child(trip.getId()).child("status");

                DatabaseReference ref = database.getReference("trips").child(user.getUid())
                        .child(trip.getId());
                Intent intent=new Intent(Intent.ACTION_VIEW, uri);;

                pIntent = PendingIntent.getActivity(ReminderActivity.this, (int) System.currentTimeMillis(), intent, 0);

                // PendingIntent pCancel= PendingIntent.getActivity(ReminderActivity.this, (int) System.currentTimeMillis(), intent, 0);

                Notification n  = new Notification.Builder(ReminderActivity.this)
                        .setContentTitle(trip.getName())
                        .setContentText("Would you like to start your trip now :) ?")
                        .setSmallIcon(R.mipmap.alarm_clock).setAutoCancel(true)
                        .setContentIntent(pIntent).build();
                      //  .addAction(R.drawable.about_icon, "Cancel", pIntent).build();
                        NotificationManager notificationManager =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                Random random = new Random();
                int m = random.nextInt(9999 - 1000) + 1000;

                notificationManager.notify(m, n);

                finish();
                //Round Trip Handling
                if (trip.getType().equals(Constants.roundTrip)) {
                    String newEndLat, newEndLong, newStartLat, newStartLong, newStartPoint, newEndPoint;
                    newStartLat = trip.getEndLat();
                    newEndLat = trip.getStartLat();
                    newStartLong = trip.getEndLong();
                    newEndLong = trip.getStartLong();
                    newStartPoint = trip.getEndPoint();
                    newEndPoint = trip.getStartPoint();

                    trip.setStartLat(newStartLat);
                    trip.setStartLong(newStartLong);
                    trip.setStartPoint(newStartPoint);
                    trip.setEndPoint(newEndPoint);
                    trip.setEndLong(newEndLong);
                    trip.setEndLat(newEndLat);
                    trip.setType(Constants.onDirectionTrip);
                    uri = Uri.parse("google.navigation:q=" + trip.getEndLat() + "," + trip.getEndLong() + "&mode=d");
                   // intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setPackage("com.google.android.apps.maps");
                   // pIntent = PendingIntent.getActivity(ReminderActivity.this, (int) System.currentTimeMillis(), intent, 0);
                    ref.setValue(trip);

                } else {
                    myRef.setValue("Done");
                    uri = Uri.parse("google.navigation:q=" + trip.getEndLat() + "," + trip.getEndLong() + "&mode=d");
                    //intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setPackage("com.google.android.apps.maps");
                  //  pIntent = PendingIntent.getActivity(ReminderActivity.this, (int) System.currentTimeMillis(), intent, 0);
                }

                myRef.setValue("Upcoming");

            }

        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
