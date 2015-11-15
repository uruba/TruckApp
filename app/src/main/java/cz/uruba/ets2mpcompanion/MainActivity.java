package cz.uruba.ets2mpcompanion;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.uruba.ets2mpcompanion.adapters.ViewPagerAdapter;
import cz.uruba.ets2mpcompanion.fragments.MeetupListFragment;
import cz.uruba.ets2mpcompanion.fragments.ServerListFragment;
import cz.uruba.ets2mpcompanion.utils.UICompat;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.tabs_area) TabLayout tabsArea;
    @Bind(R.id.viewpager) ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        setupViewPager(viewPager);
        tabsArea.setupWithViewPager(viewPager);

        UICompat.setOverscrollEffectColour(getApplicationContext());
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        final int SERVER_LIST_FRAG_POS = 0;
        final int MEETUP_LIST_FRAG_POS = 1;

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
                if (Math.abs(MEETUP_LIST_FRAG_POS - position) == 1) {
                    meetupListFragment.resetMeetupList();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
