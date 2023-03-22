package com.example.spravanot;

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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;

public class SheetmusicAdapter extends RecyclerView.Adapter<SheetmusicAdapter.ViewHolderSheetmusic> {

    private Context context;
    Activity activity;
    //Animation translate_anim;

    private ArrayList sheetmusic_name, sheetmusic_author;

    public SheetmusicAdapter(Activity activity, Context context, ArrayList sheetmusic_name, ArrayList sheetmusic_author) {
        this.activity = activity;
        this.context = context;
        this.sheetmusic_name = sheetmusic_name;
        this.sheetmusic_author = sheetmusic_author;
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
        String sh_name = String.valueOf(sheetmusic_name.get(position));
        String sh_author = String.valueOf(sheetmusic_author.get(position)) == "null" ? context.getResources().getString(R.string.text_unknown) : String.valueOf(sheetmusic_author.get(position));

        holder.sheetmusic_name_text.setText(sh_name);
        holder.sheetmusic_author_text.setText(sh_author);
        
        holder.sheetmusic_edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Edit button pressed", Toast.LENGTH_SHORT).show();
            }
        });

        holder.sheetmusic_favorite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "FAVORITE", Toast.LENGTH_SHORT).show();
            }
        });

   /*     TODO
        holder.sheetmusicLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TODO open sheets activity)
            }
        });
*/
    }

    @Override
    public int getItemCount() {
        return sheetmusic_name.size();
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