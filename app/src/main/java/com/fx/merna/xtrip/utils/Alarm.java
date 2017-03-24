package com.fx.merna.xtrip.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.fx.merna.xtrip.models.Trip;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by Merna on 3/24/17.
 */

public class Alarm {

    public static void setAlarm(Context context, Trip trip, Long dateTime) {
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.reminderBundle, trip);
        intent.putExtras(bundle);

        PendingIntent pendingIntent = PendingIntent
                .getBroadcast(context, SHA.getIntegerID(trip.getId()), intent, 0);

        Log.i("MY_TAG", "added alarm >>" + SHA.getIntegerID(trip.getId()));

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, dateTime, pendingIntent);

        Toast.makeText(context, "Alarm set on " + DateParser.parseLongDateToStrings(trip.getDate())[0] + " ", Toast.LENGTH_LONG).show();

    }

    public static void cancelAlarm(Context context, int alarmID) {
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, alarmID, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.cancel(sender);
        Log.i("MY_TAG", "Cancel alarm >> " + alarmID);
    }
}
