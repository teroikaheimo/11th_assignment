package com.example.a11th_assingment.Room;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ModelContact extends AndroidViewModel {

    private RepositoryContact mRepository;

    private LiveData<List<EntityContact>> mAllContacts;

    public ModelContact(Application application) {
        super(application);
        mRepository = new RepositoryContact(application);
        mAllContacts = mRepository.getContacts();
    }

    public LiveData<List<EntityContact>> getContacts() {
        return mAllContacts;
    }

    public void insert(EntityContact contact) {
        mRepository.insert(contact);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }

}
