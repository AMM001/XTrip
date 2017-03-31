package com.fx.merna.xtrip;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Merna on 3/31/17.
 */

public class XPathApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
