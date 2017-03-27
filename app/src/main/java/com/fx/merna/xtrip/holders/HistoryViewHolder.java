package com.fx.merna.xtrip.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fx.merna.xtrip.R;

/**
 * Created by bo on 21/03/2017.
 */

public class HistoryViewHolder extends RecyclerView.ViewHolder {

    private View convertView;

    private TextView title, year, from, to;
    private ImageView viewDetails, startTrip, imgSetting;


    HistoryViewHolder(View itemView) {
        super(itemView);
        convertView = itemView;
    }

    public TextView getTitle() {
        if (title == null)
            title = (TextView) convertView.findViewById(R.id.title);
        return title;
    }

    public TextView getYear() {
        if (year == null)
            year = (TextView) convertView.findViewById(R.id.txtDate);
        return year;
    }

    public TextView getFrom() {
        if (from == null)
            from = (TextView) convertView.findViewById(R.id.from);
        return from;
    }

    public TextView getTo() {
        if (to == null)
            to = (TextView) convertView.findViewById(R.id.to);
        return to;
    }

    public ImageView getViewDetails() {
        if (viewDetails == null)
            viewDetails = (ImageView) convertView.findViewById(R.id.view_trip);
        return viewDetails;
    }

    public ImageView getStartTrip() {
        if (startTrip == null)
            startTrip = (ImageView) convertView.findViewById(R.id.start_trip);
        return startTrip;
    }

    public ImageView getImgSetting() {
        if (imgSetting == null)
            imgSetting = (ImageView) convertView.findViewById(R.id.history_settings);
        return imgSetting;
    }
}

