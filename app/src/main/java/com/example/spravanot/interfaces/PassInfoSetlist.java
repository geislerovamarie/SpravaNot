package com.example.spravanot.interfaces;

public interface PassInfoSetlist {
    void deleteFromSetlist(int position, int idSh, int idSe);
    void deleteSetlist(int position, int idSe);
    void deleteTag(int position, String name);
    void updateSetlist(int position);
}
