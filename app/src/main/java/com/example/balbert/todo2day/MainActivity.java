package com.example.balbert.todo2day;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * ToDo2Day is a simple to-do list app.
 *
 * MainActivity is the Controller and main entry point for ToDo2Day.
 *
 * This Controller creates a list to hold the Tasks.
 *
 * Since the app currently consists only of backend functionality, we have
 * no front-end View to observe the changes.  So this version, Version 1, is for testing
 * to be sure the interface to the SQLite database behaves correctly.
 * In a sense we are using TDD, Test Driven Development, which is considered a best practice.
 *
 * The onCreate() method sets the content view, and is currently full of code to
 * test the functionality of the SQLite database.  This will likely be modified heavily
 * in Version 2.
 *
 * We are testing:
 * 1) clearing the database
 * 2) adding Tasks
 * 3) instantiating new database helpers
 * 4) running loops to add Tasks to the database from the List
 * 5) checking that deleting a Task works correctly
 */
public class MainActivity extends AppCompatActivity {

    private List<Task> mAllTasksList = new ArrayList<>();
    public static final String TAG = MainActivity.class.getSimpleName();

    /**
     * onCreate is currently checking that the SQLite database is working as intended.
     * We create Tasks, create a new DBHelper, add the Tasks to the database, delete tasks,
     * and clear the database to be sure the database is working.
     *
     * This method also retrieves each Task object from the database and logs them to the Android
     * Monitor for viewing and debugging purposes.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Clear the existing database
        deleteDatabase(DBHelper.DATABASE_NAME);

        // Pre-populate the List with 4 tasks
        mAllTasksList.add(new Task("Study for CS 273 Midterm", false));
        mAllTasksList.add(new Task("Finish IC# 08", false));
        mAllTasksList.add(new Task("Get sleep", true));
        mAllTasksList.add(new Task("Play League of Legends", true));

        // Let's instantiate a new DBHelper
        DBHelper db = new DBHelper(this);

        // Let's loop through the List and add each one to the database:
        for (Task t: mAllTasksList)
            db.addTask(t);

        // Let's clear out the List, then rebuild it from the database this time:
        mAllTasksList.clear();

        // Retrieve all tasks from the database
        mAllTasksList = db.getAllTasks();

        // Loop through each of the Tasks, print them to Log.i
        Log.i(TAG, "Showing all tasks:");
        for (Task t: mAllTasksList)
            Log.i(TAG, t.toString());

        Log.i(TAG, "After deleting task 4");
        db.deleteTask(mAllTasksList.get(2));
        mAllTasksList = db.getAllTasks();
        for (Task t: mAllTasksList)
            Log.i(TAG, t.toString());
    }
}
