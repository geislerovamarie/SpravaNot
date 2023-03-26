package com.example.spravanot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;

public class SheetmusicAdapter extends RecyclerView.Adapter<SheetmusicAdapter.ViewHolderSheetmusic> {

    private Context context;
    Activity activity;
    //Animation translate_anim;

    private ArrayList<Sheetmusic> sheetmusic;
    private PassInfoSheetmusic info;

    public SheetmusicAdapter(Activity activity, Context context, ArrayList sheetmusic, PassInfoSheetmusic info) {
        this.activity = activity;
        this.context = context;
        this.sheetmusic = sheetmusic;
        this.info = info;
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
        String sh_name = String.valueOf(sheetmusic.get(position).getName());
        String sh_author = String.valueOf(sheetmusic.get(position).getAuthor()) == "null" ? context.getResources().getString(R.string.text_unknown) : String.valueOf(sheetmusic.get(position).getAuthor());

        // Set text
        holder.sheetmusic_name_text.setText(sh_name);
        holder.sheetmusic_author_text.setText(sh_author);

        // Edit button - show menu with options "edit" and "delete"
        holder.sheetmusic_edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMenu(view, holder);
            }
        });

        // Add to favorites button
        holder.sheetmusic_favorite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                info.toggleFavorite(holder.getAdapterPosition(), sheetmusic.get(holder.getAdapterPosition()).getId());
            }
        });

        // Click and show sheetmusic
        holder.sheetmusicLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShowSheetmusic.class);
                intent.putExtra("sheetmusic", sheetmusic.get(holder.getAdapterPosition()));
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sheetmusic.size();
    }

    // --------------------------------------------------------------------------------------------

    // menu ----------------------------
    public void openMenu(View view, ViewHolderSheetmusic holder){
        PopupMenu popupMenu = new PopupMenu(context, view);
        // set up the listener
        PopupMenu.OnMenuItemClickListener listener = new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.sheetmusic_edit_option:
                        Intent intent = new Intent(context, EditSheetmusic.class);
                        intent.putExtra("sheetmusic", sheetmusic.get(holder.getAdapterPosition()));
                        activity.startActivityForResult(intent, 1);
                        return true;
                    case R.id.sheetmusic_delete_option:
                        // ask wheter really delete
                        AlertDialog.Builder dBuilder = new AlertDialog.Builder(context);
                        dBuilder.setMessage(R.string.dialog_delete_item);
                        dBuilder.setTitle(R.string.dialog_title_delete_item);

                        // delete item
                        dBuilder.setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                            info.deleteSheetmusic(holder.getAdapterPosition(), sheetmusic.get(holder.getAdapterPosition()).getId());
                        });

                        // cancel
                        dBuilder.setNegativeButton(R.string.no, (dialogInterface, i) -> {
                            dialogInterface.cancel();
                        });

                        // show the dialog
                        AlertDialog dialog = dBuilder.create();
                        dialog.show();

                        return true;
                    default:
                        return false;
                }
            }
        };
        // prepare and show the menu
        popupMenu.setOnMenuItemClickListener(listener);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.sheetmusic_edit_menu, popupMenu.getMenu());
        popupMenu.show();
    }

    // viewholder --------------------------------

    public class ViewHolderSheetmusic extends RecyclerView.ViewHolder {

        TextView sheetmusic_name_text, sheetmusic_author_text;
        ImageButton sheetmusic_edit_button;
        Chip sheetmusic_favorite_button;
        LinearLayout sheetmusicLayout;

        public ViewHolderSheetmusic(@NonNull View itemView) {
            super(itemView);
            sheetmusic_name_text = itemView.findViewById(R.id.sheetmusic_name_txt);
            sheetmusic_author_text = itemView.findViewById(R.id.sheetmusic_author_txt);
            sheetmusic_edit_button = itemView.findViewById(R.id.sheetmusic_edit_button);
            sheetmusic_favorite_button = itemView.findViewById(R.id.sheetmusic_favorite_chip);
            sheetmusicLayout = itemView.findViewById(R.id.sheetmusicLayout);

            // animation todo?
            //translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            //sheetmusicLayout.setAnimation(translate_anim);
        }
    }
}