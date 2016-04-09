package cz.uruba.ets2mpcompanion.test;

import android.test.InstrumentationTestCase;

import cz.uruba.ets2mpcompanion.model.content.contracts.TripLoggerContract;

public class UnitTestTripLogger extends InstrumentationTestCase {

    public void testBaseUri() {
          assertEquals(
                  "content://cz.uruba.ets2mpcompanion.triplogger",
                  TripLoggerContract.BASE_CONTENT_URI.toString()
          );
    }

    public void testTripLoggerEntryUri() {
        assertEquals(
                "content://cz.uruba.ets2mpcompanion.triplogger/entry",
                TripLoggerContract.TripLoggerEntry.CONTENT_URI.toString()
        );
    }

    public void testCargoDefinitionUri() {
        assertEquals(
                "content://cz.uruba.ets2mpcompanion.triplogger/cargo",
                TripLoggerContract.CargoDefinition.CONTENT_URI.toString()
        );
    }

    public void testCityUri() {
        assertEquals(
                "content://cz.uruba.ets2mpcompanion.triplogger/city",
                TripLoggerContract.City.CONTENT_URI.toString()
        );
    }

    public void testGameUri() {
        assertEquals(
                "content://cz.uruba.ets2mpcompanion.triplogger/game",
                TripLoggerContract.Game.CONTENT_URI.toString()
        );
    }
}
