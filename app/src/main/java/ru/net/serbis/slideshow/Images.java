package ru.net.serbis.slideshow;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;
import java.util.*;

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
        Set<String> dirs = new LinkedHashSet<String>();
        for (String env : Arrays.asList(
            "EXTERNAL_STORAGE",
            "SECONDARY_STORAGE",
            "SECOND_VOLUME_STORAGE"))
        {
            String dir = System.getenv(env);
            if (dir != null && dir.length() > 0 && !dirs.contains(dir))
            {
                dirs.add(dir);
                FileHelper.initWallpapers(dir, files);
            }
        }
        Collections.shuffle(files);
        db.init(files);
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
