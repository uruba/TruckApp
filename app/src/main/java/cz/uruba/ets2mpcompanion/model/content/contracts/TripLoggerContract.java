package cz.uruba.ets2mpcompanion.model.content.contracts;

import android.net.Uri;
import android.provider.BaseColumns;
import cz.uruba.ets2mpcompanion.BuildConfig;

public final class TripLoggerContract {

    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".triplogger";

    private static final Uri BASE_CONTENT_URI = new Uri.Builder()
            .scheme("content")
            .authority(AUTHORITY)
            .build();

    public static final class TripLoggerEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon()
                .appendPath("entry")
                .build();
        public static final String TABLE_NAME = "tripLoggerEntries";
        public static final String COLUMN_ORIGIN = "originCityId";
        public static final String COLUMN_DESTINATION = "destinationCityId";
        public static final String COLUMN_DISTANCE = "distance";
        public static final String COLUMN_CARGO_ID = "cargoId";
    }

    public static final class CargoDefinition implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon()
                .appendPath("cargo")
                .build();
        public static final String TABLE_NAME = "cargoDefinitions";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TONNAGE = "tonnage";
    }

    public static final class City implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon()
                .appendPath("city")
                .build();
        public static final String TABLE_NAME = "cities";
        public static final String COLUMN_NAME = "name";
    }

    public static final class Game implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon()
                .appendPath("game")
                .build();
        public static final String TABLE_NAME = "games";
        public static final String COLUMN_NAME = "name";
    }

    private TripLoggerContract() {

    }
}
