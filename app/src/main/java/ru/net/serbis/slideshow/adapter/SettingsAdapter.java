package ru.net.serbis.slideshow.adapter;

import android.content.*;
import ru.net.serbis.slideshow.*;
import ru.net.serbis.slideshow.activity.*;

public class SettingsAdapter extends Adapter<SettingItem>
{
    private void init(Context context)
    {
        add(new SettingItem(context, R.string.wall_folders, Folders.class));
        add(new SettingItem(context, R.string.parameters, Parameters.class));
        add(new SettingItem(context, R.string.information, Information.class));
        add(new SettingItem(context, R.string.controls, Controls.class));
    };

    public SettingsAdapter(Context context)
    {
        super(context, android.R.layout.simple_list_item_1);
        init(context);
    }
}
