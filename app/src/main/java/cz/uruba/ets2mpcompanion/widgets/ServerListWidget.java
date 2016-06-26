package cz.uruba.ets2mpcompanion.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import cz.uruba.ets2mpcompanion.MainActivity;
import cz.uruba.ets2mpcompanion.R;
import cz.uruba.ets2mpcompanion.constants.URL;
import cz.uruba.ets2mpcompanion.fragments.SettingsFragment;
import cz.uruba.ets2mpcompanion.interfaces.DataReceiverJSON;
import cz.uruba.ets2mpcompanion.model.ServerInfo;
import cz.uruba.ets2mpcompanion.model.general.DataSet;
import cz.uruba.ets2mpcompanion.tasks.FetchServerListTask;
import cz.uruba.ets2mpcompanion.utils.UICompat;

public class ServerListWidget extends AppWidgetProvider {
    private static final String ACTION_REFRESH = "cz.uruba.ets2mpcompanion.widgets.action.SERVERLIST_REFRESH";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] widgetIDs) {
        for (int widgetID : widgetIDs) {
            appWidgetManager.updateAppWidget(widgetID, newRemoteViews(context, widgetID));
        }
        super.onUpdate(context, appWidgetManager, widgetIDs);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction().equals(ACTION_REFRESH)) {
            int widgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

            AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(widgetID, R.id.widget_listview);
        }
    }

    // TODO – Find out if there is a way not to recreate the whole RemoteViews object in the ServerListWidgetRemoteViewsFactory.onDataSetChanged() method
    private static RemoteViews newRemoteViews(Context context, int widgetID) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget_serverlist);

        Intent intent = new Intent(context, WidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                widgetID);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        remoteViews.setRemoteAdapter(R.id.widget_listview, intent);

        Intent updateIntent = new Intent(context, ServerListWidget.class);
        updateIntent.setAction(ACTION_REFRESH);
        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
        remoteViews.setOnClickPendingIntent(R.id.refresh, PendingIntent.getBroadcast(context, widgetID, updateIntent, PendingIntent.FLAG_UPDATE_CURRENT));

        Intent launchAppIntent = new Intent(context, MainActivity.class);
        remoteViews.setOnClickPendingIntent(R.id.title, PendingIntent.getActivity(context, 0, launchAppIntent, 0));
        remoteViews.setOnClickPendingIntent(R.id.last_updated, PendingIntent.getActivity(context, 0, launchAppIntent, 0));

        return remoteViews;
    }

    public static class WidgetService extends RemoteViewsService {

        @Override
        public RemoteViewsFactory onGetViewFactory(Intent intent) {
            return (new ServerListWidgetRemoteViewsFactory(getBaseContext(), intent));
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return super.onBind(intent);
        }
    }

    public static class ServerListWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory, DataReceiverJSON<DataSet<ServerInfo>> {
        Context context;

        int widgetID;

        DataSet<ServerInfo> serverList;

        public ServerListWidgetRemoteViewsFactory(Context context, Intent intent) {
            this.context = context;
            widgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
            UICompat.setOverscrollEffectColour(context);
        }

        @Override
        public void onDataSetChanged() {
            refreshRemoteViews(context.getString(R.string.widget_loading));

            try {
                serverList = new FetchServerListTask(this, URL.SERVER_LIST, false).execute().get().getResult();

                if (serverList == null) {
                    return;
                }

                Collections.sort(serverList.getCollection(), Collections.reverseOrder());

                refreshRemoteViews(String.format(context.getString(R.string.as_of), new SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(new Date())));

                displayToast(context.getString(R.string.server_list_refreshed_widget));
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDestroy() {
            if (serverList != null) {
                serverList.getCollection().clear();
            }
        }

        @Override
        public int getCount() {
            return serverList == null ? 0 : serverList.getCollection().size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews remoteView = new RemoteViews(context.getPackageName(),
                    R.layout.item_serverinfo_remoteview);

            ServerInfo serverInfo = serverList.getCollection().get(position);

            remoteView.setTextViewText(R.id.server_name, String.format(context.getString(R.string.widget_server_name), serverInfo.getServerName(), serverInfo.getGameName()));
            remoteView.setTextViewText(R.id.number_of_players, serverInfo.getFormattedPlayerCountString(context));
            remoteView.setProgressBar(
                    R.id.number_of_players_progressbar,
                    serverInfo.getPlayerCountCapacity(),
                    serverInfo.getPlayerCountCurrent(),
                    false);

            return remoteView;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public void handleJSONException(JSONException e) {
            refreshRemoteViews(context.getString(R.string.not_available_now));
            displayToast(context.getString(R.string.json_error));
        }

        @Override
        public void processData(DataSet<ServerInfo> data, boolean notifyUser) {

        }

        @Override
        public void handleIOException(IOException e) {
            refreshRemoteViews(context.getString(R.string.not_available_now));
            displayToast(context.getString(R.string.download_error));
        }

        @Override
        public Date getLastUpdated() {
            return null;
        }

        private void refreshRemoteViews(String lastUpdatedText) {
            RemoteViews remoteViews = ServerListWidget.newRemoteViews(context, widgetID);
            remoteViews.setTextViewText(R.id.last_updated, lastUpdatedText);
            AppWidgetManager.getInstance(context).updateAppWidget(widgetID, remoteViews);
        }

        private void displayToast(final String text) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            if(sharedPref.getBoolean(SettingsFragment.PREF_WIDGET_TOAST_ENABLED, false)) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context,
                                text, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }
}
