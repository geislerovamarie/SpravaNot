package com.example.spravanot.activities;

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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spravanot.R;
import com.example.spravanot.models.Sheetmusic;
import com.example.spravanot.utils.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class EditSheetmusicActivity extends AppCompatActivity {

    ActivityResultLauncher<Intent> activityResultLaunch;
    ActivityResultLauncher<Intent> activityMp3ResultLaunch;
    EditText name_text, author_text, genre_text, key_text, instrument_text, notes_text;
    TextView jpg_text, pdf_text, mp3_text, tags_text;
    ImageButton edit_pdf, edit_jpg, edit_mp3;
    ImageButton edit_tag;
    FloatingActionButton save;

    ArrayList<String> files;
    ArrayList<String> tags;

    ArrayList<String> pdfs;
    ArrayList<String> jpgs; // and actually also pngs
    String mp3_address;

    Sheetmusic sheetmusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sheetmusic);

        setUpLauncher();
        initArrays();
        initGUI();
        getAndSetSheetmusicData();
        setOnClickListeners();
    }

    void getAndSetSheetmusicData(){
        if(getIntent().hasExtra("sheetmusic")){
            sheetmusic = (Sheetmusic) getIntent().getSerializableExtra("sheetmusic");
            if(sheetmusic != null){
                // set local data
                files = sheetmusic.getFiles();
                tags = sheetmusic.getTags();
                mp3_address = sheetmusic.getMp3();
                    // separate files into pdfs and jpgs
                for (int i = 0; i < files.size(); i++) {
                    String path = files.get(i);
                    String extension = path.substring(path.lastIndexOf(".") +1);
                    if(extension.equals("pdf")) pdfs.add(path);
                    else jpgs.add(path);
                }

                // set screen data
                if(sheetmusic.getName() != null) name_text.setText(sheetmusic.getName());
                if(sheetmusic.getAuthor() != null) author_text.setText(sheetmusic.getAuthor());
                if(sheetmusic.getGenre() != null) genre_text.setText(sheetmusic.getGenre());
                if(sheetmusic.getKey() != null) key_text.setText(sheetmusic.getKey());
                if(sheetmusic.getInstrument() != null) instrument_text.setText(sheetmusic.getInstrument());
                if(sheetmusic.getMp3() != null) mp3_text.setText(mp3PathToName(sheetmusic.getMp3()));
                if(sheetmusic.getNotes() != null) notes_text.setText(sheetmusic.getNotes());
                if(sheetmusic.getTags() != null) tags_text.setText(tagsToString());
            }
        }else{
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }

    // empty string in SQLite database is not null
    String setToNullIfEmpty(String s){
        if(s.length() > 0) return s;
        return null;
    }

    void initArrays(){
        files = new ArrayList<>();
        tags = new ArrayList<>();
        pdfs = new ArrayList<>();
        jpgs = new ArrayList<>();
    }

    void setUpLauncher(){
        // Launcher - pdfs, jpgs, tags
        activityResultLaunch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == 5) {      //pdf
                if(result.getData() != null){
                    pdfs = result.getData().getStringArrayListExtra("pdfs");
                }
            } else if (result.getResultCode() == 6) {   //jpg
                if(result.getData() != null){
                    jpgs = result.getData().getStringArrayListExtra("jpgs");
                }
            } else if (result.getResultCode() == 8) {   //tags
                if(result.getData() != null){
                    tags = result.getData().getStringArrayListExtra("tags");
                    String tagString = tagsToString();
                    tags_text.setText(tagString);
                }
            }
        });
        // Launcher for mp3
        activityMp3ResultLaunch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {      //pdf
                Intent data = result.getData();
                Uri uri = data.getData();
                getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                mp3_address = uri.toString();

                String mp3Name = mp3PathToName(mp3_address);
                mp3_text.setText(mp3Name);
            }
        });
    }

    void setOnClickListeners(){
        edit_pdf.setOnClickListener(view -> {
            Intent intent = new Intent(EditSheetmusicActivity.this, HandleFilesActivity.class);
            intent.putExtra("modify", true);
            intent.putExtra("pdfs", pdfs);
            intent.putExtra("type", "pdf");
            intent.putExtra("sheetmusic", sheetmusic);
            activityResultLaunch.launch(intent);
        });

        edit_jpg.setOnClickListener(view -> {
            Intent intent = new Intent(EditSheetmusicActivity.this, HandleFilesActivity.class);
            intent.putExtra("modify", true);
            intent.putExtra("jpgs", jpgs);
            intent.putExtra("type", "jpg");
            // cant sent sheetmusic, edit will be able to, but here its null
            activityResultLaunch.launch(intent);
        });

        edit_mp3.setOnClickListener(view -> {
            Toast.makeText(EditSheetmusicActivity.this, "edit mp3", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);    // TODO CHANGE!!
            intent.setType("audio/mpeg");
            activityMp3ResultLaunch.launch(intent);
        });


        edit_tag.setOnClickListener(view -> {
            Toast.makeText(EditSheetmusicActivity.this, "edit tag", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(EditSheetmusicActivity.this, EditTagsActivity.class);
            intent.putExtra("tags", tags);
            activityResultLaunch.launch(intent);
        });

        save.setOnClickListener(view -> {
            pdfs.addAll(jpgs);
            files = pdfs;

            DatabaseHelper db = new DatabaseHelper(EditSheetmusicActivity.this);

            // Store input in sheetmusic object and add to database and pass to sheets fragment
            Sheetmusic s = new Sheetmusic(sheetmusic.getId());

            // make sure name is not null
            String name = name_text.getText().toString().trim();
            if(name.length() <= 0){
                Toast.makeText(EditSheetmusicActivity.this, R.string.dialog_name_cannot_be_null, Toast.LENGTH_SHORT).show();
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

            db.updateSheetmusic(s);

            //end
            Intent intent = new Intent();
            setResult(2, intent);
            finish();
        });
    }

    void initGUI(){
        setTitle(getResources().getString(R.string.title_edit_sheetmusic));
        name_text = findViewById(R.id.edit_sheetmusic_name_answer);
        author_text = findViewById(R.id.edit_sheetmusic_author_answer);
        genre_text = findViewById(R.id.edit_sheetmusic_genre_answer);
        key_text = findViewById(R.id.edit_sheetmusic_key_answer);
        instrument_text = findViewById(R.id.edit_sheetmusic_instrument_answer);
        notes_text = findViewById(R.id.edit_sheetmusic_notes_answer);
        jpg_text = findViewById(R.id.edit_sheetmusic_jpg_files);
        pdf_text = findViewById(R.id.edit_sheetmusic_pdf_files);
        mp3_text = findViewById(R.id.edit_sheetmusic_mp3_answer);
        tags_text = findViewById(R.id.edit_sheetmusic_tags_answer);
        edit_pdf = findViewById(R.id.edit_sheetmusic_edit_pdf_button);
        edit_jpg = findViewById(R.id.edit_sheetmusic_edit_jpg_button);
        edit_mp3 = findViewById(R.id.edit_sheetmusic_edit_mp3_button);
        edit_tag = findViewById(R.id.edit_sheetmusic_edit_tags_button);
        save = findViewById(R.id.edit_sheetmusic_add_button);
        tags_text.setMovementMethod(new ScrollingMovementMethod());
    }

    String mp3PathToName(String path){
        Cursor cursor = getContentResolver().query(Uri.parse(path), null, null, null, null);
        int nameidx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        cursor.moveToNext();
        String name = cursor.getString(nameidx);
        cursor.close();
        return name;
    }

    String tagsToString(){
        String stringOfTags = "";
        for (int i = 0; i < tags.size(); i++) {
            stringOfTags = stringOfTags.concat(tags.get(i) + "\n");
        }
        return stringOfTags;
    }
}
