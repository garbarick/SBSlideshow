package ru.net.serbis.slideshow;

import android.app.*;
import android.content.*;
import ru.net.serbis.slideshow.*;
import ru.net.serbis.slideshow.connection.*;

public class App extends Application
{
	private MegaConnection connection = new MegaConnection();

	@Override
	public void onCreate()
	{
		super.onCreate();
		bindMega();
	}

	@Override
	public void onTerminate()
	{
		super.onTerminate();
		unbindMega();
	}

	private void bindMega()
	{
		try
		{
			if (!connection.isBound())
			{
				Intent intent = new Intent();
				intent.setClassName(Constants.MEGA_PACKAGE, Constants.MEGA_SERVICE);
				bindService(intent, connection, Context.BIND_AUTO_CREATE | Context.BIND_ADJUST_WITH_ACTIVITY);
			}
		}
		catch (Throwable e)
		{
			Log.info(this, e.getMessage());
		}
	}

	private void unbindMega()
	{
		if (connection.isBound())
        {
            unbindService(connection);
        }
	}
	
	public MegaConnection getMegaConnection()
	{
		bindMega();
		return connection;
	}
}
