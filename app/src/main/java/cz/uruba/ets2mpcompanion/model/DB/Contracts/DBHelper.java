package cz.uruba.ets2mpcompanion.model.DB.Contracts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "truckapp_data";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        initDB(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void initDB(SQLiteDatabase db) {
        StringBuffer parameters = new StringBuffer();

        parameters.append(TripLoggerContract.TripLoggerEntry._ID);
        parameters.append(" integer primary key autoincrement,");
        parameters.append(TripLoggerContract.TripLoggerEntry.COLUMN_ORIGIN);
        parameters.append(" integer references ");
        parameters.append(TripLoggerContract.City.TABLE_NAME);
        parameters.append("(");
        parameters.append(TripLoggerContract.City._ID);
        parameters.append("),");
        parameters.append(TripLoggerContract.TripLoggerEntry.COLUMN_DESTINATION);
        parameters.append(" integer references ");
        parameters.append(TripLoggerContract.City.TABLE_NAME);
        parameters.append("(");
        parameters.append(TripLoggerContract.City._ID);
        parameters.append("),");
        parameters.append(TripLoggerContract.TripLoggerEntry.COLUMN_DISTANCE);
        parameters.append(" real,");
        parameters.append(TripLoggerContract.TripLoggerEntry.COLUMN_CARGO_ID);
        parameters.append(" integer references ");
        parameters.append(TripLoggerContract.CargoDefinition.TABLE_NAME);
        parameters.append("(");
        parameters.append(TripLoggerContract.CargoDefinition._ID);
        parameters.append(")");

        db.execSQL(
                getCreateTableQuery(
                        TripLoggerContract.TripLoggerEntry.TABLE_NAME,
                        parameters.toString()
                )
        );

        parameters = new StringBuffer();
        parameters.append(TripLoggerContract.CargoDefinition._ID);
        parameters.append(" integer primary key,");
        parameters.append(TripLoggerContract.CargoDefinition.COLUMN_NAME);
        parameters.append(" text,");
        parameters.append(TripLoggerContract.CargoDefinition.COLUMN_TONNAGE);
        parameters.append(" integer");

        db.execSQL(
                getCreateTableQuery(
                        TripLoggerContract.CargoDefinition.TABLE_NAME,
                        parameters.toString()
                )
        );

        parameters = new StringBuffer();
        parameters.append(TripLoggerContract.City._ID);
        parameters.append(" integer primary key,");
        parameters.append(TripLoggerContract.City.COLUMN_NAME);
        parameters.append(" text");

        db.execSQL(
                getCreateTableQuery(
                        TripLoggerContract.City.TABLE_NAME,
                        parameters.toString()
                )
        );

        parameters = new StringBuffer();
        parameters.append(TripLoggerContract.Game._ID);
        parameters.append(" integer primary key,");
        parameters.append(TripLoggerContract.Game.COLUMN_NAME);
        parameters.append(" text");

        db.execSQL(
                getCreateTableQuery(
                        TripLoggerContract.Game.TABLE_NAME,
                        parameters.toString()
                )
        );
    }

    private String getCreateTableQuery(String tableName, String parameterString) {
        return String.format("CREATE TABLE %1$s (%2$s);", tableName, parameterString);
    }
}
