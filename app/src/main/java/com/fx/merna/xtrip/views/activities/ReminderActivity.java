package com.fx.merna.xtrip.views.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fx.merna.xtrip.R;
import com.fx.merna.xtrip.models.Trip;
import com.fx.merna.xtrip.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReminderActivity extends Activity {

    TextView txtDialogTitle;
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

        Bundle bundle = this.getIntent().getExtras();
        final Trip trip = (Trip) bundle.getSerializable(Constants.reminderBundle);
        txtDialogTitle.setText(trip.getName());

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference myRef = database.getReference("trips").child(user.getUid())
                        .child(trip.getId()).child("status");
                myRef.setValue("Done");

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
                Toast.makeText(getApplicationContext(), " See Notification", Toast.LENGTH_LONG).show();
                finish();
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
