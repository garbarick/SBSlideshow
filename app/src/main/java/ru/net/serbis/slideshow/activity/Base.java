package ru.net.serbis.slideshow.activity;

import android.app.*;
import android.os.*;
import android.view.*;
import ru.net.serbis.slideshow.*;
import ru.net.serbis.slideshow.adapter.*;
import ru.net.serbis.slideshow.db.*;

public abstract class Base<T> extends ListActivity
{
    protected App app;
    protected DBHelper db;
    protected Adapter<T> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        app = (App) getApplication();
		db = new DBHelper(this);

        initAdapter();
    }

    protected void initAdapter()
    {
        adapter = getAdapter();
        setListAdapter(adapter);
    }

    protected abstract Adapter<T> getAdapter();

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        int menuId = getOptionMenuId();
        if (menuId == 0)
        {
            return false;
        }
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(menuId, menu);
        return true;
    }
    
    protected int getOptionMenuId()
    {
        return 0;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (onItemMenuSelected(item.getItemId(), null))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onItemMenuSelected(int id, T item)
    {
        return false;
    }
}
