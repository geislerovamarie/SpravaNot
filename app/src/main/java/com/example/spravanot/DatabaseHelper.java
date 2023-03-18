package com.example.spravanot;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

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
    private static final String CREATE_TABLE_SHEETMUSIC_FILE = "CREATE TABLE " + TABLE_SHEETMUSIC_FILE + " (id_sheetmusic INTEGER NOT NULL, id_file INTEGER NOT NULL, PRIMARY KEY (id_sheetmusic, id_file), CONSTRAINT sheetmusic_file_fk_id_sheetmusic FOREIGN KEY (id_sheetmusic) REFERENCES sheetmusic (id) ON UPDATE CASCADE, CONSTRAINT sheetmusic_file_fk_id_file FOREIGN KEY (id_file) REFERENCES file (id) ON UPDATE CASCADE);";
    private static final String CREATE_TABLE_SHEETMUSIC_SETLIST = "CREATE TABLE " + TABLE_SHEETMUSIC_SETLIST + " (id_sheetmusic INTEGER NOT NULL, id_setlist INTEGER NOT NULL, PRIMARY KEY (id_sheetmusic, id_setlist), CONSTRAINT sheetmusic_setlist_fk_id_sheetmusic FOREIGN KEY (id_sheetmusic) REFERENCES sheetmusic (id) ON UPDATE CASCADE, CONSTRAINT sheetmusic_setlist_fk_id_setlist FOREIGN KEY (id_setlist) REFERENCES setlist (id) ON UPDATE CASCADE);";
    private static final String CREATE_TABLE_SHEETMUSIC_TAG = "CREATE TABLE " + TABLE_SHEETMUSIC_TAG + " (id_sheetmusic INTEGER NOT NULL, name_tag TEXT NOT NULL, PRIMARY KEY (id_sheetmusic, name_tag), CONSTRAINT sheetmusic_tag_fk_id_sheetmusic FOREIGN KEY (id_sheetmusic) REFERENCES sheetmusic (id) ON UPDATE CASCADE, CONSTRAINT sheetmusic_tag_fk_name_tag FOREIGN KEY (name_tag) REFERENCES tag (name) ON UPDATE CASCADE);";
    private static final String CREATE_TABLE_SETLIST_TAG = "CREATE TABLE " + TABLE_SETLIST_TAG + " (id_setlist INTEGER NOT NULL, name_tag TEXT NOT NULL, PRIMARY KEY (id_setlist, name_tag), CONSTRAINT setlist_tag_fk_id_setlist FOREIGN KEY (id_setlist) REFERENCES setlist (id) ON UPDATE CASCADE, CONSTRAINT setlist_tag_fk_name_tag FOREIGN KEY (name_tag) REFERENCES tag (name) ON UPDATE CASCADE);";

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

    // Adding
    void addSheetmusic(){

    }

    void addSetlist(){

    }


}
