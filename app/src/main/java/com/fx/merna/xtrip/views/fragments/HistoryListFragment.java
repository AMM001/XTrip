package com.fx.merna.xtrip.views.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fx.merna.xtrip.R;

public class HistoryListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i("Tag", "onCreateView HistoryLISTFragment");

        return inflater.inflate(R.layout.fragment_history_list, container, false);
    }




}
