package ru.net.serbis.slideshow.adapter;

import android.content.*;
import android.view.*;
import android.widget.*;

public abstract class Adapter<T> extends ArrayAdapter<T>
{
    protected int layoutId;

    protected Adapter(Context context, int layoutId)
    {
        super(context, layoutId);
        this.layoutId = layoutId;
    }
    
    protected View makeView(ViewGroup parent, int layoutId)
    {
        return LayoutInflater.from(getContext()).inflate(layoutId, parent, false);
    }
    
    protected View makeView(ViewGroup parent)
    {
        return makeView(parent, layoutId);
    }
}
