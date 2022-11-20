package com.project.mymart.ui.supplier;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SupplierViewmodel extends ViewModel {
    private MutableLiveData<String> mText;

    public SupplierViewmodel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is suppliers fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
