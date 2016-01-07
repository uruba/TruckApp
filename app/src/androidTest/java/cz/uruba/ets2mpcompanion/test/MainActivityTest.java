package cz.uruba.ets2mpcompanion.test;

import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.v4.app.Fragment;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;

import java.util.List;

import cz.uruba.ets2mpcompanion.MainActivity;
import cz.uruba.ets2mpcompanion.R;
import cz.uruba.ets2mpcompanion.fragments.MeetupListFragment;
import cz.uruba.ets2mpcompanion.fragments.ServerListFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static cz.uruba.ets2mpcompanion.test.matchers.WithToolbarTitle.withToolbarTitle;
import static org.hamcrest.Matchers.is;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity activity;
    int id;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    @Before
    protected void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
        id = R.id.settings;
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

    public void testOptionsMenu() {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(ViewMatchers.withText(cz.uruba.ets2mpcompanion.R.string.action_settings)).perform(click());
        onView(ViewMatchers.withId(cz.uruba.ets2mpcompanion.R.id.toolbar)).check(matches(withToolbarTitle(is(activity.getString(cz.uruba.ets2mpcompanion.R.string.action_settings)))));
    }
}
