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
import ru.net.serbis.slideshow.db.table.*;

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
        final List<Item> systemFolders = new ArrayList<Item>();
        List<Item> megaFolders = new ArrayList<Item>();
        
        for (Item folder : db.getFolders())
        {
			switch(folder.getType())
			{
				case Default:
				case System:
                    systemFolders.add(folder);
					break;
					
				case Mega:
					megaFolders.add(folder);
					break;
			}
        }
        
        db.initFiles(
            new FilesFinder()
            {
                public void find(Files files)
                {
                    FileHelper.findFiles(systemFolders, files);
                }
            },
            false);
		
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

    public void initCurrent(Maker maker, boolean removeTemp)
    {
		Item item = db.getCurrentItem();
		switch(item.getType())
		{
			case Mega:
				new MegaImages(context).getFile(maker, item.getPath(), removeTemp);
				break;
			
			case System:
				maker.make(item.getPath());
				break;
		}
    }
	
	public void initCurrent(Maker maker)
    {
		initCurrent(maker, true);
	}

    public void deleteCurrent(final Runner runner)
    {
		Maker maker = new Maker()
		{
			public void make(String fileName)
			{
				deleteCurrent(runner, fileName);
			}
		};
		Item item = db.getCurrentItem();
		switch(item.getType())
		{
			case Mega:
				new MegaImages(context).removeFile(maker, item.getPath());
				break;

			case System:
				maker.make(item.getPath());
				break;
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
			},
			false
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
