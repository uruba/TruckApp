package cz.uruba.ets2mpcompanion.test;

import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.v4.app.Fragment;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;

import java.util.List;

import cz.uruba.ets2mpcompanion.MainActivity;
import cz.uruba.ets2mpcompanion.fragments.MeetupListFragment;
import cz.uruba.ets2mpcompanion.fragments.ServerListFragment;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity activity;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    public MainActivityTest(Class<MainActivity> activityClass) {
        super(activityClass);
    }

    @Override
    @Before
    protected void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        activity = getActivity();
    }

    @UiThreadTest
    public void testFragmentCollection() {
        List<Fragment> fragments = activity.getSupportFragmentManager().getFragments();

        // there have to be fragments
        assertNotNull(fragments);

        // 2 fragments, to be precise
        assertEquals(fragments.size(), 2);

        // the first one we create is a ServerListFragment
        assertTrue(fragments.get(0) instanceof ServerListFragment);

        // and the second one is a MeetupListFragment
        assertTrue(fragments.get(1) instanceof MeetupListFragment);
    }
}
