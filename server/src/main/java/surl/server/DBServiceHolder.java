package surl.server;

public class DBServiceHolder {

    public static DBService db;

    public static void init(DBAdapter dbAdapter) {
        if (DBServiceHolder.db == null) {
            DBServiceHolder.db = new DBService(dbAdapter);
        }
    }
}
