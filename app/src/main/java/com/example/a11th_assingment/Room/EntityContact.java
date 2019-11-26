package com.example.a11th_assingment.Room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "contacts")
public class EntityContact {
    public String name;
    @PrimaryKey(autoGenerate = true)
    int id_contacts;
}
