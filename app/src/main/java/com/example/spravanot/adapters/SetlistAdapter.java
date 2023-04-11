package com.example.spravanot.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spravanot.R;
import com.example.spravanot.activities.SheetmusicOfSetlistActivity;
import com.example.spravanot.interfaces.PassInfoSetlist;
import com.example.spravanot.models.Setlist;
import com.example.spravanot.utils.FilterOptions;

import java.util.ArrayList;

public class SetlistAdapter extends RecyclerView.Adapter<SetlistAdapter.ViewHolderSetlist> implements Filterable {

    private Context context;
    Activity activity;

    private ArrayList<Setlist> setlists;
    private PassInfoSetlist info;

    ArrayList<Setlist> setlistsCopyFull;
    FilterOptions filterOption;

    public SetlistAdapter(Context context, Activity activity, ArrayList<Setlist> setlists, PassInfoSetlist info, FilterOptions filterOption) {
        this.context = context;
        this.activity = activity;
        this.setlists = new ArrayList<Setlist>(setlists);
        this.info = info;
        this.filterOption = filterOption;
        this.setlistsCopyFull = new ArrayList<Setlist>(setlists);
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
        int truePos = getTruePositionWhileFiltering(setlists.get(position));
        // get name and set text
        String name = String.valueOf(setlists.get(position).getName());
        holder.setlist_name_text.setText(name);

        // Edit button
        holder.setlist_edit_button.setOnClickListener(view -> {
            info.updateSetlist(truePos);
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
                dBuilder.setPositiveButton(R.string.yes, (dialogInterface, i) ->
                        info.deleteSetlist(truePos, setlists.get(holder.getAdapterPosition()).getId()));

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

    // Filter ---------------------------------------------------------------------------
    @Override
    public Filter getFilter() {
        switch (filterOption){
            case TAG:
                return tagFilter;
            default:
                return nameFilter;
        }
    }

    private Filter tagFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Setlist> filtered = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filtered.addAll(setlistsCopyFull);
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (Setlist s : setlistsCopyFull){
                    ArrayList<String> tags = s.getTags();
                    if(tags == null) continue;

                    for (int i = 0; i < tags.size(); i++) {
                        String str = tags.get(i).toLowerCase();
                        if(str.contains(filterPattern)) {
                            filtered.add(s);
                            break;
                        }
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filtered;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            setlists.clear();
            setlists.addAll((ArrayList)filterResults.values);
            notifyDataSetChanged();
        }
    };


    private Filter nameFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Setlist> filtered = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filtered.addAll(setlistsCopyFull);
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (Setlist s : setlistsCopyFull){
                    if(s.getName() == null) continue;
                    else if (s.getName().toLowerCase().contains(filterPattern)) filtered.add(s);
                }
            }
            FilterResults results = new FilterResults();
            results.values = filtered;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            setlists.clear();
            setlists.addAll((ArrayList)filterResults.values);
            notifyDataSetChanged();
        }
    };

    int getTruePositionWhileFiltering(Setlist s) {
        for (int i = 0; i < setlistsCopyFull.size(); i++) {
            if(setlistsCopyFull.get(i).getId() == s.getId()) return i;
        }
        return -1;
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