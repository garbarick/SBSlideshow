package ru.net.serbis.slideshow.adapter;

import android.content.*;
import java.util.*;
import ru.net.serbis.slideshow.data.*;

public class InformationAdapter extends Adapter<Info>
{
    public InformationAdapter(Context context, List<Info> items)
    {
        super(context, android.R.layout.simple_list_item_1);
        addAll(items);
    }
}
