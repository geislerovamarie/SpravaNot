package com.example.spravanot.interfaces;

public interface PassInfoSheetmusic {
    void deleteSheetmusic(int position, int idSh);

    void updateSheetmusic(int position);

    void toggleFavorite(int position, int idSh);

    void deleteTag(int position, String name);
}