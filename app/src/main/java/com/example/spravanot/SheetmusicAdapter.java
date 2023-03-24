package com.example.spravanot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;

public class SheetmusicAdapter extends RecyclerView.Adapter<SheetmusicAdapter.ViewHolderSheetmusic> {

    private Context context;
    Activity activity;
    //Animation translate_anim;

    private ArrayList<Sheetmusic> sheetmusic;

    public SheetmusicAdapter(Activity activity, Context context, ArrayList sheetmusic) {
        this.activity = activity;
        this.context = context;
        this.sheetmusic = sheetmusic;
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

        // Edit button
        holder.sheetmusic_edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditSheetmusic.class);
                intent.putExtra("sheetmusic", sheetmusic.get(holder.getAdapterPosition()));
                activity.startActivityForResult(intent, 1);
            }
        });

        // Add to favorites button
        holder.sheetmusic_favorite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "FAVORITE", Toast.LENGTH_SHORT).show();
                // maybe like this?
                DatabaseHelper db = new DatabaseHelper(context);

                // create setlist "Favorite" if it doesnt exist
                if(!db.doesFavoriteExist()){
                    Setlist f = new Setlist(-1);
                    f.setName("Favorite");
                    f.setNotes("Favorite / Oblíbené");
                    db.getWritableDatabase();
                    db.addSetlist(f);
                }

                // add to favorite
                ArrayList<Sheetmusic> newFaveSheets = new ArrayList<>();
                newFaveSheets.add(sheetmusic.get(holder.getAdapterPosition()));
                db.addToSetlist(0, newFaveSheets);
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