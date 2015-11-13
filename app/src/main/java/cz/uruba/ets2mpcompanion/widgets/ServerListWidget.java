package cz.uruba.ets2mpcompanion.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cz.uruba.ets2mpcompanion.R;
import cz.uruba.ets2mpcompanion.constants.URL;
import cz.uruba.ets2mpcompanion.interfaces.DataReceiverJSON;
import cz.uruba.ets2mpcompanion.model.ServerInfo;
import cz.uruba.ets2mpcompanion.tasks.FetchServerListTask;

public class ServerListWidget extends AppWidgetProvider {
    static final String ACTION_REFRESH = "cz.uruba.ets2mpcompanion.widgets.action.SERVERLIST_REFRESH";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] widgetIDs) {
        for (int widgetID : widgetIDs) {
            Intent intent = new Intent(context, WidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    widgetID);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews rv = new RemoteViews(context.getPackageName(),
                    R.layout.widget_serverlist);
            rv.setRemoteAdapter(R.id.widget_listview, intent);

            appWidgetManager.updateAppWidget(widgetID, rv);
        }
        super.onUpdate(context, appWidgetManager, widgetIDs);
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

        public ServerListWidgetRemoteViewsFactory(Context context, Intent intent) {
            this.context = context;
            widgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            try {
                serverList = new FetchServerListTask(this, URL.SERVER_LIST, false).execute().get();
                Collections.sort(serverList, Collections.reverseOrder());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDestroy() {
            serverList.clear();
        }

        @Override
        public int getCount() {
            return serverList.size();
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

        }

        @Override
        public Date getLastUpdated() {
            return null;
        }
    }
}
