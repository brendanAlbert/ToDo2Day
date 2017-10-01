package com.example.balbert.todo2day;

/**
 * Task represents the Model of ToDo2Day's MVC architecture.
 *
 * The purpose of this app is to maintain a list of tasks that need to be
 * or have been completed.  Each task is represented by a Task object.
 *
 * Task's have an id, a description and keep track of whether they are done or not.
 *
 * There are three constructors.  The first constructor takes all three private
 * member variables as its parameters.  The second constructor takes two parameters,
 * the excluded parameter is the id.  We want the database helper class to determine
 * the id for us.  The third constructor is a default constructor.  It takes no parameters
 * and assigns -1 for the id, empty string for the description and false for if it's done.
 *
 * There are also accessors and mutators for the member variables, note however that there
 * is no setId, we want the database to handle this for us.
 *
 * The last method is overriding toString so we have a useful representation of each Task
 * when viewed in the log, for debugging purposes, since at the time of writing
 * this app lacked a View.
 *
 * Created by balbert on 9/28/2017.
 */
public class Task {
    private int mId;
    private String mDescription;
    private boolean mIsDone;

    /**
     * Task has three constructors. This is the 3 parameter version.
     * @param id
     * @param description
     * @param isDone
     */
    public Task(int id, String description, boolean isDone) {
        mId = id;
        mDescription = description;
        mIsDone = isDone;
    }

    /**
     * Task has three constructors. This is the 2 parameter version.  The id is excluded
     * because we want to set the id to a junk value.  The database itself will determine
     * the appropriate id.
     * @param description
     * @param isDone
     */
    public Task(String description, boolean isDone) { this( -1, description, isDone ); }

    /**
     * Task has three constructors.  This is the default constructor which takes no parameters.
     * A call is made using this() to the 3 parameter constructor.  The values are initialized
     * to junk and will be updated elsewhere.
     */
    public Task() { this( -1, "", false ); }

    /**
     *
     * @return the Task's id.
     */
    public int getId() { return mId; }

    /**
     *
     * @return the Task's description.
     */
    public String getDescription() { return mDescription; }

    /**
     *
     * @param description is a String that describes the Task the user wants to complete
     *                    or has completed.
     */
    public void setDescription(String description) { mDescription = description; }

    /**
     *
     * @return returns either a zero to indicate the Task is * NOT * done,
     * or a 1 to indicate completion.
     */
    public boolean isDone() { return mIsDone; }

    /**
     *
     * @param done takes an int of 1 or 0 to indicate whether the task is
     *             done or not done, respectively.
     */
    public void setDone(boolean done) { mIsDone = done; }

    /**
     * Override the toString() method so that when a Task object is logged
     * we can view the state of its member variables rather than its memory address.
     *
     * @return a string representation of the Task object to determine its state.
     */
    @Override
    public String toString() {
        return "Task{" +
                "id=" + mId +
                ", description='" + mDescription + '\'' +
                ", isDone=" + mIsDone +
                '}';
    }
}
