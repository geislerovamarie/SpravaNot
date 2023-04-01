package com.example.spravanot.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spravanot.R;
import com.example.spravanot.interfaces.PassInfoSheetmusic;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class EditTagsAdapter extends RecyclerView.Adapter<EditTagsAdapter.ViewHolderTags> {

    Context context;
    Activity activity;
    private PassInfoSheetmusic info;

    private ArrayList<String> tags;

    public EditTagsAdapter(Context context, Activity activity, ArrayList<String> tags, PassInfoSheetmusic info) {
        this.context = context;
        this.activity = activity;
        this.tags = tags;
        this.info = info;
    }

    @NonNull
    @Override
    public ViewHolderTags onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.tags_row, parent, false);
        return new ViewHolderTags(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTags holder, int position) {
        // Set text
        holder.tags_name_text.setText(tags.get(position));

        // Delete on hold
        holder.tags_name_text.setOnLongClickListener(view -> {
            AtomicBoolean ret = new AtomicBoolean(false);
            // ask wheter really delete
            AlertDialog.Builder dBuilder = new AlertDialog.Builder(context);
            dBuilder.setMessage(R.string.dialog_delete_item);
            dBuilder.setTitle(R.string.dialog_title_delete_item);

            // delete item
            dBuilder.setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                info.deleteTag(holder.getAdapterPosition(), tags.get(holder.getAdapterPosition()));
                ret.set(true);
            });

            // cancel
            dBuilder.setNegativeButton(R.string.no, (dialogInterface, i) -> dialogInterface.cancel());

            // show the dialog
            AlertDialog dialog = dBuilder.create();
            dialog.show();
            return ret.get();
        });
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    public class ViewHolderTags extends RecyclerView.ViewHolder {

        TextView tags_name_text;
        LinearLayout filesLayout;

        public ViewHolderTags(@NonNull View itemView) {
            super(itemView);
            tags_name_text = itemView.findViewById(R.id.tags_row_name);
            filesLayout = itemView.findViewById(R.id.tags_layout);
        }
    }
}
