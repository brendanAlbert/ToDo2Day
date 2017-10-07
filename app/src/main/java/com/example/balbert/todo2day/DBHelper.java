package com.example.balbert.todo2day;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * DBHelper is a helper Model class.  DBHelper extends SQLiteOpenHelper
 * and by extension the two methods, onCreate and onUpgrade.  These two
 * methods must be implemented in order to create and update/upgrade the
 * SQLite database.
 *
 * This class contains database and table constants.
 * Database constants are for the database's name, any tables and the version.
 *
 * The table constants are for the columns of the table, such as the id, description and
 * isDone.
 *
 * There are methods for:
 * 1) adding a Task,
 * 2) get all Tasks,
 * 3) delete a Task,
 * 4) update a Task,
 * 5) get a single Task
 *
 * Created by balbert on 9/28/2017.
 */
class DBHelper extends SQLiteOpenHelper{

    // Create some useful database constants
    public static final String DATABASE_NAME    = "ToDo2Day";
    public static final String DATABASE_TABLE   = "Tasks";
    public static final int    DATABASE_VERSION = 1;

    // Create some useful table constants
    public static final String KEY_FIELD_ID = "_id";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_DONE = "done";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * onCreate must be implemented when a class extends SQLiteOpenHelper.
     * This is because without this method the database would never be created.
     *
     * It takes an SQLiteDatabase as a parameter.
     *
     * Inside the method we create any tables that the database requires.
     * This is accomplished by calling execSQL on the database object passed into onCreate.
     *
     * Then an SQL statement to create a table is written and pass into the execSQL method being
     * called on the database object.
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Generate an SQL statement for creating a new table
        // CREATE TABLE tasks ( _id INTEGER PRIMARY KEY, description TEXT, done INTEGER )
        String createTable = "CREATE TABLE " + DATABASE_TABLE
                + " ( " + KEY_FIELD_ID + " INTEGER PRIMARY KEY, "
                + FIELD_DESCRIPTION + " TEXT, "
                + FIELD_DONE + " INTEGER " + ")";
        db.execSQL(createTable);
    }

    /**
     * onUpgrade must also be implemented when a class extends SQLiteOpenHelper.
     * This method must be implemented because it will be called any time
     * the database or its tables are update/upgraded.
     *
     * Our current implementation drops a specified table from the database and then
     * creates it anew with any changes made.
     * @param db
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        // 1) Drop the existing table
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        // 2) Build (create) the new one
        onCreate(db);
    }

    /**
     * This method is called on the database to insert a new Task.
     *
     * @param newTask is a Task object representing one of the Users tasks.
     */
    public void addTask(Task newTask)
    {
        SQLiteDatabase db = getWritableDatabase();
        // Specify the values (fields) to insert into the database
        // Everything *except* the primary key _id (auto assigned)
        ContentValues values = new ContentValues();
        values.put(FIELD_DESCRIPTION, newTask.getDescription());
        values.put(FIELD_DONE, newTask.isDone() ? 1 : 0);
        db.insert(DATABASE_TABLE, null, values);
        db.close();
    }

    /**
     * getAllTasks() returns a List of all Tasks.
     *
     * The method instantiates an ArrayList,
     * gets a readable reference to the database,
     * instantiates a Cursor object which will be used like an iterable to collect
     * all the Tasks from the database and add them to the ArrayList,
     * close the Cursor and the database,
     * returns the List.
     *
     * @return a list of all the Tasks contained in the database.
     */
    public List<Task> getAllTasks()
    {
        List<Task> allTasksList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        // To retrieve data from a database table, we use a Cursor
        // Cursor stores the results of a query
        Cursor cursor = db.query(DATABASE_TABLE,
                new String[] {KEY_FIELD_ID, FIELD_DESCRIPTION, FIELD_DONE},
                null, null, null, null, null);

        if ( cursor.moveToFirst() )
        {
            // Guaranteed at least one result from query
            do {
                Task task = new Task(cursor.getInt(0), cursor.getString(1), cursor.getInt(2) == 1);
                allTasksList.add(task);
            } while ( cursor.moveToNext() );
        }
        cursor.close();
        // Don't forget to close or else the apps performance will degrade and eventually crash
        db.close();
        return allTasksList;
    }

    /**
     * deleteTask() gets a writable reference to the database,
     * deletes the specified Task,
     * and closes the database.
     * @param taskToDelete a Task object so we know which Task to delete from the database.
     */
    public void deleteTask (Task taskToDelete)
    {
        SQLiteDatabase db = getWritableDatabase();
        // "DELETE FROM tasks WHERE _id = ?
        db.delete(DATABASE_TABLE, KEY_FIELD_ID + "=" + taskToDelete.getId(), null);
        db.close();
    }

    /**
     * deleteAllTasks() is used to .. so surprisingly... delete all the Tasks from the database.
     */
    public void deleteAllTasks()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(DATABASE_TABLE, null, null);
        db.close();
    }

    /**
     * updateTask() gets a writable reference to the database,
     * instantiates a ContentValues object,
     * the new values for the Task are put into the ContentValues object,
     * the database is called and updates the specified Task,
     * and closes the database.
     * @param taskToEdit the Task to update/edit
     */
    public void updateTask (Task taskToEdit)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FIELD_DESCRIPTION, taskToEdit.getDescription());
        values.put(FIELD_DONE, taskToEdit.isDone() ? 1 : 0);

        db.update(DATABASE_TABLE, values, KEY_FIELD_ID + "=" + taskToEdit.getId(), null);
        db.close();
    }

    /**
     * getSingleTask() takes an int id parameter to find the specified Task.
     *
     * Declares a new Task object, instantiated to null.
     * A readable database reference is established.
     * A Cursor object is instantiated and used to find the specified Task.
     * The Cursor is first checked to be sure that the specified id exists,
     * if it does, the declared new Task object gets the appropriate Task data.
     * The Cursor and database connections are closed.
     * The singleTask is returned.
     *
     * @param id an int which matches the id of the task being searched for.
     * @return
     */
    public Task getSingleTask(int id)
    {
        Task singleTask = null;

        SQLiteDatabase db = getReadableDatabase();
        // To retrieve data from a database table, we use a Cursor
        // Cursor stores the results of a query
        Cursor cursor = db.query(DATABASE_TABLE,
                new String[] {KEY_FIELD_ID, FIELD_DESCRIPTION, FIELD_DONE},
                KEY_FIELD_ID + "=" + id, null, null, null, null);

        if ( cursor.moveToFirst() )
        {
            // Guaranteed at least one result from query
            singleTask = new Task(cursor.getInt(0), cursor.getString(1), cursor.getInt(2) == 1);
        }
        cursor.close();
        // Don't forget to close or else the apps performance will degrade and eventually crash
        db.close();
        return singleTask;
    }
}
