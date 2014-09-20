package my.i906.todolist;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import my.i906.todolist.fragment.TodoEditFragment;
import my.i906.todolist.fragment.TodoListFragment;


public class MainActivity extends Activity implements TodoListFragment.Callbacks, TodoEditFragment.Callbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new TodoListFragment())
                    .commit();
        }
    }

    @Override
    public void onNewItemButtonClicked() {
        showEditFragment(-1);
    }

    @Override
    public void onItemSelected(long id) {
        showEditFragment(id);
    }

    @Override
    public void onItemSaved(long id) {
        showListFragment();
    }

    @Override
    public void onItemDiscarded(long id) {
        showListFragment();
    }

    private void showEditFragment(long id) {
        Fragment tef = TodoEditFragment.newInstance(id);

        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .replace(R.id.container, tef, "FRAG_TODO_EDIT")
                .addToBackStack("FRAG_TODO_EDIT")
                .commit();
    }

    private void showListFragment() {
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .replace(R.id.container, new TodoListFragment(), "FRAG_TODO_LIST")
                .commit();
    }

}
