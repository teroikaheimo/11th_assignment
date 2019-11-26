package com.example.a11th_assingment.Room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DaoContact {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(EntityContact contact);

    @Query("DELETE FROM contacts;")
    void deleteAll();

    @Query("SELECT * FROM contacts;")
    LiveData<List<EntityContact>> getContacts();
}
