package com.example.counterwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link CounterWidgetConfigureActivity CounterWidgetConfigureActivity}
 */
public class CounterWidget extends AppWidgetProvider {
    private static final String ACTION_ADD = "buttonadd";
    private static final String ACTION_SUB = "buttonsub";
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = CounterWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.counter_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        Intent activeAdd = new Intent(context, CounterWidget.class);
        activeAdd.setAction(ACTION_ADD);
        activeAdd.putExtra("number", appWidgetId);
        views.setOnClickPendingIntent(R.id.btn_widgetCountUp, PendingIntent.getBroadcast(context, appWidgetId, activeAdd, PendingIntent.FLAG_UPDATE_CURRENT));

        Intent activeSub = new Intent(context, CounterWidget.class);
        activeSub.setAction(ACTION_SUB);
        activeSub.putExtra("number", appWidgetId);
        views.setOnClickPendingIntent(R.id.btn_widgetCountDown, PendingIntent.getBroadcast(context, appWidgetId, activeSub, PendingIntent.FLAG_UPDATE_CURRENT));


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            Log.d("MyLog", "onUpdate for");
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            CounterWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    void updateWidget(Context context) {
        AppWidgetManager localAppWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName localComponentName = new ComponentName(context, CounterWidget.class);
        Log.d("MyLog", localAppWidgetManager.getAppWidgetIds(localComponentName).toString());
        onUpdate(context, localAppWidgetManager, localAppWidgetManager.getAppWidgetIds(localComponentName));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d("MyLog", "onReceive");
        String action = intent.getAction();
        if(action.equals(ACTION_ADD)) {
//            Log.d("MyLog", "ACTION_ADD");
            String tt = CounterWidgetConfigureActivity.loadTitlePref(context, intent.getIntExtra("number", -1) );
            int newValue = Integer.parseInt(tt) + 1;
            CounterWidgetConfigureActivity.saveTitlePref(context, intent.getIntExtra("number", -1), String.valueOf(newValue));
            updateWidget(context);
        }

        if(action.equals(ACTION_SUB)) {
//            Log.d("MyLog", "ACTION_SUB");
            String tt = CounterWidgetConfigureActivity.loadTitlePref(context, intent.getIntExtra("number", -1) );
//            Log.d("MyLog", tt);
            int newValue = Integer.parseInt(tt) - 1;
            CounterWidgetConfigureActivity.saveTitlePref(context, intent.getIntExtra("number", -1), String.valueOf(newValue));
            updateWidget(context);
        }
    }
}