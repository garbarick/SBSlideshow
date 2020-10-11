package ru.net.serbis.slideshow.activity;

import android.view.*;
import android.widget.*;
import ru.net.serbis.slideshow.adapter.*;
import ru.net.serbis.slideshow.data.*;
import ru.net.serbis.slideshow.service.*;

import ru.net.serbis.slideshow.adapter.Adapter;

public class Controls extends Base<Action>
{
    @Override
    protected Adapter<Action> getAdapter()
    {
        return new ControlsAdapter(this);
    }
    
    @Override
    protected void onListItemClick(ListView list, View view, int position, long id)
    {
        super.onListItemClick(list, view, position, id);
        Action action = adapter.getItem(position);
        ActionsService.startAction(this, action);
    }
}
