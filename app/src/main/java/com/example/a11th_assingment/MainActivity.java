package com.example.a11th_assingment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a11th_assingment.Room.EntityContact;
import com.example.a11th_assingment.Room.ModelContact;

import java.util.List;

/*
 * INTERFACE
 *  - Make an application which has a list and a two buttons.
 *  - A list row has a checkbox in it, so that you can select 1-n selected contacts.
 *
 * LOGIC
 *  - Pressing the other button opens the Android's contact selection screen.
 *  - Pressing the other button clears the list.
 *  - If you selected a contact, the name of the contact is added to the list.
 *  - List cannot have same contact twice.
 *
 * If you don't have any contacts in your emulated or real device, make a few dummy contacts.
 *
 * */
public class MainActivity extends AppCompatActivity {
    // Request codes
    final int PICK_CONTACT_REQUEST = 234;

    RwAdapterContact adapter;
    ModelContact modelContact;
    int contactsPermissionOk;
    Button getContact, clearContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Permissions. Ask permissions IF Android version > 22 AND no permission has been granted.
        contactsPermissionOk = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    0);
        }

        // Database
        modelContact = new ViewModelProvider(this).get(ModelContact.class);

        // RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        adapter = new RwAdapterContact(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Data Observer. Updates the view when data changes.
        modelContact.getContacts().observe(this, new Observer<List<EntityContact>>() {
            @Override
            public void onChanged(@Nullable final List<EntityContact> contacts) {
                adapter.setItems(contacts);
            }
        });

        // Buttons
        getContact = findViewById(R.id.getContact);
        clearContacts = findViewById(R.id.clearContacts);

        // Button logic
        getContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactPickerIntent, PICK_CONTACT_REQUEST);
            }
        });

        clearContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modelContact.deleteAll();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        contactsPermissionOk = grantResults[0];
        // Exit the app if permission is not granted
        if (contactsPermissionOk < 0) {
            Toast.makeText(this, "App needs permission to read Contacts to work!", Toast.LENGTH_LONG).show();
            this.finish();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_CONTACT_REQUEST:
                    try {
                        Uri uri = data.getData();
                        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                        cursor.moveToFirst();
                        int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                        String name = cursor.getString(nameIndex);

                        // Don't add duplicates
                        boolean isListed = false;
                        List<EntityContact> listedContacts = adapter.getItems();
                        for (EntityContact item : listedContacts) {
                            if (item.name.equals(name)) {
                                isListed = true;
                                break;
                            }
                        }

                        if (!isListed) {
                            // Insert to database
                            EntityContact contact = new EntityContact();
                            contact.name = name;
                            modelContact.insert(contact);
                            Toast.makeText(this, "Contact added!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Contact already listed!", Toast.LENGTH_LONG).show();
                        }
                        cursor.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } else {
            Toast.makeText(this, "Picking contact failed!", Toast.LENGTH_LONG).show();
        }
    }
}
