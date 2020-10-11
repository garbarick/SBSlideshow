package ru.net.serbis.slideshow.service;

import android.app.*;
import android.content.*;
import android.os.*;
import ru.net.serbis.slideshow.data.*;

public class ActionsService extends Service
{
	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		runAction(Action.get(intent.getAction()));
		return START_REDELIVER_INTENT;
	}

	public void runAction(Action action)
	{
		ImageService service = ImageService.getInstance();
		Runner runner = service != null ?
			service.getRunner() :
			new StaticRunner(getApplicationContext());
	    runner.runActionInThread(action);
	}
    
    public static void startAction(Context context, Action action)
    {
        Intent intent = new Intent(context, ActionsService.class);
        intent.setAction(action.name());

        context.startService(intent);
		context.sendBroadcast(intent);
    }
}
