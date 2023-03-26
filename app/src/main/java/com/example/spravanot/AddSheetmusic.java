package com.example.spravanot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddSheetmusic extends AppCompatActivity {

    EditText name_text, author_text, genre_text, key_text, instrument_text, notes_text;
    FloatingActionButton save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sheetmusic);

        name_text = findViewById(R.id.add_sheetmusic_name_answer);
        author_text = findViewById(R.id.add_sheetmusic_author_answer);
        genre_text = findViewById(R.id.add_sheetmusic_genre_answer);
        key_text = findViewById(R.id.add_sheetmusic_key_answer);
        instrument_text = findViewById(R.id.add_sheetmusic_instrument_answer);
        notes_text = findViewById(R.id.add_sheetmusic_notes_answer);

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

                // add files to s

                // add tags to s

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