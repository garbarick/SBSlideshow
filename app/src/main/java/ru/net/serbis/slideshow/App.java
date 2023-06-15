package ru.net.serbis.slideshow;

import android.app.*;
import ru.net.serbis.slideshow.connection.*;
import ru.net.serbis.slideshow.extension.mega.*;
import ru.net.serbis.slideshow.extension.share.*;

public class App extends Application
{
	private ExtConnection megaConnection = new MegaConnection(this);
    private ExtConnection shareConnection = new ShareConnection(this);
    
	@Override
	public void onCreate()
	{
		super.onCreate();
        megaConnection.bind();
        shareConnection.bind();
	}

	@Override
	public void onTerminate()
	{
		super.onTerminate();
        megaConnection.unBind();
        shareConnection.unBind();
	}

	public ExtConnection getMegaConnection()
	{
		megaConnection.bind();
		return megaConnection;
	}
    
    public ExtConnection getShareConnection()
    {
        shareConnection.bind();
        return shareConnection;
	}
}
