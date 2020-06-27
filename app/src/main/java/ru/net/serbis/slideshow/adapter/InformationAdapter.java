package ru.net.serbis.slideshow.adapter;

import android.content.*;
import android.widget.*;
import ru.net.serbis.slideshow.data.*;
import ru.net.serbis.slideshow.db.*;

public class InformationAdapter extends ArrayAdapter<Info>
{
    public InformationAdapter(Context context, DBHelper db)
    {
        super(context, android.R.layout.simple_list_item_1, db.information.get());
    }
}
