package ru.net.serbis.slideshow.activity;

import android.app.*;
import android.os.*;
import ru.net.serbis.slideshow.*;
import ru.net.serbis.slideshow.db.*;

public abstract class Base extends ListActivity
{
    protected App app;
    protected DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        app = (App) getApplication();
		db = new DBHelper(this);

        initAdapter();
    }
    
    protected abstract void initAdapter();
}
