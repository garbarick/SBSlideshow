package ru.net.serbis.slideshow.adapter;

import android.content.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.slideshow.*;
import ru.net.serbis.slideshow.adapter.*;
import ru.net.serbis.slideshow.activity.*;

public class SettingsAdapter extends ArrayAdapter<SettingItem>
{
    private static List<SettingItem> getList(Context context)
    {
        List<SettingItem> result = new ArrayList<SettingItem>();
        result.add(new SettingItem(context, R.string.wall_folders, Folders.class));
        result.add(new SettingItem(context, R.string.parameters, Parameters.class));
        result.add(new SettingItem(context, R.string.information, Information.class));
        return result;
    };

    public SettingsAdapter(Context context)
    {
        super(context, android.R.layout.simple_list_item_1, getList(context));
    }
}
