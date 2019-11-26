package com.example.a11th_assingment.Room;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RepositoryContact {
    private DaoContact daoContact;

    public RepositoryContact(Application app) {
        Database db = Database.getDatabase(app);
        daoContact = db.daoContact();
    }

    LiveData<List<EntityContact>> getContacts() {
        return daoContact.getContacts();
    }

    public void insert(EntityContact contact) {
        new insertAsyncTask(daoContact).execute(contact);
    }

    public void deleteAll() {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                daoContact.deleteAll();
            }
        });
    }

    private static class insertAsyncTask extends AsyncTask<EntityContact, Void, Void> {
        private DaoContact mAsyncTaskDao;

        insertAsyncTask(DaoContact dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(EntityContact... entityContact) {
            mAsyncTaskDao.insert(entityContact[0]);
            return null;
        }
    }

}
