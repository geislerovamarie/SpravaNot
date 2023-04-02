package com.example.spravanot.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spravanot.R;
import com.example.spravanot.activities.HandleFilesActivity;
import com.example.spravanot.activities.SheetmusicOfSetlistActivity;
import com.example.spravanot.interfaces.PassInfoSetlist;
import com.example.spravanot.interfaces.PassInfoSheetmusic;
import com.example.spravanot.models.Setlist;
import com.example.spravanot.models.Sheetmusic;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

public class SetlistAdapter extends RecyclerView.Adapter<SetlistAdapter.ViewHolderSetlist>{

    private Context context;
    Activity activity;

    private ArrayList<Setlist> setlists;
    private PassInfoSetlist info;

    public SetlistAdapter(Context context, Activity activity, ArrayList<Setlist> setlists, PassInfoSetlist info) {
        this.context = context;
        this.activity = activity;
        this.setlists = setlists;
        this.info = info;
    }

    @NonNull
    @Override
    public ViewHolderSetlist onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.setlist_item_row, parent, false);
        return new ViewHolderSetlist(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderSetlist holder, int position) {
        // get name and set text
        String name = String.valueOf(setlists.get(position).getName());
        holder.setlist_name_text.setText(name);

        // Edit button
        holder.setlist_edit_button.setOnClickListener(view -> {
            info.updateSetlist(holder.getAdapterPosition());
        });

        // Click and show setlist in SheetmusicOfSetlistActivity
        holder.setlistLayout.setOnClickListener(view -> {
            Setlist clicked = setlists.get(holder.getAdapterPosition());

            Intent intent = new Intent(context, SheetmusicOfSetlistActivity.class);
            intent.putExtra("setlist", clicked);
            activity.startActivityForResult(intent, 5);
        });

        // long hold -> delete
        holder.setlistLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // delete dialog
                AlertDialog.Builder dBuilder = new AlertDialog.Builder(context);
                dBuilder.setMessage(R.string.dialog_delete_item);
                dBuilder.setTitle(R.string.dialog_title_delete_item);

                // delete item
                dBuilder.setPositiveButton(R.string.yes, (dialogInterface, i) -> info.deleteSetlist(holder.getAdapterPosition(), setlists.get(holder.getAdapterPosition()).getId()));

                // cancel
                dBuilder.setNegativeButton(R.string.no, (dialogInterface, i) -> dialogInterface.cancel());

                // show the dialog
                AlertDialog dialog = dBuilder.create();
                dialog.show();
                return true;
            }
        });
    }


    @Override
    public int getItemCount() {
        return setlists.size();
    }

    // viewholder -----------------------------------------------------

    public class ViewHolderSetlist extends RecyclerView.ViewHolder {

        TextView setlist_name_text;
        ImageButton setlist_edit_button;
        LinearLayout setlistLayout;

        public ViewHolderSetlist(@NonNull View itemView) {
            super(itemView);
            setlist_name_text = itemView.findViewById(R.id.setlist_name_txt);
            setlist_edit_button = itemView.findViewById(R.id.setlist_edit_button);
            setlistLayout = itemView.findViewById(R.id.setlistLayout);
        }
    }
}