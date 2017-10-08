package com.example.balbert.todo2day;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import java.util.List;

/**
 *
 * The TaskListAdapter class is for creating a custom way to display each Task.
 *
 * The heavy lifting of this class takes place in the getView method.
 * getView retrieves a selected Task, inflates the View for that Task,
 * gets a reference to its CheckBox and provides the appropriate
 * description text and isDone state (checked or unchecked).
 *
 * A new feature of this app from the others worked on before is setting a tag.
 * A tag is an invisible locker behind the select View where we can store
 * properties or data for retrieval.
 *
 * getView then returns the inflated View.
 *
 * Created by balbert on 10/3/2017.
 */

public class TaskListAdapter extends ArrayAdapter<Task> {

    private Context mContext;
    private int mResourceId;
    private List<Task> mTaskList;

    public TaskListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Task> objects) {
        super(context, resource, objects);
        mContext = context;
        mResourceId = resource;
        mTaskList = objects;
    }

    /**
     *
     *  getView() takes three parameters.
     *  The first parameter, which is also the only one being used, is the position in the list
     *  of the View which is being inflated.
     *  The other two parameters, convertView and parent, are used to reuse the old view if
     *  possible or necessary, and the parent is where this View will be attached.
     *
     *  The View is inflated and looks like a CheckBox with text from the user-provided
     *  description alongside the CheckBox.
     *
     * @param position is the particular Task the user tapped on
     * @param convertView is not used for the purposes of this app.
     * @param parent is the parent on which this View will be attached.
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Retrieve the selectedTask
        Task selectedTask = mTaskList.get(position);

        // Use LayoutInflater to inflate View for this specific task:
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(mResourceId, null);

        // Get reference to the checkbox
        CheckBox selectedCheckBox = view.findViewById(R.id.isDoneCheckBox);
        selectedCheckBox.setChecked(selectedTask.isDone());
        selectedCheckBox.setText(selectedTask.getDescription());

        // Tag is an invisible locker behind each view (store anything in tag)
        selectedCheckBox.setTag(selectedTask);
        return view;
    }
}
