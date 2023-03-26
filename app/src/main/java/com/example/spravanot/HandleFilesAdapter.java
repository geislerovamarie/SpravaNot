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

public class HandleFilesAdapter extends RecyclerView.Adapter<HandleFilesAdapter.ViewHolderPdfs> {

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
    public ViewHolderPdfs onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.pdf_row, parent, false);
        return new ViewHolderPdfs(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPdfs holder, int position) {
        //String name = String.valueOf(sheetmusic.get(position).getName());
        String address = String.valueOf(addresses.get(position));
        String name = address.substring(address.lastIndexOf(File.separator) + 1); // for now!!

        // Set text
        holder.pdf_name_text.setText(name);
        holder.pdf_address_text.setText(address);
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }

    public class ViewHolderPdfs extends RecyclerView.ViewHolder {

        TextView pdf_name_text, pdf_address_text;
        ImageButton pdf_icon;
        LinearLayout pdfsLayout;

        public ViewHolderPdfs(@NonNull View itemView) {
            super(itemView);
            pdf_name_text = itemView.findViewById(R.id.pdf_row_name);
            pdf_address_text = itemView.findViewById(R.id.pdf_row_address);
            pdfsLayout = itemView.findViewById(R.id.pdfsLayout);
        }
    }
}
