package my.i906.todolist.model;

public class Todo {

    public static final String TABLE_TODO = "todo";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_DURACTION = "duraction";

    public long id;
    public String title;
    public String description;
    public int time;
    public int duraction;
}
