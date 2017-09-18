package ru.net.serbis.slideshow.image;

import android.content.*;
import android.net.*;
import java.io.*;
import java.util.*;
import ru.net.serbis.slideshow.*;
import ru.net.serbis.slideshow.data.*;
import ru.net.serbis.slideshow.db.*;
import ru.net.serbis.slideshow.service.*;
import android.text.*;

/**
 * SEBY0408
 */
public class Images
{
    private DBHelper db;
    private Context context;

    public Images(Context context)
    {
        db = new DBHelper(context);
        this.context = context;
    }

    public void init(Runner runner)
    {
        List<String> files = new ArrayList<String>();
		List<Folder> megaFolders = new ArrayList<Folder>();
        for (Folder folder : db.getFolders())
        {
			switch(folder.getType())
			{
				case Default:
				case System:
					FileHelper.initWallpapers(folder, files);
					break;
					
				case Mega:
					megaFolders.add(folder);
					break;
			}
        }
        db.initFiles(files, false);
		
		if (!megaFolders.isEmpty())
        {
			new MegaImages(context).getFilesList(runner, megaFolders);
        }
		else
		{
			runner.drawAction();
		}
    }

    public void next(Runner runner)
    {
        if (db.hasNext())
        {
            db.next();
			runner.drawAction();
        }
        else
        {
            init(runner);
        }
    }

    public void previous(Runner runner)
    {
        if (db.hasPrevious())
        {
            db.previous();
			runner.drawAction();
        }
    }

    public void initCurrent(Maker maker)
    {
        String current = db.getCurrentPath();
		if (TextUtils.isEmpty(current))
		{
			return;
		}
		if (current.startsWith(Constants.MEGA_PREFIX))
		{
			new MegaImages(context).getFile(maker, current);
		}
		else
		{
			maker.make(current);
		}
    }

    public void deleteCurrent(final Runner runner)
    {
		String current = db.getCurrentPath();
		if (TextUtils.isEmpty(current))
		{
			return;
		}
		Maker maker = new Maker()
		{
			public void make(String fileName)
			{
				deleteCurrent(runner, fileName);
			}
		};
		if (current.startsWith(Constants.MEGA_PREFIX))
		{
			new MegaImages(context).removeFile(maker, current);
		}
		else
		{
			maker.make(current);
		}
    }
	
	private void deleteCurrent(Runner runner, String fileName)
	{
		db.deleteCurrent();
		File file = FileHelper.getFile(fileName);
        if (file != null)
        {
            file.delete();
        }
		next(runner);
	}

	public void open()
	{
		initCurrent(
			new Maker()
			{
				public void make(String fileName)
				{
					open(fileName);
				}
			}
		);
	}
	
    private void open(String fileName)
    {
        File file = FileHelper.getFile(fileName);
        if (file != null)
        {
            Intent open = new Intent();
            open.setAction(Intent.ACTION_VIEW);
            open.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            open.setDataAndType(Uri.fromFile(file), "image/*");

            context.startActivity(open);
        }
    }
}
