package cz.uruba.ets2mpcompanion.test;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.After;
import org.junit.Before;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.uruba.ets2mpcompanion.MainActivity;
import cz.uruba.ets2mpcompanion.R;
import cz.uruba.ets2mpcompanion.fragments.MeetupListFragment;
import cz.uruba.ets2mpcompanion.fragments.ServerListFragment;
import cz.uruba.ets2mpcompanion.fragments.SettingsFragment;
import cz.uruba.ets2mpcompanion.test.utils.ViewPagerIdlingResource;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static cz.uruba.ets2mpcompanion.test.matchers.WithToolbarTitle.withToolbarTitle;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.is;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity activity;
    private MockWebServer mockServer;
    private Dispatcher dispatcherGood, dispatcherBad;

    private final List<Integer> optionsMenuEntries = new ArrayList<>(
            Arrays.asList(
                    R.string.action_settings,
                    R.string.action_truckersmp_home,
                    R.string.action_truckersmp_forum,
                    R.string.action_truckersmp_convoys
            )
    );

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    @Before
    protected void setUp() throws Exception {
        super.setUp();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(SettingsFragment.PREF_AUTO_REFRESH_ENABLED, true);
        editor.putLong(SettingsFragment.PREF_AUTO_REFRESH_INTERVAL, 60 * 1000);
        editor.commit();

        mockServer = new MockWebServer();

        dispatcherGood = new Dispatcher() {

            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {

                switch (request.getPath()) {
                    case "/servers/":
                        return new MockResponse().setResponseCode(200).setBody("{\"error\":\"false\",\"response\":[{\"id\":1,\"game\":\"ETS2\",\"ip\":\"37.187.170.151\",\"port\":42860,\"name\":\"Europe 1\",\"shortname\":\"EU #1\",\"online\":true,\"players\":1330,\"maxplayers\":2300,\"speedlimiter\":1},{\"id\":3,\"game\":\"ETS2\",\"ip\":\"191.101.3.39\",\"port\":42860,\"name\":\"United states\",\"shortname\":\"US #1\",\"online\":true,\"players\":19,\"maxplayers\":1000,\"speedlimiter\":1},{\"id\":4,\"game\":\"ETS2\",\"ip\":\"151.80.230.166\",\"port\":42880,\"name\":\"Europe 2\",\"shortname\":\"EU #2\",\"online\":true,\"players\":1728,\"maxplayers\":2600,\"speedlimiter\":0},{\"id\":5,\"game\":\"ETS2\",\"ip\":\"37.187.170.151\",\"port\":42890,\"name\":\"Europe 3\",\"shortname\":\"EU #3\",\"online\":true,\"players\":338,\"maxplayers\":2300,\"speedlimiter\":1},{\"id\":6,\"game\":\"ETS2\",\"ip\":\"128.199.155.173\",\"port\":42860,\"name\":\"Asia\",\"shortname\":\"AS #1\",\"online\":true,\"players\":7,\"maxplayers\":500,\"speedlimiter\":1},{\"id\":7,\"game\":\"ETS2\",\"ip\":\"181.41.220.214\",\"port\":42860,\"name\":\"South America\",\"shortname\":\"SA #1\",\"online\":true,\"players\":141,\"maxplayers\":1500,\"speedlimiter\":0},{\"id\":9,\"game\":\"ATS\",\"ip\":\"37.187.170.151\",\"port\":42850,\"name\":\"Europe #1\",\"shortname\":\"EU #1\",\"online\":false,\"players\":0,\"maxplayers\":2000,\"speedlimiter\":1}]}");
                    case "/game_time/":
                        return new MockResponse().setResponseCode(200).setBody("{\"error\":false,\"game_time\":874703}");
                    case "/meetups/":
                        return new MockResponse().setResponseCode(200).setBody("{\"error\":false,\"items\":[{\"server\":\"ATS EU#1\",\"time\":\"in 30 minutes\",\"location\":\"Las Vegas, Service Station\",\"organiser\":\"[FCP][A]DAVID\",\"language\":\"English\",\"participants\":\"1 (+ 1)\",\"relativeURL\":\"view\\/45417\\/fcpadavid-las-vegas-service-station\"},{\"server\":\"ETS2 EU#1\",\"time\":\"in 30 minutes\",\"location\":\"Poznan, Hotel\",\"organiser\":\"[ConSecGroup-CEO] volvotrebla\",\"language\":\"English\",\"participants\":\"17 (+ 2)\",\"relativeURL\":\"view\\/45461\\/consecgroup-ceo-volvotrebla-poznan-hotel\"},{\"server\":\"ETS2 EU#2\",\"time\":\"in 30 minutes\",\"location\":\"Paris, Hotel\",\"organiser\":\"[STEFlogistics]routier61 ;)\",\"language\":\"Fran\\u00e7ais\",\"participants\":\"2 \",\"relativeURL\":\"view\\/45467\\/steflogisticsroutier61-paris-hotel\"},{\"server\":\"ETS2 EU#2\",\"time\":\"in 30 minutes\",\"location\":\"Wien, Service Station\",\"organiser\":\"[\\u00d6Z DOSTLAR] ONUR-18\",\"language\":\"T\\u00fcrk\\u00e7e\",\"participants\":\"33 (+ 2)\",\"relativeURL\":\"view\\/45217\\/z-dostlar-onur-18-wien-service-station\"},{\"server\":\"ETS2 EU#2\",\"time\":\"in 1 hour\",\"location\":\"Luxembourg, Hotel\",\"organiser\":\"Euro transport FR Patron S\\u00e9b\",\"language\":\"Fran\\u00e7ais\",\"participants\":\"5 (+ 1)\",\"relativeURL\":\"view\\/45463\\/euro-transport-fr-patron-sb-luxembourg-hotel\"},{\"server\":\"ETS2 EU#1\",\"time\":\"in 2 hours\",\"location\":\"Calais, Service Station\",\"organiser\":\"[ETI \\u00a9] ItsYoungGun [Owner]\",\"language\":\"English\",\"participants\":\"3 \",\"relativeURL\":\"view\\/45376\\/eti-c-itsyounggun-owner-calais-service-station\"},{\"server\":\"ETS2 EU#2\",\"time\":\"in 2 hours\",\"location\":\"Paris, Service Station\",\"organiser\":\"_TheMazer_\",\"language\":\"Deutsch\",\"participants\":\"2 \",\"relativeURL\":\"view\\/45452\\/-themazer-paris-service-station\"},{\"server\":\"ETS2 EU#2\",\"time\":\"in 2 hours\",\"location\":\"Uppsala, Service Station\",\"organiser\":\"[MSG] Xiggy\",\"language\":\"English\",\"participants\":\"3 \",\"relativeURL\":\"view\\/45464\\/msg-xiggy-uppsala-service-station\"},{\"server\":\"ETS2 EU#1\",\"time\":\"in 2 hours\",\"location\":\"Paris, Service Station\",\"organiser\":\"[KETS2I] - Md Pasek\",\"language\":\"Melayu\",\"participants\":\"4 (+ 4)\",\"relativeURL\":\"view\\/45259\\/kets2i-md-pasek-paris-service-station\"},{\"server\":\"ETS2 EU#3\",\"time\":\"in 2 hours\",\"location\":\"SFH EASTER EGG HUNT - Dover, Port\",\"organiser\":\"\\u25cf Soarfly Events Management \\u25cf\",\"language\":\"Doesn't matter\",\"participants\":\"16 (+ 7)\",\"relativeURL\":\"view\\/44754\\/-soarfly-events-management-sfh-easter-egg-hunt-dover-port\"},{\"server\":\"ETS2 EU#2\",\"time\":\"in 3 hours\",\"location\":\"Paris, Hotel\",\"organiser\":\"TheUltimateReal\",\"language\":\"Fran\\u00e7ais\",\"participants\":\"1 \",\"relativeURL\":\"view\\/45468\\/theultimatereal-paris-hotel\"},{\"server\":\"ETS2 EU#2\",\"time\":\"in 6 hours\",\"location\":\"Calais, Sea Port\",\"organiser\":\"MD chavez\",\"language\":\"Doesn't matter\",\"participants\":\"3 \",\"relativeURL\":\"view\\/44969\\/md-chavez-calais-sea-port\"},{\"server\":\"ETS2 EU#1\",\"time\":\"in 14 hours\",\"location\":\"Bergen, Service Station\",\"organiser\":\"Johannern96 on PS4 (NOR)\",\"language\":\"Norsk\",\"participants\":\"1 (+ 1)\",\"relativeURL\":\"view\\/43469\\/johannern96-on-ps4-nor-bergen-service-station\"},{\"server\":\"ETS2 EU#1\",\"time\":\"in 18 hours\",\"location\":\"Berlin, Truck Dealer\",\"organiser\":\"[MXP IC] Mimmo_ITA\\u2122\",\"language\":\"Italiano\",\"participants\":\"1 (+ 1)\",\"relativeURL\":\"view\\/45386\\/mxp-ic-mimmo-itatm-berlin-truck-dealer\"},{\"server\":\"ETS2 EU#1\",\"time\":\"in 18 hours\",\"location\":\"Cardiff, Hotel\",\"organiser\":\"[TSRVTC - Driver] Bradleyj307\",\"language\":\"Doesn't matter\",\"participants\":\"1 (+ 1)\",\"relativeURL\":\"view\\/45052\\/tsrvtc-driver-bradleyj307-cardiff-hotel\"},{\"server\":\"ETS2 UE # 1\",\"time\":\"in 19 hours\",\"location\":\"Paris, Bus Station\",\"organiser\":\"StormzKillaz\",\"language\":\"Fran\\u00e7ais\",\"participants\":\"2 \",\"relativeURL\":\"view\\/45334\\/stormzkillaz-paris-bus-station\"},{\"server\":\"ETS2 EU#2\",\"time\":\"in 19 hours\",\"location\":\"Remembrance Day to Belgium, Groningen, Hotel\",\"organiser\":\"[Aria.173891] Glasplaat25\",\"language\":\"Nederlands\",\"participants\":\"4 (+ 2)\",\"relativeURL\":\"view\\/45286\\/aria173891-glasplaat25-remembrance-day-to-belgium-groningen-hotel\"},{\"server\":\"ETS2 EU#2\",\"time\":\"in 20 hours\",\"location\":\"Paris, Service Station\",\"organiser\":\"[E&D Logistics.] TRUCK PILOT\",\"language\":\"English\",\"participants\":\"1 \",\"relativeURL\":\"view\\/45462\\/ed-logistics-truck-pilot-paris-service-station\"},{\"server\":\"ATS EU#1\",\"time\":\"in 21 hours\",\"location\":\"Sacramento, Service Station\",\"organiser\":\"Sgt. Pepper\",\"language\":\"English\",\"participants\":\"5 (+ 2)\",\"relativeURL\":\"view\\/45240\\/sgt-pepper-sacramento-service-station\"},{\"server\":\"ETS2 EU#2\",\"time\":\"in 22 hours\",\"location\":\"Amsterdam, Bus Station\",\"organiser\":\"LP_pro\",\"language\":\"English\",\"participants\":\"2 \",\"relativeURL\":\"view\\/45359\\/lp-pro-amsterdam-bus-station\"},{\"server\":\"EU2 ATS\",\"time\":\"in 23 hours\",\"location\":\"Redding, Wallbert\",\"organiser\":\"[TSRVTC - Owner] iHolby\",\"language\":\"Doesn't matter\",\"participants\":\"13 (+ 8)\",\"relativeURL\":\"view\\/42521\\/tsrvtc-owner-iholby-redding-wallbert\"},{\"server\":\"ETS2 EU#2\",\"time\":\"in 23 hours\",\"location\":\"Warszawa, Service Station\",\"organiser\":\"juggerNaultRyan\",\"language\":\"Doesn't matter\",\"participants\":\"2 \",\"relativeURL\":\"view\\/45285\\/juggernaultryan-warszawa-service-station\"},{\"server\":\"EU #1\",\"time\":\"in 1 day\",\"location\":\"Birmingham, Service Station\",\"organiser\":\"unn4r\",\"language\":\"Polski\",\"participants\":\"4 (+ 3)\",\"relativeURL\":\"view\\/41303\\/unn4r-birmingham-service-station\"},{\"server\":\"EU #1\",\"time\":\"in 1 day\",\"location\":\"Birmingham, Service Station\",\"organiser\":\"unn4r\",\"language\":\"Polski\",\"participants\":\"2 (+ 2)\",\"relativeURL\":\"view\\/41304\\/unn4r-birmingham-service-station\"},{\"server\":\"ETS2 EU#2\",\"time\":\"in 2 days\",\"location\":\"Luxembourg, Hotel\",\"organiser\":\"Euro transport FR Patron S\\u00e9b\",\"language\":\"Fran\\u00e7ais\",\"participants\":\"4 (+ 2)\",\"relativeURL\":\"view\\/45421\\/euro-transport-fr-patron-sb-luxembourg-hotel\"},{\"server\":\"ETS2 EU#2\",\"time\":\"in 2 days\",\"location\":\"Rotterdam, Hotel\",\"organiser\":\"gasmifares\",\"language\":\"Nederlands\",\"participants\":\"4 (+ 3)\",\"relativeURL\":\"view\\/44102\\/gasmifares-rotterdam-hotel\"},{\"server\":\"ATS EU#2\",\"time\":\"in 2 days\",\"location\":\"Oxnard, Chems\",\"organiser\":\"[TSRVTC - Driver] Mirrland\",\"language\":\"English\",\"participants\":\"4 (+ 2)\",\"relativeURL\":\"view\\/45413\\/tsrvtc-driver-mirrland-oxnard-chems\"},{\"server\":\"EU #1\",\"time\":\"in 3 days\",\"location\":\"Poznan, Service Station\",\"organiser\":\"ksyt14\",\"language\":\"Polski\",\"participants\":\"13 (+ 8)\",\"relativeURL\":\"view\\/37647\\/ksyt14-poznan-service-station\"},{\"server\":\"ETS2 EU#2\",\"time\":\"in 3 days\",\"location\":\"Malm\\u00f6, Service Station\",\"organiser\":\"YellowDay_HD\",\"language\":\"Deutsch\",\"participants\":\"1 (+ 1)\",\"relativeURL\":\"view\\/45401\\/yellowday-hd-malm-service-station\"},{\"server\":\"ETS2 EU#2\",\"time\":\"in 4 days\",\"location\":\"London, Bus Station\",\"organiser\":\"TTR | Warren | EM\",\"language\":\"English\",\"participants\":\"2 \",\"relativeURL\":\"view\\/44908\\/ttr-warren-em-london-bus-station\"},{\"server\":\"ETS2 EU#1\",\"time\":\"in 4 days\",\"location\":\"Hirtshals, Sea Port\",\"organiser\":\"StormSniper343\",\"language\":\"English\",\"participants\":\"1 \",\"relativeURL\":\"view\\/44153\\/stormsniper343-hirtshals-sea-port\"},{\"server\":\"ETS2 EU#2\",\"time\":\"in 4 days\",\"location\":\"Kiel, Euro Goodies\",\"organiser\":\"[Aria.173891] Glasplaat25\",\"language\":\"Nederlands\",\"participants\":\"1 \",\"relativeURL\":\"view\\/44886\\/aria173891-glasplaat25-kiel-euro-goodies\"},{\"server\":\"ETS2 EU#1\",\"time\":\"in 4 days\",\"location\":\"Aberdeen, Service Station\",\"organiser\":\"tipas\",\"language\":\"Doesn't matter\",\"participants\":\"1 \",\"relativeURL\":\"view\\/45268\\/tipas-aberdeen-service-station\"},{\"server\":\"ATS EU#1\",\"time\":\"in 4 days\",\"location\":\"Las Vegas, Print 42\",\"organiser\":\"DJ Bob (TruckSimRadio.com)\",\"language\":\"Doesn't matter\",\"participants\":\"1 \",\"relativeURL\":\"view\\/45332\\/dj-bob-trucksimradiocom-las-vegas-print-42\"},{\"server\":\"ETS2 EU#2\",\"time\":\"in 5 days\",\"location\":\"G\\u00f6teborg, Sea Port\",\"organiser\":\"[TSRVTC - Driver] Mirrland\",\"language\":\"English\",\"participants\":\"2 \",\"relativeURL\":\"view\\/45444\\/tsrvtc-driver-mirrland-gteborg-sea-port\"},{\"server\":\"ETS2 EU#1\",\"time\":\"in 6 days\",\"location\":\"Aberdeen, Service Station\",\"organiser\":\"Sgt. Pepper\",\"language\":\"English\",\"participants\":\"15 (+ 5)\",\"relativeURL\":\"view\\/44870\\/sgt-pepper-aberdeen-service-station\"},{\"server\":\"ETS2 EU#2\",\"time\":\"in 6 days\",\"location\":\"Dortmund, Hotel\",\"organiser\":\"Chrissix\",\"language\":\"Doesn't matter\",\"participants\":\"1 \",\"relativeURL\":\"view\\/44654\\/chrissix-dortmund-hotel\"},{\"server\":\"ETS2 EU#1\",\"time\":\"in 6 days\",\"location\":\"Aberdeen, Hotel\",\"organiser\":\"[T-M-Trans] xafra92-HC88[BE]\",\"language\":\"Fran\\u00e7ais\",\"participants\":\"2 (+ 2)\",\"relativeURL\":\"view\\/45172\\/t-m-trans-xafra92-hc88be-aberdeen-hotel\"},{\"server\":\"ETS2 EU#1\",\"time\":\"in 6 days\",\"location\":\"Reims, Hotel\",\"organiser\":\"R\\u00e9my.66 [FR]\",\"language\":\"Fran\\u00e7ais\",\"participants\":\"1 (+ 2)\",\"relativeURL\":\"view\\/44763\\/rmy66-fr-reims-hotel\"},{\"server\":\"EU #1\",\"time\":\"in 7 days\",\"location\":\"Aalborg, Bus Station\",\"organiser\":\"[T\\u00fcrkiyeLojistik]EfeToprak\",\"language\":\"T\\u00fcrk\\u00e7e\",\"participants\":\"8 (+ 2)\",\"relativeURL\":\"view\\/43101\\/trkiyelojistikefetoprak-aalborg-bus-station\"},{\"server\":\"ETS2 EU#2\",\"time\":\"in 7 days\",\"location\":\"Hirtshals, Sea Port\",\"organiser\":\"StormSniper343\",\"language\":\"English\",\"participants\":\"1 \",\"relativeURL\":\"view\\/44152\\/stormsniper343-hirtshals-sea-port\"},{\"server\":\"ETS2 EU#2\",\"time\":\"in 7 days\",\"location\":\"RTLVTC TruckFest @ Mannheim, Meet @ CarPark Next To EuroGoodies\",\"organiser\":\"[RTLVTC - Owner] Ray\",\"language\":\"English\",\"participants\":\"9 \",\"relativeURL\":\"view\\/45105\\/rtlvtc-owner-ray-rtlvtc-truckfest-mannheim-meet-carpark-next-to-eurogoodies\"},{\"server\":\"ETS2 EU#1\",\"time\":\"in 7 days\",\"location\":\"Bergen, Hotel\",\"organiser\":\"[CGG] Eastside \\u00ae\",\"language\":\"Deutsch\",\"participants\":\"4 \",\"relativeURL\":\"view\\/45226\\/cgg-eastside-r-bergen-hotel\"},{\"server\":\"ETS2 EU#2\",\"time\":\"in 7 days\",\"location\":\"Gda?sk, Sea Port (North)\",\"organiser\":\"[TSRVTC - Driver] Mirrland\",\"language\":\"English\",\"participants\":\"8 (+ 2)\",\"relativeURL\":\"view\\/44991\\/tsrvtc-driver-mirrland-gdask-sea-port-north\"},{\"server\":\"ATS EU#1\",\"time\":\"in 7 days\",\"location\":\"Las Vegas, Hotel\",\"organiser\":\"[T-M-Trans] xafra92-HC88[BE]\",\"language\":\"Fran\\u00e7ais\",\"participants\":\"1 (+ 2)\",\"relativeURL\":\"view\\/45173\\/t-m-trans-xafra92-hc88be-las-vegas-hotel\"},{\"server\":\"ETS2 EU#2\",\"time\":\"in 1 week\",\"location\":\"Calais, Service Station\",\"organiser\":\"[I.T.B] {G} HELDER.C\\u2122 (BR)\",\"language\":\"Portugu\\u00eas\",\"participants\":\"5 \",\"relativeURL\":\"view\\/45109\\/itb-g-helderctm-br-calais-service-station\"},{\"server\":\"ETS2 EU#2\",\"time\":\"in 1 week\",\"location\":\"Rotterdam, Service Station\",\"organiser\":\"KJP12\",\"language\":\"English\",\"participants\":\"1 \",\"relativeURL\":\"view\\/45297\\/kjp12-rotterdam-service-station\"},{\"server\":\"ETS2 EU#1\",\"time\":\"in 1 week\",\"location\":\"Frankfurt, Truck Dealer\",\"organiser\":\"[MXP IC] Mimmo_ITA\\u2122\",\"language\":\"Italiano\",\"participants\":\"1 \",\"relativeURL\":\"view\\/45384\\/mxp-ic-mimmo-itatm-frankfurt-truck-dealer\"},{\"server\":\"ETS2 EU#3\",\"time\":\"in 1 week\",\"location\":\"Milano, Hotel\",\"organiser\":\"[EXTRA]claudio[PT]\",\"language\":\"Portugu\\u00eas\",\"participants\":\"2 (+ 2)\",\"relativeURL\":\"view\\/45454\\/extraclaudiopt-milano-hotel\"},{\"server\":\"ETS2 EU#2\",\"time\":\"in 1 week\",\"location\":\"\\u00d6rebro, Norrsken\",\"organiser\":\"DJ Bob (TruckSimRadio.com)\",\"language\":\"Doesn't matter\",\"participants\":\"2 \",\"relativeURL\":\"view\\/45333\\/dj-bob-trucksimradiocom-rebro-norrsken\"},{\"server\":\"ETS2 EU#2\",\"time\":\"in 1 week\",\"location\":\"Paris, Hotel\",\"organiser\":\"[RTVTC-Driver] franticdan22\",\"language\":\"English\",\"participants\":\"1 \",\"relativeURL\":\"view\\/45416\\/rtvtc-driver-franticdan22-paris-hotel\"},{\"server\":\"ETS2 EU#2\",\"time\":\"in 2 weeks\",\"location\":\"Esbjerg, AgroNord\",\"organiser\":\"[TSRVTC - Driver] Mirrland\",\"language\":\"English\",\"participants\":\"1 \",\"relativeURL\":\"view\\/45447\\/tsrvtc-driver-mirrland-esbjerg-agronord\"},{\"server\":\"EU #1\",\"time\":\"in 2 weeks\",\"location\":\"Amsterdam, Bus Station\",\"organiser\":\"michael.van.der.vegt\",\"language\":\"Doesn't matter\",\"participants\":\"1 \",\"relativeURL\":\"view\\/42431\\/michaelvandervegt-amsterdam-bus-station\"},{\"server\":\"EU #2\",\"time\":\"in 2 weeks\",\"location\":\"Munchen, Hotel\",\"organiser\":\"(GER&HUN)AQUILA-ChrisRobin\",\"language\":\"Doesn't matter\",\"participants\":\"1 (+ 1)\",\"relativeURL\":\"view\\/42133\\/gerhunaquila-chrisrobin-munchen-hotel\"},{\"server\":\"ATS EU#2\",\"time\":\"in 2 weeks\",\"location\":\"Las Vegas, Service Station\",\"organiser\":\"[MG-Westerwald] Cronka\",\"language\":\"Deutsch\",\"participants\":\"1 \",\"relativeURL\":\"view\\/45253\\/mg-westerwald-cronka-las-vegas-service-station\"},{\"server\":\"ETS2 EU#1\",\"time\":\"in 2 weeks\",\"location\":\"Koln, Bus Station\",\"organiser\":\"[modcon Chef] Tobs [DE]\",\"language\":\"Deutsch\",\"participants\":\"2 \",\"relativeURL\":\"view\\/45456\\/modcon-chef-tobs-de-koln-bus-station\"},{\"server\":\"ETS2 EU#1\",\"time\":\"in 2 weeks\",\"location\":\"Bratislava, WGCC\",\"organiser\":\"Kecap\",\"language\":\"Very Welcome Any Language\",\"participants\":\"6 (+ 5)\",\"relativeURL\":\"view\\/45235\\/kecap-bratislava-wgcc\"},{\"server\":\"ETS2 EU#1\",\"time\":\"in 3 weeks\",\"location\":\"Groningen, EAcres\",\"organiser\":\"[TSRVTC - Driver] Mirrland\",\"language\":\"English\",\"participants\":\"1 \",\"relativeURL\":\"view\\/45448\\/tsrvtc-driver-mirrland-groningen-eacres\"},{\"server\":\"ATS EU#2\",\"time\":\"in 3 weeks\",\"location\":\"LKW 1 YEAR ANNIVERSARY, Hornbrook\",\"organiser\":\"[LKW Tr.] Elliot [COO]\",\"language\":\"English\",\"participants\":\"25 (+ 7)\",\"relativeURL\":\"view\\/44671\\/lkw-tr-elliot-coo-lkw-1-year-anniversary-hornbrook\"},{\"server\":\"ATS EU#1\",\"time\":\"in 3 weeks\",\"location\":\"Los Angeles, Truck Dealer\",\"organiser\":\"[FR\\/VN] Julien nry\",\"language\":\"Fran\\u00e7ais\",\"participants\":\"1 (+ 1)\",\"relativeURL\":\"view\\/44550\\/fr-vn-julien-nry-los-angeles-truck-dealer\"},{\"server\":\"ETS2 EU#1\",\"time\":\"in 3 weeks\",\"location\":\"Bergen, Sea Port\",\"organiser\":\"[FR\\/VN] Julien nry\",\"language\":\"Fran\\u00e7ais\",\"participants\":\"1 \",\"relativeURL\":\"view\\/44551\\/fr-vn-julien-nry-bergen-sea-port\"},{\"server\":\"ETS2 EU#1\",\"time\":\"in 4 weeks\",\"location\":\"Paris, Hotel\",\"organiser\":\"_KFAYTA07_\",\"language\":\"Doesn't matter\",\"participants\":\"1 \",\"relativeURL\":\"view\\/45279\\/-kfayta07-paris-hotel\"},{\"server\":\"EU #2\",\"time\":\"in 4 weeks\",\"location\":\"Calais, Hotel\",\"organiser\":\"(GER&HUN)AQUILA-ChrisRobin\",\"language\":\"Doesn't matter\",\"participants\":\"1 (+ 1)\",\"relativeURL\":\"view\\/42134\\/gerhunaquila-chrisrobin-calais-hotel\"},{\"server\":\"ETS2 EU#1\",\"time\":\"in 4 weeks\",\"location\":\"Stockholm, Hotel\",\"organiser\":\"[FR\\/VN] Julien nry\",\"language\":\"Fran\\u00e7ais\",\"participants\":\"1 \",\"relativeURL\":\"view\\/44889\\/fr-vn-julien-nry-stockholm-hotel\"},{\"server\":\"ATS EU#1\",\"time\":\"in 4 weeks\",\"location\":\"San Francisco, Truck Dealer\",\"organiser\":\"[FR\\/VN] Julien nry\",\"language\":\"Fran\\u00e7ais\",\"participants\":\"1 \",\"relativeURL\":\"view\\/44890\\/fr-vn-julien-nry-san-francisco-truck-dealer\"},{\"server\":\"ATS EU#1\",\"time\":\"in 1 month\",\"location\":\"Elko, Service Station\",\"organiser\":\"Gamemaster\",\"language\":\"Deutsch\",\"participants\":\"1 \",\"relativeURL\":\"view\\/45407\\/gamemaster-elko-service-station\"},{\"server\":\"ETS2 EU#2\",\"time\":\"in 1 month\",\"location\":\"Calais, Bus Station\",\"organiser\":\"[RTLVTC - Owner] Ray\",\"language\":\"English\",\"participants\":\"3 \",\"relativeURL\":\"view\\/44167\\/rtlvtc-owner-ray-calais-bus-station\"},{\"server\":\"ETS2 EU#1\",\"time\":\"in 1 month\",\"location\":\"Bremen, Service Station\",\"organiser\":\"Rumpel011 ( GER )\",\"language\":\"Deutsch\",\"participants\":\"6 \",\"relativeURL\":\"view\\/45436\\/rumpel011-ger-bremen-service-station\"},{\"server\":\"ETS2 EU#1\",\"time\":\"in 1 month\",\"location\":\"Budapest, Bus Station\",\"organiser\":\"[GVA] Maik\",\"language\":\"Deutsch\",\"participants\":\"2 \",\"relativeURL\":\"view\\/43908\\/gva-maik-budapest-bus-station\"},{\"server\":\"EU #1\",\"time\":\"in 1 month\",\"location\":\"Newcastle, Service Station\",\"organiser\":\"LILLESKUTT\",\"language\":\"Doesn't matter\",\"participants\":\"1 (+ 1)\",\"relativeURL\":\"view\\/41496\\/lilleskutt-newcastle-service-station\"},{\"server\":\"ETS2 AB 2.\",\"time\":\"in 1 month\",\"location\":\"Berlin, Hotel\",\"organiser\":\"Mert {Kacak\\u00e7\\u0131lar lojistik}\",\"language\":\"Bahasa\",\"participants\":\"1 \",\"relativeURL\":\"view\\/44445\\/mert-kacaklar-lojistik-berlin-hotel\"},{\"server\":\"ATS EU#1\",\"time\":\"in 1 month\",\"location\":\"Oxnard, Hotel\",\"organiser\":\"[FR\\/VN] Julien nry\",\"language\":\"Fran\\u00e7ais\",\"participants\":\"1 \",\"relativeURL\":\"view\\/44891\\/fr-vn-julien-nry-oxnard-hotel\"},{\"server\":\"ETS2 EU#2\",\"time\":\"in 2 months\",\"location\":\"Dresden, Truck Dealer\",\"organiser\":\"[RO]Transport ADNVampireRO\",\"language\":\"Rom\\u00e2n\\u0103\",\"participants\":\"1 \",\"relativeURL\":\"view\\/44790\\/rotransport-adnvampirero-dresden-truck-dealer\"},{\"server\":\"EU #2\",\"time\":\"in 2 months\",\"location\":\"Munchen, Hotel\",\"organiser\":\"(GER&HUN)AQUILA-ChrisRobin\",\"language\":\"Doesn't matter\",\"participants\":\"1 (+ 1)\",\"relativeURL\":\"view\\/42136\\/gerhunaquila-chrisrobin-munchen-hotel\"},{\"server\":\"ATS EU#2\",\"time\":\"in 2 months\",\"location\":\"Los Angeles, Service Station\",\"organiser\":\"[RTLVTC - Owner] Ray\",\"language\":\"English\",\"participants\":\"1 \",\"relativeURL\":\"view\\/43540\\/rtlvtc-owner-ray-los-angeles-service-station\"},{\"server\":\"ATS EU#2\",\"time\":\"in 2 months\",\"location\":\"Los Angeles, Service Station\",\"organiser\":\"[RTLVTC - Owner] Ray\",\"language\":\"English\",\"participants\":\"1 \",\"relativeURL\":\"view\\/43534\\/rtlvtc-owner-ray-los-angeles-service-station\"},{\"server\":\"ATS EU#2\",\"time\":\"in 2 months\",\"location\":\"Los Angeles, Service Station\",\"organiser\":\"[RTLVTC - Owner] Ray\",\"language\":\"English\",\"participants\":\"1 \",\"relativeURL\":\"view\\/43535\\/rtlvtc-owner-ray-los-angeles-service-station\"},{\"server\":\"ATS EU#2\",\"time\":\"in 2 months\",\"location\":\"Los Angeles, Service Station\",\"organiser\":\"[RTLVTC - Owner] Ray\",\"language\":\"English\",\"participants\":\"1 \",\"relativeURL\":\"view\\/43536\\/rtlvtc-owner-ray-los-angeles-service-station\"},{\"server\":\"ATS EU#2\",\"time\":\"in 2 months\",\"location\":\"Los Angeles, Service Station\",\"organiser\":\"[RTLVTC - Owner] Ray\",\"language\":\"English\",\"participants\":\"1 \",\"relativeURL\":\"view\\/43538\\/rtlvtc-owner-ray-los-angeles-service-station\"},{\"server\":\"ATS EU#2\",\"time\":\"in 2 months\",\"location\":\"Los Angeles, Service Station\",\"organiser\":\"[RTLVTC - Owner] Ray\",\"language\":\"English\",\"participants\":\"1 \",\"relativeURL\":\"view\\/43539\\/rtlvtc-owner-ray-los-angeles-service-station\"},{\"server\":\"EU #2\",\"time\":\"in 2 months\",\"location\":\"Berlin, Hotel\",\"organiser\":\"(GER&HUN)AQUILA-ChrisRobin\",\"language\":\"Doesn't matter\",\"participants\":\"1 (+ 1)\",\"relativeURL\":\"view\\/42137\\/gerhunaquila-chrisrobin-berlin-hotel\"},{\"server\":\"ATS EU#1\",\"time\":\"in 2 months\",\"location\":\"Los Angeles, Service Station\",\"organiser\":\"DFT | forzalover\",\"language\":\"English\",\"participants\":\"1 \",\"relativeURL\":\"view\\/44089\\/dft-forzalover-los-angeles-service-station\"},{\"server\":\"EU #2\",\"time\":\"in 2 months\",\"location\":\"Calais, Hotel\",\"organiser\":\"(GER&HUN)AQUILA-ChrisRobin\",\"language\":\"Doesn't matter\",\"participants\":\"1 (+ 1)\",\"relativeURL\":\"view\\/42138\\/gerhunaquila-chrisrobin-calais-hotel\"},{\"server\":\"EU #1\",\"time\":\"in 3 months\",\"location\":\"Poznan, Hotel\",\"organiser\":\"ryba.04\",\"language\":\"Polski\",\"participants\":\"1 (+ 1)\",\"relativeURL\":\"view\\/40659\\/ryba04-poznan-hotel\"},{\"server\":\"EU #2\",\"time\":\"in 3 months\",\"location\":\"Linz, Hotel\",\"organiser\":\"(GER&HUN)AQUILA-ChrisRobin\",\"language\":\"Doesn't matter\",\"participants\":\"1 (+ 1)\",\"relativeURL\":\"view\\/42139\\/gerhunaquila-chrisrobin-linz-hotel\"},{\"server\":\"ETS2 EU#1\",\"time\":\"in 3 months\",\"location\":\"Nurnberg, Truck Dealer\",\"organiser\":\"burakozdogan\",\"language\":\"T\\u00fcrk\\u00e7e\",\"participants\":\"2 (+ 1)\",\"relativeURL\":\"view\\/44565\\/burakozdogan-nurnberg-truck-dealer\"},{\"server\":\"US #1\",\"time\":\"in 3 months\",\"location\":\"Luxembourg, Truck Dealer\",\"organiser\":\"JLinde13\",\"language\":\"English\",\"participants\":\"1 \",\"relativeURL\":\"view\\/42412\\/jlinde13-luxembourg-truck-dealer\"},{\"server\":\"EU #2\",\"time\":\"in 4 months\",\"location\":\"Calais, Bus Station\",\"organiser\":\"vallec50\",\"language\":\"Fran\\u00e7ais\",\"participants\":\"2 (+ 7)\",\"relativeURL\":\"view\\/36682\\/vallec50-calais-bus-station\"},{\"server\":\"ETS2 EU#2\",\"time\":\"in 8 months\",\"location\":\"Aberdeen, Hotel\",\"organiser\":\"uuu100145j (Convoyer)\",\"language\":\"Doesn't matter\",\"participants\":\"1 \",\"relativeURL\":\"view\\/44985\\/uuu100145j-convoyer-aberdeen-hotel\"},{\"server\":\"EU #2\",\"time\":\"in 8 months\",\"location\":\"Amsterdam, Hotel\",\"organiser\":\"(TR)JBP\\u2122 | BigBoss\",\"language\":\"T\\u00fcrk\\u00e7e\",\"participants\":\"5 (+ 1)\",\"relativeURL\":\"view\\/42054\\/trjbptm-bigboss-amsterdam-hotel\"},{\"server\":\"EU #2\",\"time\":\"in 9 months\",\"location\":\"Berlin, Truck Dealer\",\"organiser\":\"Arschi 2.0\",\"language\":\"Doesn't matter\",\"participants\":\"1 (+ 1)\",\"relativeURL\":\"view\\/38091\\/arschi-20-berlin-truck-dealer\"},{\"server\":\"ETS2 EU#2\",\"time\":\"in 12 months\",\"location\":\"Debrecen, Service Station\",\"organiser\":\"[RO]Emanuelalex\",\"language\":\"Doesn't matter\",\"participants\":\"1 \",\"relativeURL\":\"view\\/45450\\/roemanuelalex-debrecen-service-station\"},{\"server\":\"ETS2 EU#2\",\"time\":\"in 4 years\",\"location\":\"Carlisle, Service Station\",\"organiser\":\"MakegoBg\",\"language\":\"\\u010ce\\u0161tina\",\"participants\":\"5 (+ 3)\",\"relativeURL\":\"view\\/43807\\/makegobg-carlisle-service-station\"},{\"server\":\"ETS2 EU#1\",\"time\":\"in 16 years\",\"location\":\"Amsterdam, Hotel\",\"organiser\":\"\\u219cMasse\\u30e0\\u219d#Vacbanned\",\"language\":\"Doesn't matter\",\"participants\":\"4 \",\"relativeURL\":\"view\\/44594\\/massevacbanned-amsterdam-hotel\"},{\"server\":\"How do I know\",\"time\":\"in 22 years\",\"location\":\"I don't really know, any location\",\"organiser\":\".....\",\"language\":\"Dunno\",\"participants\":\"113 (+ 14)\",\"relativeURL\":\"view\\/43520\\/i-dont-really-know-any-location\"}]}");
                }
                return new MockResponse().setResponseCode(404);
            }
        };

        dispatcherBad = new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                return new MockResponse().setResponseCode(404);
            }
        };

        mockServer.setDispatcher(dispatcherGood);

        mockServer.start(52734);

        activity = getActivity();
    }

    @Override
    @After
    protected void tearDown() throws Exception {
        mockServer.shutdown();

        super.tearDown();
    }

    public void testFragmentCollection() {
        List<Fragment> fragments = activity.getSupportFragmentManager().getFragments();

        // there have to be fragments
        assertNotNull(fragments);

        // 2 fragments, to be precise
        assertEquals(2, fragments.size());

        // the first one should be a MeetupListFragment
        assertTrue(fragments.get(0) instanceof MeetupListFragment);

        // and the second one a ServerListFragment
        assertTrue(fragments.get(1) instanceof ServerListFragment);
    }

    public void testViewPagerSwipe() {
        // the ViewPager must have precisely the following descendants
        onView(withId(R.id.viewpager)).check(matches(hasDescendant(withId(R.id.recyclerview_serverlist))));
        onView(withId(R.id.viewpager)).check(matches(hasDescendant(withId(R.id.recyclerview_meetuplist))));

        // now we test which of them is displayed before and after swiping
        onView(withId(R.id.recyclerview_serverlist)).check(matches(isDisplayed()));
        onView(withId(R.id.viewpager)).perform(swipeLeft());
        onView(withId(R.id.recyclerview_meetuplist)).check(matches(isDisplayed()));
        onView(withId(R.id.viewpager)).perform(swipeRight());
        onView(withId(R.id.recyclerview_serverlist)).check(matches(isDisplayed()));
    }

    public void testOptionsMenu() {
        // for every option menu entry we try to click it, check that the main Toolbar's title matches, and then go back
        for (int optionsMenuEntry : optionsMenuEntries) {
            openActionBarOverflowOrOptionsMenu(activity);

            onView(ViewMatchers.withText(optionsMenuEntry)).perform(click());
            onView(withId(cz.uruba.ets2mpcompanion.R.id.toolbar)).check(matches(withToolbarTitle(is(activity.getString(optionsMenuEntry)))));

            pressBack();
        }
    }

    public void testBadResponse() {
        // set the bad dispatcher to simulate server failure
        mockServer.setDispatcher(dispatcherBad);

        // we need to wait for the ViewPager's animation to finish when we swipe to another tab
        ViewPagerIdlingResource idlingRes = new ViewPagerIdlingResource((ViewPager) activity.findViewById(R.id.viewpager), "ViewPager");
        Espresso.registerIdlingResources(idlingRes);

        // we tap the FAB on the ServerListFragment (~ the currently displayed one)
        onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());
        // then we swipe left to the MeetupListFragment
        onView(withId(R.id.viewpager)).perform(swipeLeft());

        // and we tap the FAB once more there
        onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

        // re-set the dispatcher to the original, "good" state
        mockServer.setDispatcher(dispatcherGood);
        // unregister the idling resource we'd set previously
        Espresso.unregisterIdlingResources(idlingRes);
    }
}
