package com.example.spravanot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AddSheetmusic extends AppCompatActivity {

    EditText name_text, author_text, genre_text, key_text, instrument_text, notes_text;
    TextView jpg_text, pdf_text, mp3_text, tags_text;
    ImageButton edit_pdf, edit_jpg, edit_mp3;
    ImageButton add_tag, remove_tag;
    FloatingActionButton save;

    ArrayList<String> files;
    ArrayList<String> tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sheetmusic);

        // init - for AddSheetmusic files and tags are empty, for EditSheetmusic it have load from db
        files = new ArrayList<>();
        tags = new ArrayList<>();

        name_text = findViewById(R.id.add_sheetmusic_name_answer);
        author_text = findViewById(R.id.add_sheetmusic_author_answer);
        genre_text = findViewById(R.id.add_sheetmusic_genre_answer);
        key_text = findViewById(R.id.add_sheetmusic_key_answer);
        instrument_text = findViewById(R.id.add_sheetmusic_instrument_answer);
        notes_text = findViewById(R.id.add_sheetmusic_notes_answer);
        jpg_text = findViewById(R.id.add_sheetmusic_jpg_files);
        pdf_text = findViewById(R.id.add_sheetmusic_pdf_files);
        mp3_text = findViewById(R.id.add_sheetmusic_mp3_answer);
        tags_text = findViewById(R.id.add_sheetmusic_tags_answer);

        edit_pdf = findViewById(R.id.add_sheetmusic_edit_pdf_button);
        edit_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AddSheetmusic.this, "edit pdf", Toast.LENGTH_SHORT).show();
                // startactivityonresult or bundle -> store result in files, not in database, it would have to be saved first
            }
        });

        edit_jpg = findViewById(R.id.add_sheetmusic_edit_jpg_button);
        edit_jpg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AddSheetmusic.this, "edit jpg", Toast.LENGTH_SHORT).show();
                // startactivityonresult -> store result in files, not in database, it would have to be saved first
            }
        });

        edit_mp3 = findViewById(R.id.add_sheetmusic_edit_mp3_button);
        edit_mp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AddSheetmusic.this, "edit mp3", Toast.LENGTH_SHORT).show();
                // startactivityonresult -> store result in mp3
            }
        });

        add_tag = findViewById(R.id.add_sheetmusic_add_tags_button);
        add_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AddSheetmusic.this, "add tag", Toast.LENGTH_SHORT).show();
                // startactivityonresult -> store result in tags, not in database, it would have to be saved first
            }
        });

        remove_tag = findViewById(R.id.add_sheetmusic_remove_tags_button);
        remove_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AddSheetmusic.this, "remove tag", Toast.LENGTH_SHORT).show();
                // startactivityonresult -> store result in tags, not in database, it would have to be saved first
            }
        });

        save =findViewById(R.id.add_sheetmusic_add_button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper db = new DatabaseHelper(AddSheetmusic.this);

                // Store input in sheetmusic object and add to database and pass to sheets fragment
                Sheetmusic s = new Sheetmusic(-1);
                s.setName(name_text.getText().toString().trim());
                s.setAuthor(author_text.getText().toString().trim());
                s.setGenre(genre_text.getText().toString().trim());
                s.setKey(key_text.getText().toString().trim());
                s.setInstument(instrument_text.getText().toString().trim());
                s.setNotes(notes_text.getText().toString().trim());
                s.setFiles(files);
                s.setTags(tags);

                int id = db.addSheetmusic(s);
                s.setId(id);

                //end
                Intent intent = new Intent();
                setResult(3, intent);
                finish();
            }
        });
    }
}