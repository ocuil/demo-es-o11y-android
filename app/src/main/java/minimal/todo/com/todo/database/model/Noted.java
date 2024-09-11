package minimal.todo.com.todo.database.model;

/**
 * Created by Michal Štrba on 01/08/18.
 */

public class Noted {
    public static final String TABLE_NAMED = "noted";
    public static final String COLUMN_ID = "idd";
    public static final String COLUMN_NOTE = "noteed";
    public static final String COLUMN_TIMESTAMP = "timestamped";
    public static final String COLUMN_TIME = "timeed";
    private int idb;
    private String noted;
    private String timestamped;
    private String timed;

    public static final String CREATE_TABLED =
            "CREATE TABLE " + TABLE_NAMED + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NOTE + " TEXT,"
                    + COLUMN_TIME + " Time,"
                    + COLUMN_TIMESTAMP + " TIME"
                    + ")";

    public Noted() {}
    public Noted(int idb, String noted, String timed, String timestamped) {
        this.idb = idb;
        this.noted = noted;
        this.timestamped = timestamped;
        this.timed = timed; }

    public int getIdb() { return idb; }
    public String getNoted() { return noted; }
    public void setNoted(String noted) { this.noted = noted; }
    public String getTimed() { return timed; }
    public void setTimed(String timed) { this.timed = timed; }
    public String getTimestamped() { return timestamped; }
    public void setIdb(int idb) { this.idb = idb; }
    public void setTimestamped(String timestamped) { this.timestamped = timestamped; }


}
