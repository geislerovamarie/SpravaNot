package com.example.spravanot.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.method.ScrollingMovementMethod;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spravanot.R;
import com.example.spravanot.models.Setlist;
import com.example.spravanot.models.Sheetmusic;
import com.example.spravanot.utils.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AddSetlistActivity extends AppCompatActivity {

    ActivityResultLauncher<Intent> activityResultLaunch;
    EditText name_text, notes_text;
    TextView tags_text;
    ImageButton edit_tag;
    FloatingActionButton save;

    ArrayList<String> tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_setlist);

        setUpLauncher();
        init();
        setOnClickListeners();
    }

    void setUpLauncher(){
        activityResultLaunch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == 8) {   //tags
                if(result.getData() != null){
                    tags = result.getData().getStringArrayListExtra("tags");
                    String tagString = tagsToString();
                    tags_text.setText(tagString);
                }
            }
        });
    }

    void init(){
        setTitle(getResources().getString(R.string.title_add_setlist));
        tags = new ArrayList<>();
        name_text = findViewById(R.id.add_setlist_name_answer);
        notes_text = findViewById(R.id.add_setlist_notes_answer);
        tags_text = findViewById(R.id.add_setlist_tags_answer);
        edit_tag = findViewById(R.id.add_setlist_edit_tags_button);
        save = findViewById(R.id.add_setlist_add_button);
        tags_text.setMovementMethod(new ScrollingMovementMethod());
    }

    void setOnClickListeners(){
        edit_tag.setOnClickListener(view -> {
            Intent intent = new Intent(AddSetlistActivity.this, EditTagsActivity.class);
            intent.putExtra("tags", tags);
            activityResultLaunch.launch(intent);
        });

        save.setOnClickListener(view -> {
            DatabaseHelper db = new DatabaseHelper(AddSetlistActivity.this);
            Setlist s = new Setlist(-1);

            // make sure name is not null
            String name = name_text.getText().toString().trim();
            if(name.length() <= 0){
                Toast.makeText(AddSetlistActivity.this, R.string.dialog_name_cannot_be_null, Toast.LENGTH_SHORT).show();
                return;
            }
            s.setName(setToNullIfEmpty(name));
            s.setNotes(setToNullIfEmpty(notes_text.getText().toString().trim()));
            s.setTags(tags);

            db.addSetlist(s);

            //end
            Intent intent = new Intent();
            setResult(3, intent);
            finish();
        });
    }

    // empty string in SQLite database is not null
    String setToNullIfEmpty(String s){
        if(s.length() > 0) return s;
        return null;
    }

    String tagsToString(){
        String stringOfTags = "";
        for (int i = 0; i < tags.size(); i++) {
            stringOfTags = stringOfTags.concat(tags.get(i) + "\n");
        }
        return stringOfTags;
    }
}
