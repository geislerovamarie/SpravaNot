package com.example.spravanot.ui.setlists;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SetlistsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public SetlistsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}