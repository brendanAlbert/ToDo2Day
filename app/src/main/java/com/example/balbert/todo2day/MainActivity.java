package com.example.balbert.todo2day;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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

    // Reference to the list of all tasks
    private List<Task> mAllTasksList = new ArrayList<>();

    public static final String TAG = MainActivity.class.getSimpleName();

    // Reference to the database:
    private DBHelper mDB;

    // References to the widgets needed
    private EditText mDescriptionEditText;
    private ListView mTaskListView;

    // Reference to the custom list adapter
    TaskListAdapter mTaskListAdapter;

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

        mDB = new DBHelper(this);
        mDescriptionEditText = (EditText) findViewById(R.id.taskEditText);
        mTaskListView = (ListView) findViewById(R.id.taskListView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Database related "stuff"

        // 1) Populate the list from the database (using DBHelper)
        mAllTasksList = mDB.getAllTasks();

        // 2) Connect the ListView with the custom list Adapter
        mTaskListAdapter = new TaskListAdapter(this, R.layout.task_item, mAllTasksList);
        mTaskListView.setAdapter(mTaskListAdapter);

    }

    public void addTask(View v)
    {
        // Check to see if the description is empty or null
        String description = mDescriptionEditText.getText().toString();
        if (TextUtils.isEmpty(description))
            Toast.makeText(this, "Please enter a description.", Toast.LENGTH_LONG).show();
        else
        {
            // Create the Task
            Task newTask = new Task(description, false);
            // Add it to the database
            mDB.addTask(newTask);
            // Add it to the List
            mAllTasksList.add(newTask);
            // Notify the list adapter that it has been changed
            mTaskListAdapter.notifyDataSetChanged();
            // Clear out the EditText
            mDescriptionEditText.setText("");
        }
    }

    public void clearAllTasks(View v)
    {
        mDB.deleteAllTasks();
        mAllTasksList.clear();
        mTaskListAdapter.notifyDataSetChanged();
        Toast.makeText(this, "All tasks have been cleared.", Toast.LENGTH_LONG).show();
    }

    public void toggleTaskStatus(View v)
    {
        CheckBox selectedCheckBox = (CheckBox) v;
        Task selectedTask = (Task) selectedCheckBox.getTag();
        // Update the Task
        selectedTask.setDone(selectedCheckBox.isChecked());
        // Update the database
        mDB.updateTask(selectedTask);
    }
}
