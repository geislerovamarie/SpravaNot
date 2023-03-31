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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spravanot.utils.DatabaseHelper;
import com.example.spravanot.R;
import com.example.spravanot.models.Sheetmusic;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;

public class AddSheetmusicActivity extends AppCompatActivity {

    ActivityResultLauncher<Intent> activityResultLaunch;
    ActivityResultLauncher<Intent> activityMp3ResultLaunch;
    EditText name_text, author_text, genre_text, key_text, instrument_text, notes_text;
    TextView jpg_text, pdf_text, mp3_text, tags_text;
    ImageButton edit_pdf, edit_jpg, edit_mp3;
    ImageButton add_tag, remove_tag;
    FloatingActionButton save;

    ArrayList<String> files;
    ArrayList<String> tags;

    ArrayList<String> pdfs;
    ArrayList<String> jpgs; // and actually also pngs
    String mp3_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sheetmusic);

        // Launcher - when child activity returns either with pdfs or jpgs
        activityResultLaunch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == 5) {      //pdf
                if(result.getData() != null){
                    pdfs = result.getData().getStringArrayListExtra("pdfs");
                }
            } else if (result.getResultCode() == 6) {   //jpg
                if(result.getData() != null){
                    jpgs = result.getData().getStringArrayListExtra("jpgs");
                }
            }
        });

        // Launcher for mp3
        activityMp3ResultLaunch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {      //pdf
                Intent data = result.getData();
                Uri uri = data.getData();
                String address = uri.toString();
                mp3_address = address;


                Cursor help = getContentResolver().query(uri, null, null, null, null);
                int nameidx = help.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                help.moveToNext();
                mp3_text.setText(help.getString(nameidx));
            }
        });

        // init - for AddSheetmusic files and tags are empty, for EditSheetmusic it have load from db
        files = new ArrayList<>();
        tags = new ArrayList<>();
        pdfs = new ArrayList<>();
        jpgs = new ArrayList<>();

        /*
        * !!
        * For Edit
        * 0) get data (id, name,...), set known text
        * 1) load files - by existing ID - from database sheetmusic_file selectfilesforsheetmusic
        * 2) convert ids to addresses via file table in database
        * 3) store in files
        * 4) separata pdf or else (images - pdfs/jpgs) by extensions
        *
        * ...
        * x) put together pdfs and jpgs to files and put it to the db
        *
        *
        * -----------------
        * FOR DATABASE:
        * - LOAD FROM DB TO "LOCAL OBJECTS"
        * - EDIT LOCAL OBJECTS
        * - DELETE EVERYTHING FROM DB (UPDATE!!!!!)
        * - STORE LOCAL IN DB
        * */

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
        edit_pdf.setOnClickListener(view -> {
            Intent intent = new Intent(AddSheetmusicActivity.this, HandleFilesActivity.class);
            intent.putExtra("modify", true);
            intent.putExtra("pdfs", pdfs);
            intent.putExtra("type", "pdf");
            // cant sent sheetmusic, edit will be able to, but here its null
            activityResultLaunch.launch(intent);
        });

        edit_jpg = findViewById(R.id.add_sheetmusic_edit_jpg_button);
        edit_jpg.setOnClickListener(view -> {
            Intent intent = new Intent(AddSheetmusicActivity.this, HandleFilesActivity.class);
            intent.putExtra("modify", true);
            intent.putExtra("jpgs", jpgs);
            intent.putExtra("type", "jpg");
            // cant sent sheetmusic, edit will be able to, but here its null
            activityResultLaunch.launch(intent);
        });

        edit_mp3 = findViewById(R.id.add_sheetmusic_edit_mp3_button);
        edit_mp3.setOnClickListener(view -> {
            Toast.makeText(AddSheetmusicActivity.this, "edit mp3", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("audio/mpeg");
            activityMp3ResultLaunch.launch(intent);
        });

        add_tag = findViewById(R.id.add_sheetmusic_edit_tags_button);
        add_tag.setOnClickListener(view -> {
            Toast.makeText(AddSheetmusicActivity.this, "edit tag", Toast.LENGTH_SHORT).show();
            // startactivityonresult -> store result in tags, not in database, it would have to be saved first
        });

        save = findViewById(R.id.add_sheetmusic_add_button);
        save.setOnClickListener(view -> {
            // ALSO WILL BE DIFFERENT IN EDIT - for example id will be known
            // combine jpgs and pdfs into files
            pdfs.addAll(jpgs);
            files = pdfs;

            DatabaseHelper db = new DatabaseHelper(AddSheetmusicActivity.this);

            // Store input in sheetmusic object and add to database and pass to sheets fragment
            Sheetmusic s = new Sheetmusic(-1);
            // make sure name is not null
            String name = name_text.getText().toString().trim();
            if(name.length() <= 0){
                Toast.makeText(AddSheetmusicActivity.this, R.string.dialog_name_cannot_be_null, Toast.LENGTH_SHORT).show();
                return;
            }

            s.setName(setToNullIfEmpty(name));
            s.setAuthor(setToNullIfEmpty(author_text.getText().toString().trim()));
            s.setGenre(setToNullIfEmpty(genre_text.getText().toString().trim()));
            s.setKey(setToNullIfEmpty(key_text.getText().toString().trim()));
            s.setInstrument(setToNullIfEmpty(instrument_text.getText().toString().trim()));
            s.setNotes(setToNullIfEmpty(notes_text.getText().toString().trim()));
            s.setMp3(mp3_address);
            s.setFiles(files);
            s.setTags(tags);

            int id = db.addSheetmusic(s);
            s.setId(id);

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
}