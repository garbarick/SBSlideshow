package ru.net.serbis.slideshow.activity;

import android.app.*;
import android.appwidget.*;
import android.content.*;
import android.net.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.slideshow.*;
import ru.net.serbis.slideshow.service.*;

public class Widget extends AppWidgetProvider
{
    protected int count;

    @Override
    public void onUpdate(Context context, AppWidgetManager widgetManager, int[] widgetIds)
    {
        super.onUpdate(context, widgetManager, widgetIds);

        for (int widgetId : widgetIds)
        {
            initWidget(context, widgetId);
        }
    }

    private void setAction(Context context, RemoteViews views, int viewId, Action action)
    {
        views.setOnClickPendingIntent(viewId, getPendingSelfIntent(context, action));
    }

    private PendingIntent getPendingSelfIntent(Context context, Action action)
    {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action.name());
        intent.setData(Uri.fromParts(action.name(), getClass().getName(), ""));
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        super.onReceive(context, intent);
        try
        {
            Action action = Action.getAction(intent.getAction());
            if (action != null)
            {
                ImageService service = ImageService.getInstance();
                if (service != null)
                {
                    service.getRunner().runActionInThread(action);
                }
                else
                {
                    new StaticRunner(context).runActionInThread(action);
                }
            }
        }
        catch (Throwable e)
        {
            Log.info(this, "error onReceive", e);
        }
    }

    @Override
    public void onDeleted(Context context, int[] widgetIds)
    {
        super.onDeleted(context, widgetIds);

        SharedPreferences.Editor preferences = context.getSharedPreferences(Constants.WIDGET_PREFERENCE, Context.MODE_WORLD_WRITEABLE).edit();
        for (int widgetId : widgetIds)
        {
            String widgetKey = String.valueOf(widgetId);
            preferences.remove(widgetKey);
        }
        preferences.commit();
    }

    private List<String> getActions(Context context, int widgetId)
    {
        String value = context.getSharedPreferences(Constants.WIDGET_PREFERENCE, Context.MODE_WORLD_WRITEABLE).getString(String.valueOf(widgetId), null);
        if (value != null && value.length() > 0)
        {
            value = value.replaceAll("^\\[", "").replaceAll("\\]$", "");
            return Arrays.asList(value.split("[ ,]+"));
        }
        return Collections.EMPTY_LIST;
    }

    public void initWidget(Context context, int widgetId)
    {
        List<String> names = getActions(context, widgetId);
        if (!names.isEmpty())
        {
            AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);

            RemoteViews views = new RemoteViews(context.getPackageName(), widgetManager.getAppWidgetInfo(widgetId).initialLayout);

            int indexViewId = 0;

            for (String name : names)
            {
                Action action = Action.getAction(name);
                if (action != null)
                {
                    int viewId = Constants.VIEWS.get(indexViewId);
                    views.setImageViewResource(viewId, action.getResource());
                    views.setViewVisibility(viewId, View.VISIBLE);
                    setAction(context, views, viewId, action);
                    indexViewId++;
                    if (indexViewId == count)
                    {
                        break;
                    }
                }
            }

            for (; indexViewId < Constants.VIEWS.size(); indexViewId++)
            {
                int viewId = Constants.VIEWS.get(indexViewId);
                views.setViewVisibility(viewId, View.GONE);
            }

            widgetManager.updateAppWidget(widgetId, views);
        }
    }
}
