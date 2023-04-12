package com.example.spravanot.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.spravanot.R;

import java.util.ArrayList;
import java.util.Objects;

public class JpgImageAdapter extends PagerAdapter {

    ArrayList<String> addresses;
    Context context;
    LayoutInflater layoutInflater;

    public JpgImageAdapter(Context context, ArrayList<String> addresses) {
        this.addresses = addresses;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return addresses.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = layoutInflater.inflate(R.layout.imageview_open_jpg, container, false);
        ImageView imageView = itemView.findViewById(R.id.open_jpg_imageview);

        String path = addresses.get(position);
        Uri uri = Uri.parse(path);
        imageView.setImageURI(uri);

        Objects.requireNonNull(container).addView(itemView);

        // store as last opened in shared preferences
        SharedPreferences sharedPref = context.getSharedPreferences("LastFilePref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("last_opened", path);
        editor.apply();

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}
