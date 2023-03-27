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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class HandleFilesAdapter extends RecyclerView.Adapter<HandleFilesAdapter.ViewHolderFiles> {

    Context context;
    Activity activity;
    private PassInfoSheetmusic info;

    private ArrayList<String> addresses;
    //private ArrayList<String> names;

    public HandleFilesAdapter(Context context, Activity activity, ArrayList<String> addresses, PassInfoSheetmusic info) {
        this.context = context;
        this.activity = activity;
        this.addresses = addresses;
        this.info = info;
    }

    @NonNull
    @Override
    public ViewHolderFiles onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.files_row, parent, false);
        return new ViewHolderFiles(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderFiles holder, int position) {
        String address = String.valueOf(addresses.get(position));
        String name = address.substring(address.lastIndexOf(File.separator) + 1); // for now!!

        // Set text
        holder.files_name_text.setText(name);
        holder.files_address_text.setText(address);

        // Click file and show
        holder.filesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String path = addresses.get(holder.getAdapterPosition());
                String extension = path.substring(path.lastIndexOf(".") +1);
                if(extension.equals("pdf")) {
                    //Intent intent;
                    // show pdf
                }else{
                    Intent intent;
                    // show jpg
                }
            }

        });
        // Delete
        holder.filesLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AtomicBoolean ret = new AtomicBoolean(false);
                // ask wheter really delete
                AlertDialog.Builder dBuilder = new AlertDialog.Builder(context);
                dBuilder.setMessage(R.string.dialog_delete_item);
                dBuilder.setTitle(R.string.dialog_title_delete_item);

                // delete item
                dBuilder.setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                    //addresses.remove(holder.getAdapterPosition());
                    info.deleteSheetmusic(holder.getAdapterPosition(),-1);
                    ret.set(true);
                });

                // cancel
                dBuilder.setNegativeButton(R.string.no, (dialogInterface, i) -> {
                    dialogInterface.cancel();
                });

                // show the dialog
                AlertDialog dialog = dBuilder.create();
                dialog.show();
                return ret.get();
            }
        });
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }

    public class ViewHolderFiles extends RecyclerView.ViewHolder {

        TextView files_name_text, files_address_text;
        ImageButton files_icon;
        LinearLayout filesLayout;

        public ViewHolderFiles(@NonNull View itemView) {
            super(itemView);
            files_name_text = itemView.findViewById(R.id.files_row_name);
            files_address_text = itemView.findViewById(R.id.files_row_address);
            filesLayout = itemView.findViewById(R.id.filesLayout);
        }
    }
}
