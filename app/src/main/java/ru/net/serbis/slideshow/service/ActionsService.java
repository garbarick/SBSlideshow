package ru.net.serbis.slideshow.service;

import android.app.*;
import android.content.*;
import android.os.*;
import ru.net.serbis.slideshow.*;
import ru.net.serbis.slideshow.connection.*;

public class ActionsService extends Service implements ConnectionHandler
{
	private MegaConnection connection = new MegaConnection(this);
	private Action lastAction;
	
	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		bindMega();
	}

	private void bindMega()
	{
		try
		{
			Intent intent = new Intent();
			intent.setClassName(Constants.MEGA_PACKAGE, Constants.MEGA_SERVICE);
			bindService(intent, connection, Context.BIND_AUTO_CREATE);
		}
		catch (Throwable e)
		{
			Log.info(this, e.getMessage());
		}
	}

	@Override
	public void onDestroy()
	{
		unBindMega();
		super.onDestroy();
	}

	@Override
	public void onTaskRemoved(Intent rootIntent)
	{
		unBindMega();
		super.onTaskRemoved(rootIntent);
	}
	
	private void unBindMega()
	{
		if (connection.isBound())
        {
            unbindService(connection);
        }
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		runAction(Action.getAction(intent.getStringExtra(Constants.ACTION)));
		return START_REDELIVER_INTENT;
	}

	public void runAction(Action action)
	{
		if (!connection.isBound())
		{
			lastAction = action;
			bindMega();
		}
		Log.info(this, "mega=" + connection.isBound());
		
		ImageService service = ImageService.getInstance();
		Runner runner = service != null ?
			service.getRunner() :
			new StaticRunner(getApplicationContext());
	    runner.runActionInThread(action);
	}

	@Override
	public void onConnect()
	{
		if (lastAction != null)
		{
			Log.info(this, "lastAction=" + lastAction);
			lastAction = null;
		}
	}
}
