package com.fx.merna.xtrip.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.fx.merna.xtrip.R;
import com.fx.merna.xtrip.holders.NotesHeaderViewHolder;
import com.fx.merna.xtrip.holders.NotesItemViewHolder;

import java.util.ArrayList;

/**
 * Created by Merna on 3/27/17.
 */

public class NotesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    Context context;
    ArrayList<String> notes;

    public NotesRecyclerAdapter(Context context, ArrayList<String> notes) {
        this.context = context;
        this.notes = notes;
        Log.i("MY_TAG", "<in>" + notes.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_header_item, parent, false);
            return new NotesHeaderViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_list_item, parent, false);
            return new NotesItemViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if (holder instanceof NotesHeaderViewHolder) {
            final NotesHeaderViewHolder header = (NotesHeaderViewHolder) holder;
            final EditText edtTxtAddNote = header.getEdtTxtNote();

            if (notes.size() < 4) header.getBtnAddNote().setEnabled(true);

            header.getBtnAddNote().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("MY_TAG", edtTxtAddNote.getText().toString());
                    if (notes.size() < 4) {
                        notes.add(edtTxtAddNote.getText().toString());
                        notifyDataSetChanged();
                    }
                    if (notes.size() == 4) header.getBtnAddNote().setEnabled(false);
                }
            });

        } else if (holder instanceof NotesItemViewHolder) {
            final NotesItemViewHolder item = (NotesItemViewHolder) holder;
            final int pos = position;
            String currentItem = getItem(position - 1);
            item.getTxtNote().setText(currentItem);
            item.getBtnDeleteNote().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notes.remove(pos - 1);
                    notifyDataSetChanged();

                }
            });
        }

    }

    private String getItem(int position) {
        return notes.get(position);
    }

    @Override
    public int getItemCount() {
        return notes.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

}
