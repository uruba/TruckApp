package cz.uruba.ets2mpcompanion.widgets;

import android.app.Service;
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
import java.util.Date;
import java.util.List;

import cz.uruba.ets2mpcompanion.R;
import cz.uruba.ets2mpcompanion.constants.URL;
import cz.uruba.ets2mpcompanion.interfaces.DataReceiverJSON;
import cz.uruba.ets2mpcompanion.model.ServerInfo;
import cz.uruba.ets2mpcompanion.tasks.FetchServerListTask;

public class ServerListWidget extends AppWidgetProvider {
    private static final String ACTION_REFRESH = "cz.uruba.ets2mpcompanion.widgets.action.SERVERLIST_REFRESH";

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

    public static class UpdateServerListService extends Service {

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }

    public static class ServerListWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        Context context;

        int widgetID;

        List<ServerInfo> serverList;

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

            class CallBackHandler implements DataReceiverJSON<ArrayList<ServerInfo>> {
                List<ServerInfo> listToUpdate;

                public CallBackHandler(List<ServerInfo> listToUpdate) {
                    this.listToUpdate = listToUpdate;
                }

                @Override
                public void handleJSONException(JSONException e) {

                }

                @Override
                public void processData(ArrayList<ServerInfo> data, boolean notifyUser) {
                    listToUpdate = data;
                }

                @Override
                public void handleIOException(IOException e) {

                }

                @Override
                public Date getLastUpdated() {
                    return null;
                }
            }

            FetchServerListTask fetchServerList = new FetchServerListTask(new CallBackHandler(serverList), URL.SERVER_LIST, false);
            fetchServerList.execute();
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews remoteView = new RemoteViews(context.getPackageName(),
                    R.layout.item_serverinfo);

            ServerInfo serverInfo = serverList.get(position);

            remoteView.setTextViewText(R.id.server_name, serverInfo.getServerName());

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
            return true;
        }
    }
}
