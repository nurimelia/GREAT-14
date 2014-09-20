package my.i906.todolist.fragment;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import my.i906.todolist.R;
import my.i906.todolist.contentprovider.TodoContentProvider;
import my.i906.todolist.model.Todo;
import my.i906.todolist.receiver.AlarmReceiver;

public class TodoEditFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String ARG_TODO_ID = "todoId";

    private long mTodoId;
    private Callbacks mCallbacks = sDummyCallbacks;
    private Uri mItemUri;

    @InjectView(R.id.todoedit_title)
    protected EditText mTitleView;

    @InjectView(R.id.todoedit_description)
    protected EditText mDescriptionView;

    //
    @InjectView(R.id.todo_time)
    protected EditText mTimeView;

    @InjectView(R.id.todo_duraction)
    protected Spinner mDuractionView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static TodoEditFragment newInstance(long id) {
        TodoEditFragment fragment = new TodoEditFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_TODO_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    public TodoEditFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        setMenuVisibility(false);

        if (getArguments() != null) {
            mTodoId = getArguments().getLong(ARG_TODO_ID);

            if (mTodoId != -1) {
                mItemUri = ContentUris.withAppendedId(TodoContentProvider.CONTENT_URI, mTodoId);
                getLoaderManager().initLoader(1, null, this);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_todo_edit, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @OnClick(R.id.action_save)
    protected void onSaveButtonClicked() {

        String title = mTitleView.getText().toString();
        String description = mDescriptionView.getText().toString();
        String new_time = mTimeView.getText().toString();

        int new_duraction = mDuractionView.getSelectedItemPosition();

        if (title.length() == 0 && description.length() == 0) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(Todo.COLUMN_TITLE, title);
        values.put(Todo.COLUMN_DESCRIPTION, description);
        values.put(Todo.COLUMN_TIME, new_time);

        values.put(Todo.COLUMN_DURACTION, new_duraction);

        if (mTodoId != -1) {
            getActivity().getContentResolver().update(mItemUri, values, null, null);
        } else {
            Uri newUri = getActivity().getContentResolver().insert(TodoContentProvider.CONTENT_URI, values);
            mTodoId = ContentUris.parseId(newUri);
        }

        mCallbacks.onItemSaved(mTodoId);

        AlarmManager am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent rem = new Intent(getActivity(), AlarmReceiver.class);
        rem.putExtra("todoedit_title", title);
        rem.putExtra("todoedit_description", description);
        rem.putExtra("todoedit_id", mTodoId);

        long trigger = getDuraction(Long.parseLong(new_time), new_duraction);

        PendingIntent pi = PendingIntent.getBroadcast(getActivity(), 0, rem, 0);
        am.set(AlarmManager.RTC, trigger, pi);

    }

    private long getDuraction(long time, int duraction) {
        long milliseconds = 1000;
        long minutes = milliseconds * 60;
        long hours = minutes * 60;
        long now = System.currentTimeMillis();

        if (duraction == 0) return now + time * milliseconds;
        else if (duraction == 1) return now + time * minutes;
        else if (duraction == 2) return now + time * hours;
        else return now;
    }

    @OnClick(R.id.action_discard)
    protected void onDiscardButtonClicked() {
        if (mTodoId != -1) {
            getActivity().getContentResolver().delete(mItemUri, null, null);
        }

        mCallbacks.onItemDiscarded(mTodoId);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(this.getActivity(), mItemUri, null, null, null, null);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.getCount() > 0) {
            data.moveToFirst();

            int iTitle = data.getColumnIndex(Todo.COLUMN_TITLE);
            int iDescription = data.getColumnIndex(Todo.COLUMN_DESCRIPTION);
            int iTime = data.getColumnIndex(Todo.COLUMN_TIME);
            int iDuraction = data.getColumnIndex(Todo.COLUMN_DURACTION);

            mTitleView.setText(data.getString(iTitle));
            mDescriptionView.setText(data.getString(iDescription));
            mTimeView.setText(data.getString(iTime));
            mDuractionView.setSelection(data.getInt(iDuraction));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = sDummyCallbacks;
    }

    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSaved(long id) { }

        @Override
        public void onItemDiscarded(long id) { }
    };

    public interface Callbacks {
        public void onItemSaved(long id);
        public void onItemDiscarded(long id);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

}
