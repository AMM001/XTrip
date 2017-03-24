package com.fx.merna.xtrip.views.activities;


import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fx.merna.xtrip.R;
import com.fx.merna.xtrip.models.Trip;
import com.fx.merna.xtrip.utils.Alarm;
import com.fx.merna.xtrip.utils.AlarmBroadcastReceiver;
import com.fx.merna.xtrip.utils.Constants;
import com.fx.merna.xtrip.utils.DateParser;
import com.fx.merna.xtrip.utils.SHA;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;


public class AddTripActivity extends AppCompatActivity {

    EditText edtTripName, edtDate, edtTime, edtStartPoint, edtEndPoint;
    Button btnCreateTrip;
    RadioGroup rBtnTripType;
    PlaceAutocompleteFragment endPointAutocompleteFragment, startPointAutocompleteFragment;
    String startPoint = "", endPoint = "", startLong = "", startLat = "", endLong = "", endLat = "";
    Bundle bandleToEdit;
    Trip trip;
    Calendar calendar = Calendar.getInstance();
    private TextWatcher mTxtWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.i("MY_Tag", "Listener");
            checkAllFieldsIsEmpty();
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        bandleToEdit = this.getIntent().getExtras();

        if (bandleToEdit != null) {
            getSupportActionBar().setTitle("Edit Trip");
        } else {
            getSupportActionBar().setTitle("Add Trip");
        }

        checkAllFieldsIsEmpty();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!checkAnyFieldsIsEmpty()) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(AddTripActivity.this);
                    alert.setTitle("back");

                    alert.setMessage("when back data in fields will lost" +
                            ",Are you sure you want to back?");
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
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
                    return true;

                } else {
                    finish();
                    return true;
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        edtTripName = (EditText) findViewById(R.id.edtTripName);
        edtTripName.addTextChangedListener(mTxtWatcher);
        edtDate = (EditText) findViewById(R.id.edtDate);
        edtDate.addTextChangedListener(mTxtWatcher);
        edtTime = (EditText) findViewById(R.id.edtTime);
        edtTime.addTextChangedListener(mTxtWatcher);
        btnCreateTrip = (Button) findViewById(R.id.btnCreateTrip);
        rBtnTripType = (RadioGroup) findViewById(R.id.rBtnTripType);
        rBtnTripType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                checkAllFieldsIsEmpty();
            }
        });
        startPointAutocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.startPoint);
        edtStartPoint = ((EditText) startPointAutocompleteFragment.getView()
                .findViewById(R.id.place_autocomplete_search_input));
        edtStartPoint.addTextChangedListener(mTxtWatcher);

        endPointAutocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.endPoint);
        edtEndPoint = ((EditText) endPointAutocompleteFragment.getView()
                .findViewById(R.id.place_autocomplete_search_input));
        edtEndPoint.addTextChangedListener(mTxtWatcher);

        bandleToEdit = this.getIntent().getExtras();
        if (bandleToEdit != null) {

            trip = (Trip) bandleToEdit.getSerializable("clickedItem");

            edtTripName.setText(trip.getName());

            edtStartPoint.setText(trip.getStartPoint());
            startPoint = trip.getStartPoint();
            startLong = trip.getStartLong();
            startLat = trip.getStartLat();

            edtEndPoint.setText(trip.getEndPoint());
            endPoint = trip.getEndPoint();
            endLong = trip.getEndLong();
            endLat = trip.getEndLat();

            String[] arrDate = DateParser.parseLongDateToStrings(trip.getDate());
            edtDate.setText(arrDate[0]);
            edtTime.setText(arrDate[1]);

            RadioButton checked = (RadioButton) findViewById(Integer.parseInt(trip.getType()));
            checked.setChecked(true);

        }

        btnCreateTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = edtTripName.getText().toString();
                String type = String.valueOf(rBtnTripType.getCheckedRadioButtonId());

//                String dateTime = ((EditText) findViewById(R.id.edtDate)).getText().toString() +
//                        " " + ((EditText) findViewById(R.id.edtTime)).getText().toString();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference myRef = database.getReference("trips").child(user.getUid());
                Trip newTrip;

                if (bandleToEdit != null) {
                    newTrip = new Trip(trip.getId(), name, startPoint, startLong, startLat, endPoint, endLong, endLat, type, calendar.getTimeInMillis());
                    myRef.child(trip.getId()).setValue(newTrip);

                } else {
                    String key = myRef.push().getKey();
                    newTrip = new Trip(key, name, startPoint, startLong, startLat, endPoint, endLong, endLat, type, calendar.getTimeInMillis());
                    myRef.child(key).setValue(newTrip);
                }

                //Create new or update PendingIntent and add it to the AlarmManager
                Alarm.setAlarm(getApplicationContext(), newTrip, calendar.getTimeInMillis());

            }
        });

        handlePlaceSelection();

    }

    public void handlePlaceSelection() {

        // --------------- start point handling ---------------

        startPointAutocompleteFragment.setHint("Enter start Point");
        edtStartPoint.setTextSize(14);

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build();
        startPointAutocompleteFragment.setFilter(typeFilter);

        startPointAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                startPoint = place.getAddress().toString();
                Log.i("place_tag", "Place: " + place.getName());
                Log.i("place_tag", "Place: " + place.getLatLng().longitude);
                Log.i("place_tag", "Place: " + place.getLatLng().latitude);

                startLong = String.valueOf(place.getLatLng().longitude);
                startLat = String.valueOf(place.getLatLng().latitude);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("place_tag", "An error occurred: " + status);
            }
        });

        // --------------- end point handling ---------------

        edtEndPoint.setHint("Enter end Point");
        edtEndPoint.setTextSize(14);

        endPointAutocompleteFragment.setFilter(typeFilter);

        endPointAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                endPoint = place.getAddress().toString();
                Log.i("place_tag", "Place: " + place.getName());
                endLong = String.valueOf(place.getLatLng().longitude);
                endLat = String.valueOf(place.getLatLng().latitude);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("place_tag", "An error occurred: " + status);
            }
        });

    }

    public void edtDateAction(View v) {

        final Calendar mcurrentDate = Calendar.getInstance();

        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog mDatePicker = new DatePickerDialog(AddTripActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {

                calendar.set(Calendar.YEAR, selectedyear);
                calendar.set(Calendar.MONTH, selectedmonth);
                calendar.set(Calendar.DAY_OF_MONTH, selectedday);

                edtDate.setText(selectedday + "-" + selectedmonth + "-" + selectedyear);
            }
        }, mYear, mMonth, mDay);

        mDatePicker.setTitle("Select date");
        mDatePicker.show();
    }

    public void edtTimeAction(View v) {

        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);


        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 00);

                        edtTime.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();

    }

    public void checkAllFieldsIsEmpty() {

        boolean name = edtTripName.getText().toString().trim().isEmpty();
        boolean date = edtDate.getText().toString().trim().isEmpty();
        boolean time = edtTime.getText().toString().trim().isEmpty();
        boolean type;

        if (rBtnTripType.getCheckedRadioButtonId() == -1)
            type = true;
        else
            type = false;

        boolean startPoint = ((EditText) findViewById(R.id.place_autocomplete_search_input))
                .getText().toString().trim().isEmpty();
        boolean endPoint = ((EditText) endPointAutocompleteFragment.getView()
                .findViewById(R.id.place_autocomplete_search_input))
                .getText().toString().trim().isEmpty();
        Log.i("MY_Tag", "Before" + name + date + time + type + startPoint + endPoint);
        if (name || date || time || type || startPoint || endPoint) {
            Log.i("MY_Tag", "in True");
            btnCreateTrip.setEnabled(false);
        } else {
            Log.i("MY_Tag", "in false");
            btnCreateTrip.setEnabled(true);
        }

    }

    public Boolean checkAnyFieldsIsEmpty() {

        boolean name = edtTripName.getText().toString().trim().isEmpty();
        boolean date = edtDate.getText().toString().trim().isEmpty();
        boolean time = edtTime.getText().toString().trim().isEmpty();
        boolean type;

        if (rBtnTripType.getCheckedRadioButtonId() == -1)
            type = true;
        else
            type = false;

        boolean startPoint = ((EditText) findViewById(R.id.place_autocomplete_search_input))
                .getText().toString().trim().isEmpty();
        boolean endPoint = ((EditText) endPointAutocompleteFragment.getView()
                .findViewById(R.id.place_autocomplete_search_input))
                .getText().toString().trim().isEmpty();
        Log.i("MY_Tag", "Before" + name + date + time + type + startPoint + endPoint);
        if (name && date && time && type && startPoint && endPoint) {
            Log.i("MY_Tag", "in True");
            return true;
        } else {
            Log.i("MY_Tag", "in false");
            return false;
        }

    }
    
}
