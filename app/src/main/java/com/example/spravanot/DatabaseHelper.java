package com.example.spravanot;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
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

    // Column names
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_AUTHOR = "author";
    private static final String COL_GENRE = "genre";
    private static final String COL_TONE = "tone";
    private static final String COL_INSTRUMENT = "instrument";
    private static final String COL_MP3 = "mp3";
    private static final String COL_NOTES = "notes";
    private static final String COL_ADDRESS = "address";
    private static final String COL_ID_SHEETMUSIC = "id_sheetmusic";
    private static final String COL_ID_SETLIST = "id_setlist";
    private static final String COL_ID_FILE = "id_file";
    private static final String COL_NAME_TAG = "name_tag";

    // Constraints TODO??


    // create table statements
    private static final String CREATE_TABLE_SHEETMUSIC = "CREATE TABLE " + TABLE_SHEETMUSIC + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NAME + " TEXT NOT NULL, " + COL_AUTHOR + " TEXT, " + COL_GENRE + " TEXT, " + COL_TONE + " TEXT, " + COL_INSTRUMENT + " TEXT, " + COL_MP3 + " TEXT, " + COL_NOTES + " TEXT);";
    private static final String CREATE_TABLE_FILE = "CREATE TABLE " + TABLE_FILE + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_ADDRESS + " TEXT NOT NULL);";
    private static final String CREATE_TABLE_SETLIST = "CREATE TABLE " + TABLE_SETLIST + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NAME + " TEXT NOT NULL, " + COL_NOTES + " TEXT);";
    private static final String CREATE_TABLE_TAG = "CREATE TABLE " + TABLE_TAG + " (" + COL_NAME + " TEXT PRIMARY KEY);";
    private static final String CREATE_TABLE_SHEETMUSIC_FILE = "CREATE TABLE " + TABLE_SHEETMUSIC_FILE + " (" + COL_ID_SHEETMUSIC + " INTEGER NOT NULL, " + COL_ID_FILE + " INTEGER NOT NULL, PRIMARY KEY (" + COL_ID_SHEETMUSIC + ", " + COL_ID_FILE + "), CONSTRAINT sheetmusic_file_fk_id_sheetmusic FOREIGN KEY (" + COL_ID_SHEETMUSIC + ") REFERENCES " + TABLE_SHEETMUSIC + " (" + COL_ID + ") ON DELETE CASCADE ON UPDATE CASCADE, CONSTRAINT sheetmusic_file_fk_id_file FOREIGN KEY (" + COL_ID_FILE + ") REFERENCES " + TABLE_FILE + " (" + COL_ID + ") ON DELETE CASCADE ON UPDATE CASCADE);";
    private static final String CREATE_TABLE_SHEETMUSIC_SETLIST = "CREATE TABLE " + TABLE_SHEETMUSIC_SETLIST + " (" + COL_ID_SHEETMUSIC + " INTEGER NOT NULL, " + COL_ID_SETLIST + " INTEGER NOT NULL, PRIMARY KEY (" + COL_ID_SHEETMUSIC + ", " + COL_ID_SETLIST + "), CONSTRAINT sheetmusic_setlist_fk_id_sheetmusic FOREIGN KEY (" + COL_ID_SHEETMUSIC + ") REFERENCES " + TABLE_SHEETMUSIC + " (" + COL_ID + ") ON DELETE CASCADE ON UPDATE CASCADE, CONSTRAINT sheetmusic_setlist_fk_id_setlist FOREIGN KEY (" + COL_ID_SETLIST + ") REFERENCES " + TABLE_SETLIST + " (" + COL_ID + ") ON DELETE CASCADE ON UPDATE CASCADE);";
    private static final String CREATE_TABLE_SHEETMUSIC_TAG = "CREATE TABLE " + TABLE_SHEETMUSIC_TAG + " (" + COL_ID_SHEETMUSIC + " INTEGER NOT NULL, " + COL_NAME_TAG + " TEXT NOT NULL, PRIMARY KEY (" + COL_ID_SHEETMUSIC + ", " + COL_NAME_TAG + "), CONSTRAINT sheetmusic_tag_fk_id_sheetmusic FOREIGN KEY (" + COL_ID_SHEETMUSIC + ") REFERENCES " + TABLE_SHEETMUSIC + " (" + COL_ID + ") ON DELETE CASCADE ON UPDATE CASCADE, CONSTRAINT sheetmusic_tag_fk_name_tag FOREIGN KEY (" + COL_NAME_TAG + ") REFERENCES " + TABLE_TAG + " (" + COL_NAME + ") ON DELETE CASCADE ON UPDATE CASCADE);";
    private static final String CREATE_TABLE_SETLIST_TAG = "CREATE TABLE " + TABLE_SETLIST_TAG + " (" + COL_ID_SETLIST + " INTEGER NOT NULL, " + COL_NAME_TAG + " TEXT NOT NULL, PRIMARY KEY (" + COL_ID_SETLIST + ", " + COL_NAME_TAG + "), CONSTRAINT setlist_tag_fk_id_setlist FOREIGN KEY (" + COL_ID_SETLIST + ") REFERENCES " + TABLE_SETLIST + " (" + COL_ID + ") ON DELETE CASCADE ON UPDATE CASCADE, CONSTRAINT setlist_tag_fk_name_tag FOREIGN KEY (" + COL_NAME_TAG + ") REFERENCES " + TABLE_TAG + " (" + COL_NAME + ") ON DELETE CASCADE ON UPDATE CASCADE);";

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
        cvSh.put(COL_NAME, s.getName());
        cvSh.put(COL_AUTHOR, s.getAuthor());
        cvSh.put(COL_GENRE, s.getGenre());
        cvSh.put(COL_TONE, s.getKey());
        cvSh.put(COL_INSTRUMENT, s.getInstument());
        cvSh.put(COL_MP3, s.getMp3());
        cvSh.put(COL_NOTES, s.getNotes());
        long idSheetmusic = db.insert(TABLE_SHEETMUSIC, null, cvSh);
        s.setId((int) idSheetmusic);

        // files + sheetmusic_files table
        addFilesToSheetmusic(s);

        // tags + sheetmusic_tag table
        addTagsToSheetmusic(s);
    }

    void addFilesToSheetmusic(Sheetmusic s){
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < s.getFiles().size(); i++) {
            ContentValues cvF = new ContentValues();
            cvF.put(COL_ADDRESS, s.getFiles().get(i));
            long idFile = db.insert(TABLE_FILE, null, cvF);

            ContentValues cvSh_F = new ContentValues();
            cvSh_F.put(COL_ID_SHEETMUSIC,s.getId());
            cvSh_F.put(COL_ID_FILE,idFile);
            db.insert(TABLE_SHEETMUSIC_FILE, null, cvSh_F);
        }
    }

    void addTagsToSheetmusic(Sheetmusic s){
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < s.getTags().size(); i++) {
            ContentValues cvT = new ContentValues();
            String nameTag = s.getTags().get(i);
            cvT.put(COL_NAME, nameTag);
            db.insert(TABLE_TAG, null, cvT);

            ContentValues cvSh_T = new ContentValues();
            cvSh_T.put(COL_ID_SHEETMUSIC,s.getId());
            cvSh_T.put(COL_NAME_TAG, nameTag);
            db.insert(TABLE_SHEETMUSIC_TAG, null, cvSh_T);
        }
    }

    void addSetlist(Setlist s){
        SQLiteDatabase db = this.getWritableDatabase();

        // setlist table
        ContentValues cvS = new ContentValues();
        cvS.put(COL_NAME, s.getName());
        cvS.put(COL_NOTES, s.getNotes());
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
            cvT.put(COL_NAME, nameTag);
            db.insert(TABLE_TAG, null, cvT);

            ContentValues cvSe_T = new ContentValues();
            cvSe_T.put(COL_ID_SETLIST,s.getId());
            cvSe_T.put(COL_NAME_TAG, nameTag);
            db.insert(TABLE_SETLIST_TAG, null, cvSe_T);
        }
    }

    void addToSetlist(int idSetlist, ArrayList<Sheetmusic> sheetmusic){
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < sheetmusic.size(); i++) {
            ContentValues cvS = new ContentValues();
            cvS.put(COL_ID_SHEETMUSIC, sheetmusic.get(i).getId());
            cvS.put(COL_ID_SETLIST, idSetlist);
            db.insert(TABLE_SHEETMUSIC_SETLIST, null, cvS);
        }
    }

    // Update -------------------------------------------------------------------
    void updateSheetmusic(Sheetmusic s){
        SQLiteDatabase db = this.getWritableDatabase();

        // sheetmusic table
        ContentValues cvSh = new ContentValues();
        cvSh.put(COL_NAME, s.getName());
        cvSh.put(COL_AUTHOR, s.getAuthor());
        cvSh.put(COL_GENRE, s.getGenre());
        cvSh.put(COL_TONE, s.getKey());
        cvSh.put(COL_INSTRUMENT, s.getInstument());
        cvSh.put(COL_MP3, s.getMp3());
        cvSh.put(COL_NOTES, s.getNotes());
        long shResult = db.update(TABLE_SHEETMUSIC, cvSh, "id = ?", new String[]{String.valueOf(s.getId())});

        // Files can be only added or deleted?
        // Tags can be only added or deleted?
    }


    void updateSetlist(Setlist s){
        SQLiteDatabase db = this.getWritableDatabase();

        // setlist table
        ContentValues cvS = new ContentValues();
        cvS.put(COL_NAME, s.getName());
        cvS.put(COL_NOTES, s.getNotes());
        long seResult = db.update(TABLE_SETLIST, cvS, "id = ?", new String[]{String.valueOf(s.getId())});

        // Tags can be only added or deleted
    }

    // Showing ------------------------------------------------------------------
    @SuppressLint("Range")
    public ArrayList<Sheetmusic> selectAllSheetmusic(){
        ArrayList<Sheetmusic> sheetmusic = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String qSheetmusic = "SELECT * FROM " + TABLE_SHEETMUSIC;
        Cursor cSh = db.rawQuery(qSheetmusic, null);

        while(cSh.moveToNext()) {
            // since i am reading the columns, i am not expecting values < 0, i hope that's correct
            // if it is -1, maybe it's null?
            int id = cSh.getInt(cSh.getColumnIndex(COL_ID));
            Sheetmusic s = selectOneSheetmusic(id);
            sheetmusic.add(s);
        }
        return sheetmusic;
    }

    @SuppressLint("Range")
    public Sheetmusic selectOneSheetmusic(int id){
        Sheetmusic s = new Sheetmusic(id);

        SQLiteDatabase db = this.getReadableDatabase();

        String qSheetmusic = "SELECT * FROM " + TABLE_SHEETMUSIC + " WHERE " + COL_ID + " = " + id + ";";
        Cursor cSh = db.rawQuery(qSheetmusic, null);

        while(cSh.moveToNext()){
            s.setName(cSh.getString(cSh.getColumnIndex(COL_NAME)));
            s.setAuthor(cSh.getString(cSh.getColumnIndex(COL_AUTHOR)));
            s.setGenre(cSh.getString(cSh.getColumnIndex(COL_GENRE)));
            s.setKey(cSh.getString(cSh.getColumnIndex(COL_TONE)));
            s.setInstument(cSh.getString(cSh.getColumnIndex(COL_INSTRUMENT)));
            s.setNotes(cSh.getString(cSh.getColumnIndex(COL_NOTES)));
            s.setMp3(cSh.getString(cSh.getColumnIndex(COL_MP3)));

            //files
            String qSheetmusicFiles = "SELECT * FROM " + TABLE_SHEETMUSIC_FILE + " WHERE " + COL_ID_SHEETMUSIC + " = " + id + ";";
            Cursor cShF = db.rawQuery(qSheetmusicFiles, null);

            ArrayList<String> files = new ArrayList<>();
            while(cShF.moveToNext()){
                int file_id = cShF.getInt(cShF.getColumnIndex(COL_ID_FILE));

                // get address of the file
                String qFiles = "SELECT * FROM " + TABLE_FILE + " WHERE " + COL_ID + " = " + file_id + ";";
                Cursor cF = db.rawQuery(qFiles, null);
                if(cF.moveToNext()) files.add(cF.getString(cF.getColumnIndex(COL_ADDRESS)));
            }
            s.setFiles(files);

            // tags
            String qSheetmusicTags = "SELECT * FROM " + TABLE_SHEETMUSIC_TAG + " WHERE " + COL_ID_SHEETMUSIC + " = " + id + ";";
            Cursor cT = db.rawQuery(qSheetmusicTags, null);

            ArrayList<String> tags = new ArrayList<>();
            while(cT.moveToNext()){
                String t = cT.getString(cT.getColumnIndex(COL_NAME_TAG));
                tags.add(t);
            }
            s.setTags(tags);
        }
        return s;
    }

    @SuppressLint("Range")
    public Setlist selectOneSetlist(int id){
        Setlist s = new Setlist(id);

        SQLiteDatabase db = this.getReadableDatabase();
        String qSetlist = "SELECT * FROM " + TABLE_SETLIST + " WHERE " + COL_ID + " = " + id + ";";
        Cursor cSe = db.rawQuery(qSetlist, null);

        while(cSe.moveToNext()){
            s.setName(cSe.getString(cSe.getColumnIndex(COL_NAME)));
            s.setNotes(cSe.getString(cSe.getColumnIndex(COL_NOTES)));

            // tags
            String qSetlistTags = "SELECT * FROM " + TABLE_SETLIST_TAG + " WHERE " + COL_ID_SETLIST + " = " + id + ";";
            Cursor cT = db.rawQuery(qSetlistTags, null);

            ArrayList<String> tags = new ArrayList<>();
            while(cT.moveToNext()){
                String t = cT.getString(cT.getColumnIndex(COL_NAME_TAG));
                tags.add(t);
            }
            s.setTags(tags);

            // sheetmusic
            String qSheetmusicSetlist = "SELECT * FROM " + TABLE_SHEETMUSIC_SETLIST + " WHERE " + COL_ID_SETLIST + " = " + id + ";";
            Cursor cSS = db.rawQuery(qSheetmusicSetlist, null);

            ArrayList<Sheetmusic> sheetmusic = new ArrayList<>();
            while(cSS.moveToNext()){
                int sh_id = cSS.getInt(cSS.getColumnIndex(COL_ID_SHEETMUSIC));
                Sheetmusic sh = selectOneSheetmusic(sh_id);
                sheetmusic.add(sh);
            }
            s.setSheetmusic(sheetmusic);
        }
        return s;
    }

    @SuppressLint("Range")
    public ArrayList<Setlist> selectAllSetlists(){
        ArrayList<Setlist> setlist = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String qSetlist = "SELECT * FROM " + TABLE_SETLIST;
        Cursor cSe = db.rawQuery(qSetlist, null);
        while(cSe.moveToNext()){
             int id = cSe.getInt(cSe.getColumnIndex(COL_ID));
            Setlist s = selectOneSetlist(id);
            setlist.add(s);
        }
        return setlist;

        // select------------------------------------------------------------


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
