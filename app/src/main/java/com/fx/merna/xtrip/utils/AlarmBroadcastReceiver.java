package com.fx.merna.xtrip.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.fx.merna.xtrip.views.activities.ReminderActivity;

/**
 * Created by Merna on 3/22/17.
 */

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent i = new Intent(context, ReminderActivity.class);
        i.putExtras(intent.getExtras());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

        Toast.makeText(context, "alarm....", Toast.LENGTH_LONG).show();
    }
}
