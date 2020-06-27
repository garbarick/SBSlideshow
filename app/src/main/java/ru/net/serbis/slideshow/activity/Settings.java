package ru.net.serbis.slideshow.activity;

import android.content.*;
import android.view.*;
import android.widget.*;
import ru.net.serbis.slideshow.adapter.*;

public class Settings extends Base
{
    private SettingsAdapter adapter;

    @Override
    protected void initAdapter()
    {
        adapter = new SettingsAdapter(this);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView list, View view, int position, long id)
    {
        super.onListItemClick(list, view, position, id);
        SettingItem item = adapter.getItem(position);
        startActivity(item.getIntent());
    }
}
