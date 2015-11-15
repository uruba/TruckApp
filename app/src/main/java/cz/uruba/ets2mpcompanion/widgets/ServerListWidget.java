package cz.uruba.ets2mpcompanion.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import cz.uruba.ets2mpcompanion.R;
import cz.uruba.ets2mpcompanion.constants.URL;
import cz.uruba.ets2mpcompanion.interfaces.DataReceiverJSON;
import cz.uruba.ets2mpcompanion.model.ServerInfo;
import cz.uruba.ets2mpcompanion.tasks.FetchServerListTask;
import cz.uruba.ets2mpcompanion.utils.UICompat;

public class ServerListWidget extends AppWidgetProvider {
    static final String ACTION_REFRESH = "cz.uruba.ets2mpcompanion.widgets.action.SERVERLIST_REFRESH";

    private static Map<Integer, RemoteViews> remoteViews = new HashMap<>();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] widgetIDs) {
        for (int widgetID : widgetIDs) {
            Intent intent = new Intent(context, WidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    widgetID);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews rv = new RemoteViews(context.getPackageName(),
                    R.layout.widget_serverlist);

            Intent updateIntent = new Intent(context, getClass());
            updateIntent.setAction(ACTION_REFRESH);
            updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
            rv.setOnClickPendingIntent(R.id.refresh, PendingIntent.getBroadcast(context, widgetID, updateIntent, PendingIntent.FLAG_UPDATE_CURRENT));

            rv.setRemoteAdapter(R.id.widget_listview, intent);

            appWidgetManager.updateAppWidget(widgetID, rv);
            remoteViews.put(widgetID, rv);
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

    public static class ServerListWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory, DataReceiverJSON<ArrayList<ServerInfo>> {
        Context context;

        int widgetID;

        List<ServerInfo> serverList = new ArrayList<>();

        boolean firstRun = true;

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
            try {
                serverList = new FetchServerListTask(this, URL.SERVER_LIST, false).execute().get();

                if (serverList == null) {
                    return;
                }

                Collections.sort(serverList, Collections.reverseOrder());
                RemoteViews rv = remoteViews.get(widgetID);
                rv.setTextViewText(R.id.last_updated, String.format(context.getString(R.string.as_of), new SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(new Date())));
                AppWidgetManager.getInstance(context).updateAppWidget(widgetID, rv);

                if (!firstRun) {
                    displayToast(context.getString(R.string.server_list_refreshed_widget));
                } else {
                    firstRun = false;
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDestroy() {
            if (serverList != null) {
                serverList.clear();
            }
        }

        @Override
        public int getCount() {
            return serverList == null ? 0 : serverList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews remoteView = new RemoteViews(context.getPackageName(),
                    R.layout.item_serverinfo_remoteview);

            ServerInfo serverInfo = serverList.get(position);

            remoteView.setTextViewText(R.id.server_name, serverInfo.getServerName());
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

        }

        @Override
        public void processData(ArrayList<ServerInfo> data, boolean notifyUser) {

        }

        @Override
        public void handleIOException(IOException e) {
            displayToast(context.getString(R.string.download_error));
        }

        @Override
        public Date getLastUpdated() {
            return null;
        }

        private void displayToast(final String text) {
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
