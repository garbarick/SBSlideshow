package ru.net.serbis.slideshow.image;

import android.content.*;
import android.os.*;
import android.text.*;
import java.io.*;
import java.util.*;
import ru.net.serbis.slideshow.*;
import ru.net.serbis.slideshow.connection.*;
import ru.net.serbis.slideshow.data.*;
import ru.net.serbis.slideshow.db.*;
import ru.net.serbis.slideshow.db.table.*;
import ru.net.serbis.slideshow.service.*;
import ru.net.serbis.slideshow.tools.*;

public abstract class ExtImages
{
    protected Context context;
    protected DBHelper db;
	protected App app;

    public ExtImages(Context context)
    {
        this.context = context;
        db = new DBHelper(context);
        app = (App) context.getApplicationContext();
    }

    protected abstract ExtConnection getConnection();
    
    protected abstract int actionGetFilesList();
    protected abstract int actionGetFile();
    protected abstract int actionRemoveFile();

    protected abstract String keyPath();
    protected abstract String keyFilesList();
    protected abstract String keyFile();
    protected abstract String keyErrorCode();
    protected abstract String keyResult();
    protected abstract String valueSuccess();

    protected abstract void finishFilesList();

    private void sendServiceAction(int action, String requestKey, String requestValue, Handler reply)
    {
        ExtConnection connection = getConnection();
        if (!connection.isBound())
        {
            return;
        }
        Message msg = Message.obtain(null, action, 0, 0);
        Bundle data = new Bundle();
        if (!TextUtils.isEmpty(requestKey))
        {
            data.putString(requestKey, requestValue);
        }
        msg.setData(data);
        msg.replyTo = new Messenger(reply);
        try
        {
            connection.getService().send(msg);
        }
        catch (RemoteException e)
        {
            Log.error(this, e);
        }
    }

    public void getFilesList(Runner runner, List<Item> folders)
    {
        getFilesList(runner, folders, null);
    }

    public void getFilesList(Runner runner, List<Item> folders, Runnable next)
    {
        getFilesList(runner, folders.iterator(), next);
	}

    protected void getFilesList(final Runner runner, final Iterator<Item> iterator, final Runnable next)
    {
        if (iterator.hasNext())
        {
            sendServiceAction(
                actionGetFilesList(),
                keyPath(),
                iterator.next().getPath(),
                new Handler(Looper.getMainLooper())
                {
                    @Override
                    public void handleMessage(Message msg)
                    {
                        if (msg.getData().containsKey(keyFilesList()))
                        {
                            initFilesList(msg.getData().getString(keyFilesList()));
                            getFilesList(runner, iterator, next);
                        }
                    }
                }
            );
        }
        else if (next != null)
        {
            finishFilesList();
            next.run();
        }
        else
        {
            finishFilesList();
            db.files.excludeNoExist();
            runner.drawAction();
        }
	}

    protected void initFilesList(final String fileList)
    {
        db.files.initFiles(
            new FilesFinder()
            {
                public void find(Files files)
                {
                    findFiles(fileList, files);
                }
            });
	}

    protected void findFiles(String fileList, Files files)
    {
        File file = new File(fileList);
        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            while ((line = reader.readLine()) != null)
            {
                if (FileHelper.checkExt(line))
                {
                    files.addFile(line);
                }
            }
        }
        catch (Throwable e)
        {
            Log.error(this, e);
        }
        finally
        {
            Utils.close(reader);
            new File(fileList).delete();
        }
	}

    public void getFile(final Maker maker, final String fileName, final boolean removeTemp)
    {
        sendServiceAction(
            actionGetFile(),
            keyPath(),
            fileName,
            new Handler(Looper.getMainLooper())
            {
                @Override
                public void handleMessage(Message msg)
                {
                    if (msg.getData().containsKey(keyFile()))
                    {
                        String result = msg.getData().getString(keyFile());
                        maker.make(result);
                        if (removeTemp)
                        {
                            new File(result).delete();
                        }
                    }
                    else if (msg.getData().containsKey(keyErrorCode()) &&
                             Constants.ERROR_FILE_NOT_FOUND == msg.getData().getInt(keyErrorCode()))
                    {
                        maker.make(fileName);
                    }
                }
            }
        );
    }

    public void removeFile(final Maker maker, final String fileName)
    {
        sendServiceAction(
            actionRemoveFile(),
            keyPath(),
            fileName,
            new Handler(Looper.getMainLooper())
            {
                @Override
                public void handleMessage(Message msg)
                {
                    if (msg.getData().containsKey(keyResult()) &&
                        valueSuccess().equals(msg.getData().getString(keyResult())))
                    {
                        maker.make(fileName);
                    }
                    else if (msg.getData().containsKey(keyErrorCode()) &&
                             Constants.ERROR_FILE_NOT_FOUND == msg.getData().getInt(keyErrorCode()))
                    {
                        maker.make(fileName);
                    }
                }
            }
        );
	}
}
