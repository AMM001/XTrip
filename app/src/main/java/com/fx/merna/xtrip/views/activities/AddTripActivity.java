package com.fx.merna.xtrip.views.activities;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import com.fx.merna.xtrip.R;
import com.fx.merna.xtrip.models.Trip;
import com.fx.merna.xtrip.utils.Constants;
import com.fx.merna.xtrip.utils.DateParser;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class AddTripActivity extends AppCompatActivity {

    EditText edtTripName, edtDate, edtTime;
    Button btnCreateTrip;
    RadioGroup rBtnTripType;
    String startPoint = "", endPoint = "", startLong = "", startLat = "", endLong = "", endLat = "";
    Bundle bandleToEdit;
    Trip trip;

    @Override
    protected void onStart() {
        super.onStart();
        bandleToEdit = this.getIntent().getExtras();
        if (bandleToEdit != null) {
            getSupportActionBar().setTitle("Edit Trip");
        } else {
            getSupportActionBar().setTitle("Add Trip");
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();



        edtTripName = (EditText) findViewById(R.id.edtTripName);
        edtDate = (EditText) findViewById(R.id.edtDate);
        edtTime = (EditText) findViewById(R.id.edtTime);
        btnCreateTrip = (Button) findViewById(R.id.btnCreateTrip);
        rBtnTripType = (RadioGroup) findViewById(R.id.rBtnTripType);

        if (bandleToEdit != null) {
            trip = (Trip) bandleToEdit.getSerializable("clickedItem");

            edtTripName.setText(trip.getName());

            ((EditText) findViewById(R.id.place_autocomplete_search_input)).setText(trip.getStartPoint());

            PlaceAutocompleteFragment endPointAutocompleteFragment = (PlaceAutocompleteFragment)
                    getFragmentManager().findFragmentById(R.id.endPoint);
            ((EditText) endPointAutocompleteFragment.getView()
                    .findViewById(R.id.place_autocomplete_search_input))
                    .setText(trip.getEndPoint());

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


                String dateTime = ((EditText) findViewById(R.id.edtDate)).getText().toString() +
                        " " + ((EditText) findViewById(R.id.edtTime)).getText().toString();


                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference myRef = database.getReference("trips").child(user.getUid());

                if (bandleToEdit != null) {


                    myRef.child(trip.getId()).setValue(trip);

                } else {
                    String key = myRef.push().getKey();
                    Trip newTrip = new Trip(key, name, startPoint, startLong, startLat, endPoint, endLong, endLat, type, DateParser.parseStringDateToLong(dateTime));
                    myRef.child(key).setValue(newTrip);
                }

            }
        });


        handlePlaceSelection();

    }

    public void handlePlaceSelection() {

        // --------------- start point handling ---------------
        PlaceAutocompleteFragment startPointAutocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.startPoint);

        startPointAutocompleteFragment.setHint("Enter start Point");
        ((EditText) startPointAutocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setTextSize(14);

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

        PlaceAutocompleteFragment endPointAutocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.endPoint);

        endPointAutocompleteFragment.setHint("Enter end Point");
        ((EditText) endPointAutocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setTextSize(14);

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

        Calendar mcurrentDate = Calendar.getInstance();

        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog mDatePicker = new DatePickerDialog(AddTripActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
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

                        edtTime.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();

    }

}
