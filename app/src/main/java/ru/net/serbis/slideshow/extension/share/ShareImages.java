package ru.net.serbis.slideshow.extension.share;

import android.content.*;
import ru.net.serbis.slideshow.*;
import ru.net.serbis.slideshow.image.*;
import ru.net.serbis.slideshow.tools.*;
import ru.net.serbis.slideshow.connection.*;

public class ShareImages extends ExtImages
{
	public ShareImages(Context context)
    {
        super(context);
    }

    @Override
    protected ExtConnection getConnection()
    {
        return app.getShareConnection();
    }

    @Override
    protected int actionGetFilesList()
    {
        return Share.ACTION_GET_FILES_LIST;
    }

    @Override
    protected int actionGetFile()
    {
        return Share.ACTION_GET_FILE;
    }

    @Override
    protected int actionRemoveFile()
    {
        return Share.ACTION_REMOVE_FILE;
    }

    @Override
    protected String keyPath()
    {
        return Share.PATH;
    }

    @Override
    protected String keyFilesList()
    {
        return Share.FILES_LIST;
    }

    @Override
    protected String keyFile()
    {
        return Share.FILE;
    }

    @Override
    protected String keyErrorCode()
    {
        return Share.ERROR_CODE;
    }

    @Override
    protected String keyResult()
    {
        return Share.RESULT;
    }

    @Override
    protected String valueSuccess()
    {
        return Share.SUCCESS;
    }

    @Override
    protected void finishFilesList()
    {
        Log.info(this, "share folders finished");
        UITools.get().toast(context, "SHARE Files updated");
    }
}
