package com.example.spravanot.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.opengl.Visibility;
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
import com.example.spravanot.interfaces.PassInfoSheetmusic;
import com.example.spravanot.models.Sheetmusic;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AddSheetmusicToSetlistAdapter extends RecyclerView.Adapter<AddSheetmusicToSetlistAdapter.ViewHolderSheetmusicToSetlist> {

    private Context context;
    Activity activity;

    private ArrayList<Sheetmusic> sheetmusic;
    private ArrayList<Boolean> favorite;
    private PassInfoSheetmusic info;
    private boolean showingSetlistFavorite;
    private ArrayList<Sheetmusic> selected;

    public AddSheetmusicToSetlistAdapter(Activity activity, Context context, ArrayList sheetmusic, PassInfoSheetmusic info, ArrayList favorite, boolean showingSetlistFavorite, ArrayList<Sheetmusic> alreadyStoredSheets) {
        this.activity = activity;
        this.context = context;
        this.sheetmusic = sheetmusic;
        this.info = info;
        this.favorite = favorite;
        selected = alreadyStoredSheets;
        this.showingSetlistFavorite = showingSetlistFavorite;
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
        Boolean isFav = favorite.get(position);

        // Set text and color
        holder.sheetmusic_name_text.setText(sh_name);
        holder.sheetmusic_author_text.setText(sh_author);

        Sheetmusic click = sheetmusic.get(position);
        if(setlistContains(selected, click)) holder.sheetmusicLayout.setBackgroundResource(R.color.teal_200);
        else holder.sheetmusicLayout.setBackgroundResource(androidx.cardview.R.color.cardview_light_background);


        // Click and mark sheetmusic
        holder.sheetmusicLayout.setOnClickListener(view -> {
            Sheetmusic clicked = sheetmusic.get(holder.getAdapterPosition());
            if(setlistContains(selected, clicked)){
                selected.remove(clicked);
                holder.sheetmusicLayout.setBackgroundResource(androidx.cardview.R.color.cardview_light_background);
                info.addSheetmusicToSetlist(clicked, false);
            }else{
                selected.add(clicked);
                holder.sheetmusicLayout.setBackgroundResource(R.color.teal_200);
                info.addSheetmusicToSetlist(clicked, true);
            }
        });
/*
        // long hold -> delete
        holder.sheetmusicLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // delete dialog
                AlertDialog.Builder dBuilder = new AlertDialog.Builder(context);
                dBuilder.setMessage(R.string.dialog_delete_item);
                dBuilder.setTitle(R.string.dialog_title_delete_item);

                // delete item
                dBuilder.setPositiveButton(R.string.yes, (dialogInterface, i) -> info.deleteSheetmusic(holder.getAdapterPosition(), sheetmusic.get(holder.getAdapterPosition()).getId()));

                // cancel
                dBuilder.setNegativeButton(R.string.no, (dialogInterface, i) -> dialogInterface.cancel());

                // show the dialog
                AlertDialog dialog = dBuilder.create();
                dialog.show();
                return true;
            }
        });

 */
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

    // viewholder  and interface --------------------------------

    public class ViewHolderSheetmusicToSetlist extends RecyclerView.ViewHolder {

        TextView sheetmusic_name_text, sheetmusic_author_text;
        ImageButton sheetmusic_edit_button;
        ImageButton sheetmusic_favorite_button;
        LinearLayout sheetmusicLayout;

        public ViewHolderSheetmusicToSetlist(@NonNull View itemView) {
            super(itemView);
            sheetmusic_name_text = itemView.findViewById(R.id.sheetmusic_name_txt);
            sheetmusic_author_text = itemView.findViewById(R.id.sheetmusic_author_txt);
            sheetmusic_edit_button = itemView.findViewById(R.id.sheetmusic_edit_button);
            sheetmusic_favorite_button = itemView.findViewById(R.id.sheetmusic_favorite_button);
            sheetmusicLayout = itemView.findViewById(R.id.sheetmusicLayout);

            sheetmusic_edit_button.setVisibility(View.GONE);
            sheetmusic_favorite_button.setVisibility(View.GONE);
        }
    }
}