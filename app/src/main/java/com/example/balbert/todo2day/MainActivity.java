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
 * References are created for the database, the various Views and the TaskListAdapter
 * which we use to inflate the Views for our custom Tasks, which appear as the text of
 * the description and a checkbox.
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
     * The onCreate method sets the content view.
     * The database is initialized and Views are referenced.
     *
     * @param savedInstanceState recovers any previous state if applicable.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDB = new DBHelper(this);
        mDescriptionEditText = (EditText) findViewById(R.id.taskEditText);
        mTaskListView = (ListView) findViewById(R.id.taskListView);

    }

    /**
     * The method onResume is used during the Activity lifecycle.
     *
     * As a user uses your app, various Activities which make up the functionality
     * of the app are created, started, restarted, paused, resumed, stopped and destroyed.
     *
     * onResume is called after onStart when the app is first launched or restarted.
     * If the user switches activities in the app, the first Activity is paused.
     * Then the paused Activity can be returned to using onResume.
     *
     * Any method calls or functionality you would want to happen every time an Activity
     * is returned to should be placed in onResume.
     *
     * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
     *
     * The onResume method of onResume's super class is called.
     *
     * Our list of Tasks is filled by making a call to getAllTasks() from the database.
     *
     * mTaskListAdapter is the custom adapter we made which is used to render each Task
     * with a checkbox.  It is initialized and set.
     */
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

    /**
     * addTask() is called when the user taps the ADD TASK button after entering a description.
     *
     * TextUtils has a method isEmpty which we use here to check that the user in fact entered
     * a Task description.  If they tapped add task without entering a description, we display
     * a Toast to remind them to enter a description.
     *
     * If they did enter a description then we instantiate a new Task with it.
     * The Task is then added to the database and the Task-list.
     *
     * We inform the adapter of the change and then set the EditText field back to an empty string.
     * @param v
     */
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

    /**
     *  clearAllTask() is called when the user taps the CLEAR ALL TASKS button.
     *  All the Tasks are deleted from the database, the list of Tasks is cleared,
     *  the adapter is notified of the change so the View matches the Model/database.
     *  Finally, a Toast pops up to inform the user of their action.
     *
     * @param v
     */
    public void clearAllTasks(View v)
    {
        mDB.deleteAllTasks();
        mAllTasksList.clear();
        mTaskListAdapter.notifyDataSetChanged();
        Toast.makeText(this, "All tasks have been cleared.", Toast.LENGTH_LONG).show();
    }

    /**
     *  toggleTaskStatus() is called whenever the user taps on a Task.
     *  If the Task is checked, it becomes unchecked and vice versa.
     *
     *  We know which Task the user tapped because we downcast to a CheckBox the View object that
     *  enters the method.
     *
     *  Behind each Task is a Tag, we create a Task object called selectedTask from that Tag.
     *
     *  We use this newly created selectedTask and update its isDone property.
     *
     *  Lastly the database is informed of the Task's updated isDone state.
     *
     * @param v
     */
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
