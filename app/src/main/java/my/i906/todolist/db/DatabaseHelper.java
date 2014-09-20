package my.i906.todolist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import my.i906.todolist.model.Todo;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "todo.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "CREATE TABLE " + Todo.TABLE_TODO + " ("
            + Todo.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Todo.COLUMN_TITLE + " TEXT NOT NULL, "
            + Todo.COLUMN_DESCRIPTION + " TEXT NOT NULL, "
            + Todo.COLUMN_TIME + " INTEGER, "
            + Todo.COLUMN_DURACTION + " INTEGER"
            + ");";

    private static final String DUMMY_CONTENTS = "INSERT INTO " + Todo.TABLE_TODO + " ("
            + Todo.COLUMN_TITLE + ","
            + Todo.COLUMN_DESCRIPTION + ","
            + Todo.COLUMN_TIME + ","
            + Todo.COLUMN_DURACTION + ")"
            + "VALUES ("
            + "'First note', "
            + "'Some very long description here.',"
            + "300,"
            + "0"
            + ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
        database.execSQL(DUMMY_CONTENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
