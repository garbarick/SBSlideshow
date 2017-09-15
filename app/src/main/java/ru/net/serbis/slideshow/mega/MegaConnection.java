package ru.net.serbis.slideshow.mega;

import android.content.*;
import android.os.*;

public class MegaConnection implements ServiceConnection
{
    private boolean bound;
    private Messenger service;

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
}
