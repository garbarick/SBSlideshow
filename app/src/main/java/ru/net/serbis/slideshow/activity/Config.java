package ru.net.serbis.slideshow.activity;

import android.app.*;
import android.appwidget.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import ru.net.serbis.slideshow.*;
import ru.net.serbis.slideshow.adapter.*;
import ru.net.serbis.slideshow.tools.*;

public class Config extends Activity
{
    private int widgetId;
    private Button createWidget;
    private ListView items;
    private ActionsAdapter actionAdapter;
    private Widget widget;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config);
        setResult(RESULT_CANCELED);

        createWidget = UITools.findView(this, R.id.createWidget);
        items = UITools.findView(this, R.id.buttons);
        widgetId = getIntent().getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        widget = getWidget(this, widgetId);

        initButtons();
        initCreateWidget();
    }

    private void initButtons()
    {
        actionAdapter = new ActionsAdapter(this, widget.count);
        items.setAdapter(actionAdapter);
        items.setOnItemClickListener(actionAdapter);
    }

    private void initCreateWidget()
    {
        createWidget.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                SharedPreferences.Editor preferences = getSharedPreferences(Constants.WIDGET_PREFERENCE, MODE_WORLD_WRITEABLE).edit();
                preferences.putString(String.valueOf(widgetId), actionAdapter.getCheckedActions().toString());
                preferences.commit();

                widget.initWidget(Config.this, widgetId);

                Intent intent = new Intent();
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
                setResult(RESULT_OK, intent);

                finish();
            }
        });
    }

    public Widget getWidget(Context context, int widgetId)
    {
        try
        {
            AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
            Class clazz = Class.forName(widgetManager.getAppWidgetInfo(widgetId).provider.getClassName());
            return (Widget) clazz.newInstance();
        }
        catch (Throwable e)
        {
            Log.info(this, "error on initWidget", e);
            return null;
        }
    }
}
