package cz.uruba.ets2mpcompanion.model.content.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import cz.uruba.ets2mpcompanion.model.content.DBHelper;
import cz.uruba.ets2mpcompanion.model.content.contracts.TripLoggerContract;

public class TripLoggerProvider extends ContentProvider {

    private static final int TRIP_LIST = 1;
    private static final int TRIP_ENTRY = 2;
    private static final int CARGO_LIST = 11;
    private static final int CARGO_ENTRY = 12;
    private static final int CITY_LIST = 21;
    private static final int CITY_ENTRY = 22;
    private static final int GAME_LIST = 31;
    private static final int GAME_ENTRY = 32;
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(TripLoggerContract.AUTHORITY, "trips", TRIP_LIST);
        uriMatcher.addURI(TripLoggerContract.AUTHORITY, "trips/#", TRIP_ENTRY);
        uriMatcher.addURI(TripLoggerContract.AUTHORITY, "cargoes", CARGO_LIST);
        uriMatcher.addURI(TripLoggerContract.AUTHORITY, "cargoes/#", CARGO_ENTRY);
        uriMatcher.addURI(TripLoggerContract.AUTHORITY, "cities", CITY_LIST);
        uriMatcher.addURI(TripLoggerContract.AUTHORITY, "cities/#", CITY_ENTRY);
        uriMatcher.addURI(TripLoggerContract.AUTHORITY, "games", GAME_LIST);
        uriMatcher.addURI(TripLoggerContract.AUTHORITY, "games/#", GAME_ENTRY);
    }

    private DBHelper databaseHelper;

    @Override
    public boolean onCreate() {
        databaseHelper = new DBHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection,
                        String selection, String[] selectionArgs,
                        String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
