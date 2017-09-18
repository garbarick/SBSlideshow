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

	public void getFilesList(Runner runner, List<Folder> folders)
	{
		getFilesList(runner, folders.iterator());
	}

	private void getFilesList(final Runner runner, final Iterator<Folder> iterator)
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
							initFilesList(runner, msg.getData().getString(Constants.MEGA_FILES_LIST));
							getFilesList(runner, iterator);
						}
					}
				}
			);
		}
	}

	private void initFilesList(Runner runner, String fileList)
	{
		List<String> files = getFiles(fileList);
		if (!files.isEmpty())
		{
			db.initFiles(files, true);
			runner.drawAction();
		}
	}

	private List<String> getFiles(String fileList)
	{
		List<String> files = new ArrayList<String>();
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
					files.add(line);
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
		}
		return files;
	}

	public void getFile(final Maker maker, String fileName)
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
						maker.make(msg.getData().getString(Constants.MEGA_FILE));
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
						Constants.MEGA_SUCCESS.equals(msg.getData().getString(Constants.MEGA_FILE)))
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
