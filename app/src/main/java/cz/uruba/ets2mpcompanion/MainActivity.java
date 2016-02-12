package cz.uruba.ets2mpcompanion;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.lang.reflect.Method;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.uruba.ets2mpcompanion.adapters.ViewPagerAdapter;
import cz.uruba.ets2mpcompanion.constants.URL;
import cz.uruba.ets2mpcompanion.fragments.MeetupListFragment;
import cz.uruba.ets2mpcompanion.fragments.ServerListFragment;
import cz.uruba.ets2mpcompanion.interfaces.AbstractThemedActivity;
import cz.uruba.ets2mpcompanion.utils.UICompat;

public class MainActivity extends AbstractThemedActivity {
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.tabs_area) TabLayout tabsArea;
    @Bind(R.id.viewpager) ViewPager viewPager;

    final int SERVER_LIST_FRAG_POS = 0;
    final int MEETUP_LIST_FRAG_POS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        setupViewPager(viewPager);
        tabsArea.setupWithViewPager(viewPager);

        // Lollipop-esque coloured "overscroll" effect for pre-Lollipop versions of Android
        UICompat.setOverscrollEffectColour(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        submitViewPagerAnalytics(viewPager.getCurrentItem());
    }

    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                // make the overflow menu's icons visible
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception ignored) {
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch (item.getItemId()) {
            case R.id.settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.link_ets2mp_home:
                intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(WebViewActivity.INTENT_EXTRA_URL, URL.ETS2MP_HOMEPAGE);
                intent.putExtra(WebViewActivity.INTENT_ACTIVITY_TITLE, getString(R.string.action_ets2mp_home));
                startActivity(intent);
                return true;
            case R.id.link_ets2mp_forum:
                intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(WebViewActivity.INTENT_EXTRA_URL, URL.ETS2MP_FORUM);
                intent.putExtra(WebViewActivity.INTENT_ACTIVITY_TITLE, getString(R.string.action_ets2mp_forum));
                startActivity(intent);
                return true;
            case R.id.link_ets2mp_convoys:
                intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(WebViewActivity.INTENT_EXTRA_URL, URL.ETS2MP_CONVOYS);
                intent.putExtra(WebViewActivity.INTENT_ACTIVITY_TITLE, getString(R.string.action_ets2mp_convoys));
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // we add two pages – a ServerListFragment one and a MeetupListFragment one
        final ServerListFragment serverListFragment = new ServerListFragment();
        final MeetupListFragment meetupListFragment = new MeetupListFragment();

        adapter.addFragment(serverListFragment, "SERVERS", SERVER_LIST_FRAG_POS);
        adapter.addFragment(meetupListFragment, "MEETUPS", MEETUP_LIST_FRAG_POS);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // if we move to the "meetup list" fragment,
                // we want to cancel any filtering possibly made beforehand,
                // so we reset its adapter
                if (position == MEETUP_LIST_FRAG_POS) {
                    meetupListFragment.resetMeetupList();
                }

                submitViewPagerAnalytics(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void submitViewPagerAnalytics(int position) {
        Tracker analyticsTracker = ((ETS2MPCompanionApplication) getApplication())
                .getAnalyticsTracker();

        switch (position) {
            case SERVER_LIST_FRAG_POS:
                analyticsTracker.setScreenName("Fragment – Server list");
                break;
            case MEETUP_LIST_FRAG_POS:
                analyticsTracker.setScreenName("Fragment – Meetup list");
                break;
            default:
                return;
        }

        analyticsTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
