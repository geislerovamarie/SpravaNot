package com.example.spravanot;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class HandleFilesAdapter extends RecyclerView.Adapter<HandleFilesAdapter.ViewHolderFiles> {

    Context context;
    Activity activity;

    private ArrayList<String> addresses;
    //private ArrayList<String> names;

    public HandleFilesAdapter(Context context, Activity activity, ArrayList<String> addresses) {
        this.context = context;
        this.activity = activity;
        this.addresses = addresses;
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
        //String name = String.valueOf(sheetmusic.get(position).getName());
        String address = String.valueOf(addresses.get(position));
        String name = address.substring(address.lastIndexOf(File.separator) + 1); // for now!!

        // Set text
        holder.files_name_text.setText(name);
        holder.files_address_text.setText(address);
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
