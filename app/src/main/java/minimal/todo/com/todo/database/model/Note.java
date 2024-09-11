package minimal.todo.com.todo.database.model;

/**
 * Created by Michal Štrba on 01/08/18.
 */

public class Note {
    public static final String TABLE_NAME = "notes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_TIME = "time";
    private int id;
    private String note;
    private String timestamp;
    private String time;

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NOTE + " TEXT,"
                    + COLUMN_TIME + " Tiime,"
                    + COLUMN_TIMESTAMP + " TIME"
                    + ")";

    public Note() {}
    public Note(int id, String note, String time , String timestamp) {
        this.id = id;
        this.note = note;
        this.timestamp = timestamp;
        this.time = time; }

    public int getId() { return id; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getTimestamp() { return timestamp; }
    public void setId(int id) { this.id = id; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
