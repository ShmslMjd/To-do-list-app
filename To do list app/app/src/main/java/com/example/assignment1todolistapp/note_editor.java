package com.example.assignment1todolistapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashSet;

// the user input the task detail here and save all the user input
// into the share preferences for later use, later we can edit the details
// or delete the details

public class note_editor extends AppCompatActivity {

    int noteId; // we need to know the taskID, different task have different ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        EditText editText = (EditText) findViewById(R.id.editText);

        Intent intent = getIntent();
        noteId = intent.getIntExtra("noteId", -1);
        // note id variable transferred from main activity

        if (noteId != -1) { // display the contents that you pull out from the file

            editText.setText(MainActivity.notes.get(noteId));

        } else {

            MainActivity.notes.add(""); // display nothing
            noteId = MainActivity.notes.size() - 1; // arraylist item start 0
            MainActivity.arrayAdapter.notifyDataSetChanged(); // update arraylist
        }

        // the above if else code used to check whether the arraylist
        // is empty or not, if tis empty display nothing, and add nothing
        // because this is the first time we used the app, the are no user
        // input in the shared preferences

        // we need a text box event listener here to listen to the event
        // generated from the textbox, when the user type inside
        // the text box save all the user input into shared preferences

        editText.addTextChangedListener(new TextWatcher() {

            // what happen before the changes in the textbox
            public void beforeTextChanged(CharSequence charSequence,
                                          int i, int i1, int i2) {

            }

            // what happen during the changes in the textbox
            public void onTextChanged(CharSequence charSequence, int i, int i1,
                                      int i2) {

                MainActivity.notes.set(noteId, String.valueOf(charSequence));
                MainActivity.arrayAdapter.notifyDataSetChanged();

                SharedPreferences sharedPreferences =
                        getApplicationContext().getSharedPreferences
                                ("com.example.assignment1todolistapp",
                                        Context.MODE_PRIVATE);

                // convert the notes array list into hash set
                // because SP cannot read data from notes array
                HashSet<String> set = new HashSet(MainActivity.notes);

                sharedPreferences.edit().putStringSet("notes", set).apply();

            }

            // what happen after the changes in the textbox
            public void afterTextChanged(Editable editable) {

            } // nothing happen

        }); //end of listener

    }
}