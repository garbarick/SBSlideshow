package ru.net.serbis.slideshow.image;

import android.content.*;
import android.net.*;
import java.io.*;
import java.util.*;
import ru.net.serbis.slideshow.*;
import ru.net.serbis.slideshow.data.*;
import ru.net.serbis.slideshow.db.*;

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

    public void init()
    {
        List<String> files = new ArrayList<String>();
        for (Folder folder : db.getFolders())
        {
        	FileHelper.initWallpapers(folder, files);
        }
        Collections.shuffle(files);
        db.initFiles(files);
    }

    public void next()
    {
        if (db.hasNext())
        {
            db.next();
        }
        else
        {
            init();
        }
    }

    public void previous()
    {
        if (db.hasPrevious())
        {
            db.previous();
        }
    }

    public String getCurrent()
    {
        return db.getCurrentPath();
    }

    public File getCurrentFile()
    {
        String current = getCurrent();
        if (FileHelper.exist(current))
        {
            return new File(current);
        }
        return null;
    }

    public void deleteCurrent()
    {
        File file = getCurrentFile();
        if (file != null)
        {
            db.deleteCurrent();
            file.delete();
            next();
        }
    }

    public void open()
    {
        File file = getCurrentFile();
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
