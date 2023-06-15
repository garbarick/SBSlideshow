package ru.net.serbis.slideshow.connection;

import android.app.*;
import android.content.*;
import android.os.*;
import ru.net.serbis.slideshow.*;

public abstract class ExtConnection implements ServiceConnection
{
    protected Application app;
    protected boolean bound;
    protected Messenger service;

    public ExtConnection(Application app)
    {
        this.app = app;
    }

    protected abstract String packageName();
    protected abstract String serviceName();

    public boolean isBound()
    {
        return bound;
    }

    public Messenger getService()
    {
        return service;
    }

    @Override
    public void onServiceConnected(ComponentName classsName, IBinder binder)
    {
        service = new Messenger(binder);
        bound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName p1)
    {
        service = null;
        bound = false;
    }

    public void bind()
    {
        try
        {
            if (!isBound())
            {
                Intent intent = new Intent();
                intent.setClassName(packageName(), serviceName());
                app.bindService(intent, this, Context.BIND_AUTO_CREATE | Context.BIND_ADJUST_WITH_ACTIVITY);
            }
        }
        catch (Throwable e)
        {
            Log.error(this, e);
        }
    }

    public void unBind()
    {
        if (isBound())
        {
            app.unbindService(this);
        }
    }
}
