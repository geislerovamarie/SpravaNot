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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spravanot.interfaces.PassInfoSheetmusic;
import com.example.spravanot.R;
import com.example.spravanot.models.Sheetmusic;
import com.example.spravanot.activities.HandleFilesActivity;
import com.example.spravanot.utils.FilterOptions;

import java.util.ArrayList;

public class SheetmusicAdapter extends RecyclerView.Adapter<SheetmusicAdapter.ViewHolderSheetmusic> implements Filterable {

    private Context context;
    Activity activity;

    private ArrayList<Sheetmusic> sheetmusicOfSetlist;
    private ArrayList<Boolean> favorite;
    private PassInfoSheetmusic info;
    private boolean showingSetlistFavorite;

    ArrayList<Sheetmusic> sheetmusicCopyFull;
    FilterOptions filterOption;

    public SheetmusicAdapter(Activity activity, Context context, ArrayList sheetmusicOfSetlist, PassInfoSheetmusic info, ArrayList favorite, boolean showingSetlistFavorite, FilterOptions filterOption) {
        this.activity = activity;
        this.context = context;
        this.sheetmusicOfSetlist = new ArrayList<Sheetmusic>(sheetmusicOfSetlist);
        this.info = info;
        this.favorite = favorite;
        this.showingSetlistFavorite = showingSetlistFavorite;
        this.sheetmusicCopyFull = new ArrayList<Sheetmusic>(sheetmusicOfSetlist);
        this.filterOption = filterOption;
    }

    @NonNull
    @Override
    public ViewHolderSheetmusic onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.sheetmusic_row, parent, false);
        return new ViewHolderSheetmusic(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderSheetmusic holder, int position) {

        // get name and author and if favorite
        String sh_name = String.valueOf(sheetmusicOfSetlist.get(position).getName());
        String sh_author = String.valueOf(sheetmusicOfSetlist.get(position).getAuthor()).equals("null") ? context.getResources().getString(R.string.text_unknown) : String.valueOf(sheetmusicOfSetlist.get(position).getAuthor());
        int truePos = getTruePositionWhileFiltering(sheetmusicOfSetlist.get(position));
        Boolean isFav = favorite.get(truePos);

        // Set text and color
        holder.sheetmusic_name_text.setText(sh_name);
        holder.sheetmusic_author_text.setText(sh_author);
        if(isFav){
            holder.sheetmusic_favorite_button.setImageResource(R.drawable.ic_heart_filled);
        }else{
            holder.sheetmusic_favorite_button.setImageResource(R.drawable.ic_heart_empty);
        }

        // Edit button
        holder.sheetmusic_edit_button.setOnClickListener(view -> {
            Sheetmusic sh = sheetmusicOfSetlist.get(holder.getAdapterPosition());
            info.updateSheetmusic(getTruePositionWhileFiltering(sh));
        });

        // Add to favorites button
        holder.sheetmusic_favorite_button.setOnClickListener(view -> {
            if (showingSetlistFavorite) {
                Toast.makeText(context, R.string.error_toggle_favorite_in_favorite, Toast.LENGTH_SHORT).show();
            } else {
                Sheetmusic sh = sheetmusicOfSetlist.get(holder.getAdapterPosition());
                int pos = getTruePositionWhileFiltering(sh);

                info.toggleFavorite(pos, sh.getId());
                boolean fav = favorite.get(pos);
                favorite.set(pos, !fav);
                if (!fav) {
                    holder.sheetmusic_favorite_button.setImageResource(R.drawable.ic_heart_filled);
                } else {
                    holder.sheetmusic_favorite_button.setImageResource(R.drawable.ic_heart_empty);
                }
            }
        });

        // Click and show sheetmusic
        holder.sheetmusicLayout.setOnClickListener(view -> {
            Sheetmusic clicked = sheetmusicOfSetlist.get(holder.getAdapterPosition());
            int pdfFiles = 0;
            int jpgFiles = 0;

            // check what types are stored
            for (int i = 0; i < clicked.getFiles().size(); i++) {
                String path = clicked.getFiles().get(i);
                String extension = path.substring(path.lastIndexOf(".") +1);
                if(extension.equals("pdf")) pdfFiles++;
                else jpgFiles++;

                if(pdfFiles > 0 && jpgFiles > 0) break;
            }

            if(pdfFiles == 0 && jpgFiles == 0){
                Toast.makeText(context, R.string.no_available_data, Toast.LENGTH_SHORT).show();
            }else if(pdfFiles > 0 && jpgFiles == 0){
                showPdfs(clicked);
            }else if(pdfFiles == 0 && jpgFiles > 0){
                showJpgs(clicked);
            }else{
                dialogPdfOrJpg(clicked);
            }
        });

        // long hold -> delete
        holder.sheetmusicLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Sheetmusic sh = sheetmusicOfSetlist.get(holder.getAdapterPosition());
                int pos = getTruePositionWhileFiltering(sh);

                // delete dialog
                AlertDialog.Builder dBuilder = new AlertDialog.Builder(context);
                dBuilder.setMessage(R.string.dialog_delete_item);
                dBuilder.setTitle(R.string.dialog_title_delete_item);

                // delete item
                dBuilder.setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                    info.deleteSheetmusic(pos, sh.getId());
                });
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
        return sheetmusicOfSetlist.size();
    }
    
    void dialogPdfOrJpg(Sheetmusic s){
        String[] choices = new String[] {context.getResources().getString(R.string.pdf), context.getResources().getString(R.string.image_jpg_png)};
        int[] chosen = {-1};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.dialog_title_show_jpg_or_pdf);
        builder.setSingleChoiceItems(choices, chosen[0], (dialog, which) -> {
            chosen[0] = which;
            if(which == 0) showPdfs(s);
            else if (which == 1) showJpgs(s);
            dialog.dismiss();
        });
        builder.setNegativeButton(context.getResources().getString(R.string.cancel), (dialogInterface, i) -> {});
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    void showPdfs(Sheetmusic s){
        ArrayList<String> pdfs = getFilesFromSheetmusicFiles(s, true);
        Intent intent = new Intent(context, HandleFilesActivity.class);
        intent.putExtra("modify", false);
        intent.putExtra("type", "pdf");
        intent.putExtra("pdfs", pdfs);
        intent.putExtra("sheetmusic", s);
        activity.startActivityForResult(intent, 5);
    }

    void showJpgs(Sheetmusic s){
        ArrayList<String> jpgs = getFilesFromSheetmusicFiles(s, false);
        Intent intent = new Intent(context, HandleFilesActivity.class);
        intent.putExtra("modify", false);
        intent.putExtra("type", "jpg");
        intent.putExtra("jpgs", jpgs);
        intent.putExtra("sheetmusic", s);
        activity.startActivityForResult(intent, 6);
    }

    ArrayList<String> getFilesFromSheetmusicFiles(Sheetmusic s, boolean isPDFType){
        ArrayList<String> result = new ArrayList<>();

        for (int i = 0; i < s.getFiles().size(); i++) {
            String path = s.getFiles().get(i);
            String extension = path.substring(path.lastIndexOf(".") +1);

            if(extension.equals("pdf") && isPDFType) result.add(path);
            else if(!extension.equals("pdf") && !isPDFType) result.add(path);
        }
        return result;
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
            sheetmusicOfSetlist.clear();
            sheetmusicOfSetlist.addAll((ArrayList)filterResults.values);
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
            sheetmusicOfSetlist.clear();
            sheetmusicOfSetlist.addAll((ArrayList)filterResults.values);
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
            sheetmusicOfSetlist.clear();
            sheetmusicOfSetlist.addAll((ArrayList)filterResults.values);
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

    public class ViewHolderSheetmusic extends RecyclerView.ViewHolder {

        TextView sheetmusic_name_text, sheetmusic_author_text;
        ImageButton sheetmusic_edit_button;
        ImageButton sheetmusic_favorite_button;
        LinearLayout sheetmusicLayout;

        public ViewHolderSheetmusic(@NonNull View itemView) {
            super(itemView);
            sheetmusic_name_text = itemView.findViewById(R.id.sheetmusic_name_txt);
            sheetmusic_author_text = itemView.findViewById(R.id.sheetmusic_author_txt);
            sheetmusic_edit_button = itemView.findViewById(R.id.sheetmusic_edit_button);
            sheetmusic_favorite_button = itemView.findViewById(R.id.sheetmusic_favorite_button);
            sheetmusicLayout = itemView.findViewById(R.id.sheetmusicLayout);
        }
    }
}