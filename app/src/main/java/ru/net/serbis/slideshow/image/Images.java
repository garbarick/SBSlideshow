package ru.net.serbis.slideshow.image;

import android.content.*;
import android.net.*;
import java.io.*;
import java.util.*;
import ru.net.serbis.slideshow.data.*;
import ru.net.serbis.slideshow.db.*;
import ru.net.serbis.slideshow.db.table.*;
import ru.net.serbis.slideshow.extension.mega.*;
import ru.net.serbis.slideshow.service.*;
import ru.net.serbis.slideshow.tools.*;
import ru.net.serbis.slideshow.extension.share.*;

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
        Map<FileType, List<Item>> folders = new HashMap<FileType, List<Item>>();

        folders.put(FileType.System, new ArrayList<Item>());
        folders.put(FileType.Mega, new ArrayList<Item>());
        folders.put(FileType.Share, new ArrayList<Item>());

        for (Item folder : db.getFolders())
        {
            FileType type = folder.getType();
            type = type == FileType.Default ? FileType.System : type;
            folders.get(type).add(folder);
        }

        db.files.clearExist();
        getFilesList(folders.get(FileType.System));
	    getExtFilesList(runner, folders);
    }

    private void getFilesList(final List<Item> folders)
    {
        db.files.initFiles(
            new FilesFinder()
            {
                public void find(Files files)
                {
                    FileHelper.findFiles(folders, files);
                }
            });
		UITools.toast(context, "Local Files updated");
    }

    private void getExtFilesList(final Runner runner, final Map<FileType, List<Item>> folders)
    {
        new MegaImages(context)
            .getFilesList(
            runner,
            folders.get(FileType.Mega),
            new Runnable()
            {
                @Override
                public void run()
                {
                    new ShareImages(context)
                        .getFilesList(
                        runner,
                        folders.get(FileType.Share));
                }
            }
        );
    }

    public void next(Runner runner)
    {
        if (db.files.hasNext())
        {
            db.files.next();
			runner.drawAction();
        }
        else
        {
            init(runner);
        }
    }

    public void previous(Runner runner)
    {
        if (db.files.hasPrevious())
        {
            db.files.previous();
			runner.drawAction();
        }
    }

    public void initCurrent(Maker maker, boolean removeTemp)
    {
		Item item = db.files.getCurrentItem();
        if (item == null)
        {
            return;
        }
		switch(item.getType())
		{
			case Mega:
				new MegaImages(context).getFile(maker, item.getPath(), removeTemp);
				break;

			case Share:
                new ShareImages(context).getFile(maker, item.getPath(), removeTemp);
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
		Item item = db.files.getCurrentItem();
        if (item == null)
        {
            return;
        }
		switch(item.getType())
		{
			case Mega:
				new MegaImages(context).removeFile(maker, item.getPath());
				break;

            case Share:
                new ShareImages(context).removeFile(maker, item.getPath());
				break;

			case System:
				maker.make(item.getPath());
				break;
		}
    }
	
	private void deleteCurrent(Runner runner, String fileName)
	{
		db.files.deleteCurrent();
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
