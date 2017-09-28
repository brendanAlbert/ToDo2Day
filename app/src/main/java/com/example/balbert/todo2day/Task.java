package com.example.balbert.todo2day;

/**
 * Created by balbert on 9/28/2017.
 */

public class Task {
    private int mId;
    private String mDescription;
    private boolean mIsDone;

    public Task(int id, String description, boolean isDone) {
        mId = id;
        mDescription = description;
        mIsDone = isDone;
    }

    public Task(String description, boolean isDone) { this( -1, description, isDone ); }

    public Task() { this( -1, "", false ); }

    public int getId() { return mId; }

    public String getDescription() { return mDescription; }

    public void setDescription(String description) { mDescription = description; }

    public boolean isDone() { return mIsDone; }

    public void setDone(boolean done) { mIsDone = done; }

    @Override
    public String toString() {
        return "Task{" +
                "Id=" + mId +
                ", description='" + mDescription + '\'' +
                ", IsDone=" + mIsDone +
                '}';
    }
}
