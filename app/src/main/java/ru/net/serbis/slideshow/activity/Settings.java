package ru.net.serbis.slideshow.activity;

import android.view.*;
import android.widget.*;
import ru.net.serbis.slideshow.adapter.*;

import ru.net.serbis.slideshow.adapter.Adapter;

public class Settings extends Base<SettingItem>
{
    @Override
    protected Adapter<SettingItem> getAdapter()
    {
        return new SettingsAdapter(this);
    }

    @Override
    protected void onListItemClick(ListView list, View view, int position, long id)
    {
        super.onListItemClick(list, view, position, id);
        SettingItem item = adapter.getItem(position);
        startActivity(item.getIntent());
    }
}
