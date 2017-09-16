package ru.net.serbis.slideshow.connection;

import android.content.*;
import android.os.*;

public class MegaConnection implements ServiceConnection
{
    private boolean bound;
    private Messenger service;
	private ConnectionHandler handler;

	public MegaConnection()
	{
	}
	
	public MegaConnection(ConnectionHandler handler)
	{
		this.handler = handler;
	}

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
		
		if (handler != null)
		{
			handler.onConnect();
		}
    }

    @Override
    public void onServiceDisconnected(ComponentName p1)
    {
        service = null;
        bound = false;
    }
}
