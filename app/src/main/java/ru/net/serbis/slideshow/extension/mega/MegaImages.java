package ru.net.serbis.slideshow.extension.mega;

import android.content.*;
import ru.net.serbis.slideshow.*;
import ru.net.serbis.slideshow.image.*;
import ru.net.serbis.slideshow.tools.*;
import ru.net.serbis.slideshow.connection.*;

public class MegaImages extends ExtImages
{
	public MegaImages(Context context)
    {
        super(context);
    }

    @Override
    protected ExtConnection getConnection()
    {
        return app.getMegaConnection();
    }

    @Override
    protected int actionGetFilesList()
    {
        return Mega.ACTION_GET_FILES_LIST;
    }

    @Override
    protected int actionGetFile()
    {
        return Mega.ACTION_GET_FILE;
    }

    @Override
    protected int actionRemoveFile()
    {
        return Mega.ACTION_REMOVE_FILE;
    }

    @Override
    protected String keyPath()
    {
        return Mega.PATH;
    }

    @Override
    protected String keyFilesList()
    {
        return Mega.FILES_LIST;
    }

    @Override
    protected String keyFile()
    {
        return Mega.FILE;
    }

    @Override
    protected String keyErrorCode()
    {
        return Mega.ERROR_CODE;
    }

    @Override
    protected String keyResult()
    {
        return Mega.RESULT;
    }

    @Override
    protected String valueSuccess()
    {
        return Mega.SUCCESS;
    }

    @Override
    protected void finishFilesList()
    {
        Log.info(this, "mega folders finished");
        UITools.toast(context, "MEGA Files updated");
    }
}
