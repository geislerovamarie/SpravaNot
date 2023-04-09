package com.example.spravanot.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spravanot.R;
import com.example.spravanot.adapters.EditTagsAdapter;
import com.example.spravanot.adapters.HandleFilesAdapter;
import com.example.spravanot.interfaces.PassInfoSheetmusic;
import com.example.spravanot.models.Sheetmusic;
import com.example.spravanot.utils.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class EditTagsActivity extends AppCompatActivity {

    ActivityResultLauncher<Intent> activityResultLaunch;
    PassInfoSheetmusic info;
    RecyclerView recView;
    EditTagsAdapter tagsAdapter;
    FloatingActionButton addButton;
    FloatingActionButton saveButton;

    ArrayList<String> tags;
   // DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // db = new DatabaseHelper(this);
        setContentView(R.layout.activity_handle_files);
        setTitle(getResources().getString(R.string.text_tags));

        // PassInfo - child activiry adapter tells this activity, which files should be removed
        info = new PassInfoSheetmusic() {
            @Override
            public void deleteSheetmusic(int position, int idSh) {} // not needed here

            @Override
            public void updateSheetmusic(int position) { }

            @Override
            public void toggleFavorite(int position, int idSh) {}   // not needed here

            @Override
            public void deleteTag(int position, String name) {

                tags.remove(position);
                tagsAdapter.notifyItemRemoved(position);
            }

            @Override
            public void addSheetmusicToSetlist(Sheetmusic s, boolean add) {}
        };

        tags = getIntent().getExtras().getStringArrayList("tags");

        addButton = findViewById(R.id.add_files_button);
        addButton.setOnClickListener(view -> addTag());

        saveButton = findViewById(R.id.save_files_button);
        saveButton.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.putExtra("tags", tags);
            setResult(8, intent);
            finish();
        });

        tagsAdapter = new EditTagsAdapter(this, this, tags, info);
        recView = findViewById(R.id.recyclerViewFiles);
        recView.setLayoutManager(new LinearLayoutManager(this));
        recView.setAdapter(tagsAdapter);
    }

    void addTag(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.dialog_title_new_tag));
        View layout = getLayoutInflater().inflate(R.layout.dialog_add_tag, null);
        builder.setView(layout);

        builder.setPositiveButton(getResources().getString(R.string.add), (dialog, which) -> {
            // send data from the AlertDialog to the Activity
            EditText editText = layout.findViewById(R.id.dialog_add_text);
            String new_tag = editText.getText().toString();
            
            if(!tags.contains(new_tag)) {
                tags.add(new_tag);
            }else{
                Toast.makeText(this, R.string.tag_already_present, Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}