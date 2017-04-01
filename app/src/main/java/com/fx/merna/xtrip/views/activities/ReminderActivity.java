package com.fx.merna.xtrip.views.activities;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fx.merna.xtrip.R;
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

                DatabaseReference myRef = database.getReference("trips").child(user.getUid())
                        .child(trip.getId()).child("status");
                myRef.setValue("#Done");

                Uri uri = Uri.parse("google.navigation:q=" + trip.getEndLat() + "," + trip.getEndLong() + "&mode=d");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
                finish();

            }
        });

        btnLater.setOnClickListener(new View.OnClickListener() {
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
                myRef.setValue("#Done");

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
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference myRef = database.getReference("trips").child(user.getUid())
                        .child(trip.getId()).child("status");
                myRef.setValue("#Cancel");
                finish();
            }
        });

    }
}
