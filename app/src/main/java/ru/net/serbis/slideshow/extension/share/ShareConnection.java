package ru.net.serbis.slideshow.extension.share;

import android.app.*;
import android.content.*;
import ru.net.serbis.slideshow.*;
import ru.net.serbis.slideshow.connection.*;

public class ShareConnection extends ExtConnection
{
    public ShareConnection(Application app)
    {
        super(app);
    }

    @Override
    protected String packageName()
    {
        return Share.PACKAGE;
    }

    @Override
    protected String serviceName()
    {
        return Share.SERVICE;
    }
}
