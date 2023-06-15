package ru.net.serbis.slideshow.extension.mega;

import android.app.*;
import android.content.*;
import ru.net.serbis.slideshow.*;
import ru.net.serbis.slideshow.connection.*;

public class MegaConnection extends ExtConnection
{
    public MegaConnection(Application app)
    {
        super(app);
    }

    @Override
    protected String packageName()
    {
        return Mega.PACKAGE;
    }

    @Override
    protected String serviceName()
    {
        return Mega.SERVICE;
    }
}
