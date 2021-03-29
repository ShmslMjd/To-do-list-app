package com.example.assignment1todolistapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;

    //create menu bar for the apps
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater(); // pop up menu
        menuInflater.inflate(R.menu.main_menu, menu); // get menu

        return super.onCreateOptionsMenu(menu); // return menu
    }

    // what happen if the user select the menu item - add button
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.add_note) {

            Intent intent = new Intent(getApplicationContext(),
                    note_editor.class); // open the link and page

            startActivity(intent);

            return true;

        }

        return false;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listView);

        SharedPreferences sharedPreferences =
                getApplicationContext().getSharedPreferences
                        ("com.example.assignment1todolistapp", Context.MODE_PRIVATE);

        // the shared preferences object cannot work directly with  the arraylist
        // this app will stored all the user input into array list first
        // then convert the arraylist into hash set before we can save
        // the user input into SP
        // user input --> array list --> hash net --> shared preferences
        HashSet<String> set = (HashSet<String>)
                sharedPreferences.getStringSet("notes", null);

        //Log.i("test", set.toString());

        if (set == null) {
            notes.add("Example note");
        } else {
            notes = new ArrayList(set);
        }

        arrayAdapter = new ArrayAdapter
                (this, android.R.layout.simple_list_item_1, notes);

        listView.setAdapter(arrayAdapter);

        // what happen if the user click the items in the listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            // override the abstract method
            // use an intent object to bring the user to another page
            // send the intent object to another page
            // the intent object contains noteID to
            // which task the user wish to edit
            // which record in the file the user with
            public void onItemClick
                    (AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(),
                        note_editor.class);
                intent.putExtra("noteId", i);
                startActivity(intent);

            }

        });

        //when the user long press the item on the list
        //delete the task
        listView.setOnItemLongClickListener
                (new AdapterView.OnItemLongClickListener() {

                    public boolean onItemLongClick
                            (AdapterView<?> adapterView, View view, int i, long l) {

                        final int itemToDelete = i;

                        new AlertDialog.Builder(MainActivity.this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Are you sure?")
                                .setMessage("Do you want to delete this note?")
                                .setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                notes.remove(itemToDelete); // remove item on the notes array list
                                                arrayAdapter.notifyDataSetChanged(); // update the list view

                                                SharedPreferences sharedPreferences =
                                                        getApplicationContext().getSharedPreferences
                                                                ("com.example.assignment1todolistapp",
                                                                        Context.MODE_PRIVATE); // open SP or file

                                                HashSet<String> set = new HashSet(MainActivity.notes); //convert array to hash set

                                                sharedPreferences.edit().putStringSet("notes", set).apply(); // open the SP, put new data, save all changes

                                            }

                                        }

                                )

                                .setNegativeButton("No", null)
                                .show();

                        return true;

                    }


                });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), note_editor.class);
                startActivity(intent);
            }
        });

    }
}