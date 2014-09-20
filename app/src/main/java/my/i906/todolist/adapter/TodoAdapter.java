package my.i906.todolist.adapter;

import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;

import my.i906.todolist.R;

public class TodoAdapter extends SimpleCursorAdapter {

    public TodoAdapter(Context context, Cursor c, String[] from, int[] to, int flags) {
        super(context, R.layout.row_todo_list, c, from, to, flags);
    }

}
