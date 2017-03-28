package com.fx.merna.xtrip.utils;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

/**
 * Created by mustafa on 22/03/17.
 */

public class MySingleTone {

    private static MySingleTone mInstance;
    private Context con;
    private RequestQueue mRequestQueue;



    private MySingleTone(Context conn){
        con=conn;
        mRequestQueue=getRequestQueue();

    }

    public RequestQueue getRequestQueue() {

        if (mRequestQueue==null) {
            Cache cache = new DiskBasedCache(con.getCacheDir(), 1024 * 1024 * 3);
            Network network = new BasicNetwork(new HurlStack());

            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
        }

        return mRequestQueue;
    }

    public static synchronized  MySingleTone getInstance(Context con){
        if(mInstance==null) {
            mInstance = new MySingleTone(con);
        }
        return mInstance;
    }

    public void addToRequestQueu(Request req){
        mRequestQueue.add(req);
    }
}
