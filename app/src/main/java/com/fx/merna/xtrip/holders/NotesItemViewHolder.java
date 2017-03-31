package com.fx.merna.xtrip.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fx.merna.xtrip.R;

/**
 * Created by Merna on 3/27/17.
 */

public class NotesItemViewHolder extends RecyclerView.ViewHolder {

    private View convertView;
    private TextView txtNote;
    private ImageButton btnDeleteNote;


    public NotesItemViewHolder(View itemView) {
        super(itemView);
        convertView = itemView;
    }

    public TextView getTxtNote() {
        if (txtNote == null)
            txtNote = (TextView) convertView.findViewById(R.id.txtNote);
        return txtNote;
    }

    public ImageButton getBtnDeleteNote() {
        if (btnDeleteNote == null)
            btnDeleteNote = (ImageButton) convertView.findViewById(R.id.btnDeleteNote);
        return btnDeleteNote;
    }
}