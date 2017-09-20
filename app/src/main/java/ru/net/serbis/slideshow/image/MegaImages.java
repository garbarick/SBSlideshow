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

public class MegaImages
{
	private DBHelper db;
	private App app;

	public MegaImages(Context context)
    {
        db = new DBHelper(context);
		app = (App) context.getApplicationContext();
    }

	public void getFilesList(Runner runner, List<Item> folders)
	{
		getFilesList(runner, folders.iterator());
	}

	private void getFilesList(final Runner runner, final Iterator<Item> iterator)
	{
		if (iterator.hasNext())
		{
			sendServiceAction(
				Constants.MEGA_ACTION_GET_FILES_LIST,
				Constants.MEGA_PATH,
				iterator.next().getPath(),
				new Handler(Looper.getMainLooper())
				{
					@Override
					public void handleMessage(Message msg)
					{
						if (msg.getData().containsKey(Constants.MEGA_FILES_LIST))
						{
							initFilesList(msg.getData().getString(Constants.MEGA_FILES_LIST));
						}
                        getFilesList(runner, iterator);
					}
				}
			);
		}
        else
        {
            Log.info(this, "mega folders finished");
            runner.drawAction();
        }
	}

	private void initFilesList(final String fileList)
	{
        db.initFiles(
            new FilesFinder()
            {
                public void find(Files files)
                {
                    findFiles(fileList, files);
                }
            },
            true);
	}

	private void findFiles(String fileList, Files files)
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
			Log.info(this, e);
		}
		finally
		{
			Utils.close(reader);
            new File(fileList).delete();
		}
	}

	public void getFile(final Maker maker, String fileName, final boolean removeTemp)
	{
		sendServiceAction(
			Constants.MEGA_ACTION_GET_FILE,
			Constants.MEGA_PATH,
			fileName,
			new Handler(Looper.getMainLooper())
			{
				@Override
				public void handleMessage(Message msg)
				{
					if (msg.getData().containsKey(Constants.MEGA_FILE))
					{
						String result = msg.getData().getString(Constants.MEGA_FILE);
						maker.make(result);
						if (removeTemp)
						{
							new File(result).delete();
						}
					}
				}
			}
		);
	}

	public void removeFile(final Maker maker, final String fileName)
	{
		sendServiceAction(
			Constants.MEGA_ACTION_REMOVE_FILE,
			Constants.MEGA_PATH,
			fileName,
			new Handler(Looper.getMainLooper())
			{
				@Override
				public void handleMessage(Message msg)
				{
					if (msg.getData().containsKey(Constants.MEGA_RESULT) &&
						Constants.MEGA_SUCCESS.equals(msg.getData().getString(Constants.MEGA_RESULT)))
					{
						maker.make(fileName);
					}
				}
			}
		);
	}

	private void sendServiceAction(int action, String requestKey, String requestValue, Handler reply)
	{
		MegaConnection connection = app.getMegaConnection();
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
			Log.info(this, e);
		}
	}
}
