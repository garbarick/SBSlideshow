package ru.net.serbis.slideshow.adapter;

import android.content.*;

public class SettingItem
{
    private Context context;
    private int title;
    private Class<?> activity;

    public SettingItem(Context context, int title, Class<?> activity)
    {
        this.context = context;
        this.title = title;
        this.activity = activity;
    }

    @Override
    public String toString()
    {
        return context.getResources().getString(title);
    }

    public Intent getIntent()
    {
        return new Intent(context, activity);
    }
}
