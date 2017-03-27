package com.fx.merna.xtrip.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fx.merna.xtrip.R;

/**
 * Created by Merna on 3/27/17.
 */

public class NotesHeaderViewHolder extends RecyclerView.ViewHolder {

    private View convertView;
    private EditText edtTxtNote;
    private Button btnAddNote;

    public NotesHeaderViewHolder(View itemView) {
        super(itemView);
        convertView = itemView;
    }

    public Button getBtnAddNote() {
        if (btnAddNote == null)
            btnAddNote = (Button) convertView.findViewById(R.id.btnAddNotes);
        return btnAddNote;
    }

    public EditText getEdtTxtNote() {
        if (edtTxtNote == null)
            edtTxtNote = (EditText) convertView.findViewById(R.id.edtTxtNotes);
        return edtTxtNote;
    }
}
