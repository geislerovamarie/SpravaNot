package com.example.spravanot.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spravanot.R;
import com.example.spravanot.interfaces.PassInfoSheetmusic;
import com.example.spravanot.models.Sheetmusic;
import com.example.spravanot.utils.FilterOptions;

import java.util.ArrayList;

public class AddSheetmusicToSetlistAdapter extends RecyclerView.Adapter<AddSheetmusicToSetlistAdapter.ViewHolderSheetmusicToSetlist> implements Filterable {

    private Context context;
    Activity activity;

    private ArrayList<Sheetmusic> sheetmusic;
    private ArrayList<Boolean> favorite;
    private PassInfoSheetmusic info;
    private boolean showingSetlistFavorite;
    private ArrayList<Sheetmusic> selected;

    ArrayList<Sheetmusic> sheetmusicCopyFull;
    FilterOptions filterOption;

    public AddSheetmusicToSetlistAdapter(Activity activity, Context context, ArrayList sheetmusic, PassInfoSheetmusic info, ArrayList favorite, boolean showingSetlistFavorite, ArrayList<Sheetmusic> alreadyStoredSheets, FilterOptions filterOption) {
        this.activity = activity;
        this.context = context;
        this.sheetmusic = sheetmusic;
        this.info = info;
        this.favorite = favorite;
        selected = alreadyStoredSheets;
        this.showingSetlistFavorite = showingSetlistFavorite;

        this.sheetmusicCopyFull = new ArrayList<Sheetmusic>(sheetmusic);
        this.filterOption = filterOption;
    }

    @NonNull
    @Override
    public ViewHolderSheetmusicToSetlist onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.sheetmusic_row, parent, false);
        return new ViewHolderSheetmusicToSetlist(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderSheetmusicToSetlist holder, int position) {

        // get name and author and if favorite
        String sh_name = String.valueOf(sheetmusic.get(position).getName());
        String sh_author = String.valueOf(sheetmusic.get(position).getAuthor()).equals("null") ? context.getResources().getString(R.string.text_unknown) : String.valueOf(sheetmusic.get(position).getAuthor());
        int truePos = getTruePositionWhileFiltering(sheetmusic.get(position));
        Boolean isFav = favorite.get(position);

        // Set text and color
        holder.sheetmusic_name_text.setText(sh_name);
        holder.sheetmusic_author_text.setText(sh_author);

        Sheetmusic click = sheetmusic.get(position);
        if(setlistContains(selected, click)) holder.sheetmusic_card.setBackgroundResource(R.color.teal_700);
        else holder.sheetmusic_card.setBackgroundResource(com.google.android.material.R.color.cardview_light_background);

        // Click and mark sheetmusic
        holder.sheetmusicLayout.setOnClickListener(view -> {
            Sheetmusic clicked = sheetmusic.get(holder.getAdapterPosition());
            if(setlistContains(selected, clicked)){
                selected = setlistRemove(selected, clicked);
                holder.sheetmusic_card.setBackgroundResource(androidx.cardview.R.color.cardview_light_background);
                info.addSheetmusicToSetlist(clicked, false);
                //notifyDataSetChanged();
            }else{
                selected.add(clicked);
                holder.sheetmusic_card.setBackgroundResource(R.color.teal_700);
                info.addSheetmusicToSetlist(clicked, true);
                //notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return sheetmusic.size();
    }

    public boolean setlistContains(ArrayList<Sheetmusic> sArr, Sheetmusic sItem){
        boolean ret = false;
        for (int i = 0; i < sArr.size(); i++) {
            if(sArr.get(i).getId() == sItem.getId()) return true;
        }
        return ret;
    }

    public ArrayList<Sheetmusic> setlistRemove(ArrayList<Sheetmusic> sArr, Sheetmusic sItem){
        for (int i = 0; i < sArr.size(); i++) {
            if(sArr.get(i).getId() == sItem.getId()) sArr.remove(i);
        }
        return sArr;
    }

    // Filter ---------------------------------------------------------------------------
    @Override
    public Filter getFilter() {
        switch (filterOption){
            case TAG:
                return tagFilter;
            case AUTHOR:
                return authorFilter;
            default:
                return nameFilter;
        }
    }

    private Filter authorFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Sheetmusic> filtered = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filtered.addAll(sheetmusicCopyFull);
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (Sheetmusic s : sheetmusicCopyFull){
                    if(s.getAuthor() == null) continue;
                    else if (s.getAuthor().toLowerCase().contains(filterPattern)) filtered.add(s);
                }
            }
            FilterResults results = new FilterResults();
            results.values = filtered;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            sheetmusic.clear();
            sheetmusic.addAll((ArrayList)filterResults.values);
            notifyDataSetChanged();
        }
    };

    private Filter tagFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Sheetmusic> filtered = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filtered.addAll(sheetmusicCopyFull);
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (Sheetmusic s : sheetmusicCopyFull){
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
            sheetmusic.clear();
            sheetmusic.addAll((ArrayList)filterResults.values);
            notifyDataSetChanged();
        }
    };


    private Filter nameFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Sheetmusic> filtered = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filtered.addAll(sheetmusicCopyFull);
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (Sheetmusic s : sheetmusicCopyFull){
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
            sheetmusic.clear();
            sheetmusic.addAll((ArrayList)filterResults.values);
            notifyDataSetChanged();
        }
    };

    int getTruePositionWhileFiltering(Sheetmusic s) {
        for (int i = 0; i < sheetmusicCopyFull.size(); i++) {
            if(sheetmusicCopyFull.get(i).getId() == s.getId()) return i;
        }
        return -1;
    }

    // viewholder  and interface --------------------------------

    public class ViewHolderSheetmusicToSetlist extends RecyclerView.ViewHolder {

        TextView sheetmusic_name_text, sheetmusic_author_text;
        ImageButton sheetmusic_edit_button;
        ImageButton sheetmusic_favorite_button;
        LinearLayout sheetmusicLayout;
        CardView sheetmusic_card;

        public ViewHolderSheetmusicToSetlist(@NonNull View itemView) {
            super(itemView);
            sheetmusic_name_text = itemView.findViewById(R.id.sheetmusic_name_txt);
            sheetmusic_author_text = itemView.findViewById(R.id.sheetmusic_author_txt);
            sheetmusic_edit_button = itemView.findViewById(R.id.sheetmusic_edit_button);
            sheetmusic_favorite_button = itemView.findViewById(R.id.sheetmusic_favorite_button);
            sheetmusicLayout = itemView.findViewById(R.id.sheetmusicLayout);
            sheetmusic_card = itemView.findViewById(R.id.sheets_card);

            sheetmusic_edit_button.setVisibility(View.GONE);
            sheetmusic_favorite_button.setVisibility(View.GONE);
        }
    }
}