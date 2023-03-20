package com.example.spravanot;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

// tone == key in music

public class DatabaseHelper extends SQLiteOpenHelper {

     private Context context;
    private static final String DATABASE_NAME = "Noty.db";
    private static final int DATABASE_VERSION = 1;

    // table names
    private static final String TABLE_SHEETMUSIC = "sheetmusic";
    private static final String TABLE_FILE = "file";
    private static final String TABLE_SETLIST = "setlist";
    private static final String TABLE_TAG = "tag";
    private static final String TABLE_SHEETMUSIC_FILE = "sheetmusic_file";
    private static final String TABLE_SHEETMUSIC_SETLIST = "sheetmusic_setlist";
    private static final String TABLE_SHEETMUSIC_TAG = "sheetmusic_tag";
    private static final String TABLE_SETLIST_TAG = "setlist_tag";

    // create table statements
    private static final String CREATE_TABLE_SHEETMUSIC = "CREATE TABLE " + TABLE_SHEETMUSIC + " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, author TEXT, genre TEXT, tone TEXT, instrument TEXT, mp3 TEXT, notes TEXT);";
    private static final String CREATE_TABLE_FILE = "CREATE TABLE " + TABLE_FILE + " (id INTEGER PRIMARY KEY AUTOINCREMENT, address TEXT NOT NULL);";
    private static final String CREATE_TABLE_SETLIST = "CREATE TABLE " + TABLE_SETLIST + " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, notes TEXT);";
    private static final String CREATE_TABLE_TAG = "CREATE TABLE " + TABLE_TAG + " (name TEXT PRIMARY KEY);";
    private static final String CREATE_TABLE_SHEETMUSIC_FILE = "CREATE TABLE " + TABLE_SHEETMUSIC_FILE + " (id_sheetmusic INTEGER NOT NULL, id_file INTEGER NOT NULL, PRIMARY KEY (id_sheetmusic, id_file), CONSTRAINT sheetmusic_file_fk_id_sheetmusic FOREIGN KEY (id_sheetmusic) REFERENCES sheetmusic (id) ON DELETE CASCADE ON UPDATE CASCADE, CONSTRAINT sheetmusic_file_fk_id_file FOREIGN KEY (id_file) REFERENCES file (id) ON DELETE CASCADE ON UPDATE CASCADE);";
    private static final String CREATE_TABLE_SHEETMUSIC_SETLIST = "CREATE TABLE " + TABLE_SHEETMUSIC_SETLIST + " (id_sheetmusic INTEGER NOT NULL, id_setlist INTEGER NOT NULL, PRIMARY KEY (id_sheetmusic, id_setlist), CONSTRAINT sheetmusic_setlist_fk_id_sheetmusic FOREIGN KEY (id_sheetmusic) REFERENCES sheetmusic (id) ON DELETE CASCADE ON UPDATE CASCADE, CONSTRAINT sheetmusic_setlist_fk_id_setlist FOREIGN KEY (id_setlist) REFERENCES setlist (id) ON DELETE CASCADE ON UPDATE CASCADE);";
    private static final String CREATE_TABLE_SHEETMUSIC_TAG = "CREATE TABLE " + TABLE_SHEETMUSIC_TAG + " (id_sheetmusic INTEGER NOT NULL, name_tag TEXT NOT NULL, PRIMARY KEY (id_sheetmusic, name_tag), CONSTRAINT sheetmusic_tag_fk_id_sheetmusic FOREIGN KEY (id_sheetmusic) REFERENCES sheetmusic (id) ON DELETE CASCADE ON UPDATE CASCADE, CONSTRAINT sheetmusic_tag_fk_name_tag FOREIGN KEY (name_tag) REFERENCES tag (name) ON DELETE CASCADE ON UPDATE CASCADE);";
    private static final String CREATE_TABLE_SETLIST_TAG = "CREATE TABLE " + TABLE_SETLIST_TAG + " (id_setlist INTEGER NOT NULL, name_tag TEXT NOT NULL, PRIMARY KEY (id_setlist, name_tag), CONSTRAINT setlist_tag_fk_id_setlist FOREIGN KEY (id_setlist) REFERENCES setlist (id) ON DELETE CASCADE ON UPDATE CASCADE, CONSTRAINT setlist_tag_fk_name_tag FOREIGN KEY (name_tag) REFERENCES tag (name) ON DELETE CASCADE ON UPDATE CASCADE);";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SHEETMUSIC);
        db.execSQL(CREATE_TABLE_FILE);
        db.execSQL(CREATE_TABLE_SETLIST);
        db.execSQL(CREATE_TABLE_TAG);
        db.execSQL(CREATE_TABLE_SHEETMUSIC_FILE);
        db.execSQL(CREATE_TABLE_SHEETMUSIC_SETLIST);
        db.execSQL(CREATE_TABLE_SHEETMUSIC_TAG);
        db.execSQL(CREATE_TABLE_SETLIST_TAG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHEETMUSIC + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILE + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETLIST + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAG + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHEETMUSIC_FILE + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHEETMUSIC_SETLIST + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHEETMUSIC_TAG + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETLIST_TAG + ";");
        onCreate(db);
    }

    // Adding ------------------------------------------------------------------
    void addSheetmusic(Sheetmusic s){
        SQLiteDatabase db = this.getWritableDatabase();

        // sheetmusic table
        ContentValues cvSh = new ContentValues();
        cvSh.put("name", s.getName());
        cvSh.put("author", s.getAuthor());
        cvSh.put("genre", s.getGenre());
        cvSh.put("key", s.getKey());
        cvSh.put("instrument", s.getInstument());
        cvSh.put("mp3", s.getMp3());
        cvSh.put("notes", s.getNotes());
        long idSheetmusic = db.insert(TABLE_SHEETMUSIC, null, cvSh);
        s.setId((int) idSheetmusic);

        // files + sheetmusic_files table
        //addFilesToSheetmusic(s);

        // tags + sheetmusic_tag table
        //addTagsToSheetmusic(s);
    }

    void addFilesToSheetmusic(Sheetmusic s){
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < s.getFiles().size(); i++) {
            ContentValues cvF = new ContentValues();
            cvF.put("address", s.getFiles().get(i));
            long idFile = db.insert(TABLE_FILE, null, cvF);

            ContentValues cvSh_F = new ContentValues();
            cvSh_F.put("id_sheetmusic",s.getId());
            cvSh_F.put("id_file",idFile);
            db.insert(TABLE_SHEETMUSIC_FILE, null, cvSh_F);
        }
    }

    void addTagsToSheetmusic(Sheetmusic s){
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < s.getTags().size(); i++) {
            ContentValues cvT = new ContentValues();
            String nameTag = s.getTags().get(i);
            cvT.put("name", nameTag);
            db.insert(TABLE_TAG, null, cvT);

            ContentValues cvSh_T = new ContentValues();
            cvSh_T.put("id_sheetmusic",s.getId());
            cvSh_T.put("name_tag", nameTag);
            db.insert(TABLE_SHEETMUSIC_TAG, null, cvSh_T);
        }
    }

    void addSetlist(Setlist s){
        SQLiteDatabase db = this.getWritableDatabase();

        // setlist table
        ContentValues cvS = new ContentValues();
        cvS.put("name", s.getName());
        cvS.put("notes", s.getNotes());
        long idSetlist = db.insert(TABLE_SETLIST, null, cvS);
        s.setId((int) idSetlist);

        // tags + setlist_tag table
        //addTagsToSetlist(s);
    }

    void addTagsToSetlist(Setlist s){
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < s.getTags().size(); i++) {
            ContentValues cvT = new ContentValues();
            String nameTag = s.getTags().get(i);
            cvT.put("name", nameTag);
            db.insert(TABLE_TAG, null, cvT);

            ContentValues cvSe_T = new ContentValues();
            cvSe_T.put("id_setlist",s.getId());
            cvSe_T.put("name_tag", nameTag);
            db.insert(TABLE_SETLIST_TAG, null, cvSe_T);
        }
    }

    void addToSetlist(int idSetlist, ArrayList<Sheetmusic> sheetmusic){
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < sheetmusic.size(); i++) {
            ContentValues cvS = new ContentValues();
            cvS.put("id_sheetmusic", sheetmusic.get(i).getId());
            cvS.put("id_setlist", idSetlist);
            db.insert(TABLE_SHEETMUSIC_SETLIST, null, cvS);
        }
    }

    // Update -------------------------------------------------------------------
    void updateSheetmusic(Sheetmusic s){
        SQLiteDatabase db = this.getWritableDatabase();

        // sheetmusic table
        ContentValues cvSh = new ContentValues();
        cvSh.put("name", s.getName());
        cvSh.put("author", s.getAuthor());
        cvSh.put("genre", s.getGenre());
        cvSh.put("key", s.getKey());
        cvSh.put("instrument", s.getInstument());
        cvSh.put("mp3", s.getMp3());
        cvSh.put("notes", s.getNotes());
        long shResult = db.update(TABLE_SHEETMUSIC, cvSh, "id = ?", new String[]{String.valueOf(s.getId())});

        // Files can be only added or deleted
        // Tags can be only added or deleted
    }


    void updateSetlist(Setlist s){
        SQLiteDatabase db = this.getWritableDatabase();

        // setlist table
        ContentValues cvS = new ContentValues();
        cvS.put("name", s.getName());
        cvS.put("notes", s.getNotes());
        long seResult = db.update(TABLE_SETLIST, cvS, "id = ?", new String[]{String.valueOf(s.getId())});

        // Tags can be only added or deleted
    }

    // Showing ------------------------------------------------------------------
    void selectAllSheetmusic(){
        //select
    }

    void selectAllSetlists(){
        // select
    }

    // Deleting -----------------------------------------------------------------
    void deleteOneSheetmusic(){

    }

    void deleteOneSetlist(){

    }

    void deleteOneSheetmusicFromSetlist(){
        // only sever connection
    }

    void deleteFileFromSheetmusic(){
        // only sever connection
    }

    void deleteTagFromSheetmusic(){
        // only sever connection
    }

    void deleteTagFromSetlist(){
        // only sever connection

    }

}
