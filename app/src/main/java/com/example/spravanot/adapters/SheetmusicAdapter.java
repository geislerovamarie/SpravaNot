package com.example.spravanot.adapters;

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
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spravanot.interfaces.PassInfoSheetmusic;
import com.example.spravanot.R;
import com.example.spravanot.models.Setlist;
import com.example.spravanot.models.Sheetmusic;
import com.example.spravanot.activities.EditSheetmusicActivity;
import com.example.spravanot.activities.HandleFilesActivity;
import com.google.android.material.chip.Chip;

import java.io.Serializable;
import java.util.ArrayList;

public class SheetmusicAdapter extends RecyclerView.Adapter<SheetmusicAdapter.ViewHolderSheetmusic> {

    private Context context;
    Activity activity;

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

        // get name and author
        String sh_name = String.valueOf(sheetmusic.get(position).getName());
        String sh_author = String.valueOf(sheetmusic.get(position).getAuthor()).equals("null") ? context.getResources().getString(R.string.text_unknown) : String.valueOf(sheetmusic.get(position).getAuthor());

        // Set text
        holder.sheetmusic_name_text.setText(sh_name);
        holder.sheetmusic_author_text.setText(sh_author);

        // Edit button
        holder.sheetmusic_edit_button.setOnClickListener(view -> {
            info.updateSheetmusic(holder.getAdapterPosition());
        });

        // Add to favorites button
        holder.sheetmusic_favorite_button.setOnClickListener(view -> info.toggleFavorite(holder.getAdapterPosition(), sheetmusic.get(holder.getAdapterPosition()).getId()));

        // Click and show sheetmusic
        holder.sheetmusicLayout.setOnClickListener(view -> {
            Sheetmusic clicked = sheetmusic.get(holder.getAdapterPosition());
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
    }

    @Override
    public int getItemCount() {
        return sheetmusic.size();
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

    // viewholder  and interface --------------------------------

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
        }
    }

}