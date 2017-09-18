package ru.net.serbis.slideshow.db;

import android.content.*;
import android.database.sqlite.*;
import java.io.*;
import java.util.*;
import ru.net.serbis.slideshow.*;
import ru.net.serbis.slideshow.data.*;
import ru.net.serbis.slideshow.db.table.*;

/**
 * SEBY0408
 */
public class DBHelper extends SQLiteOpenHelper
{
	private Files files = new Files(this);
	private Folders folders = new Folders(this);
	
    public DBHelper(Context context)
    {
        super(context, "db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
		for (Table table : Arrays.asList(files, folders))
		{
			try
			{
				table.init(db);
			}
			catch (Throwable e)
			{
				Log.info(this, "Error on init", e);
			}
		}
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
		onCreate(db);
    }

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		onCreate(db);
	}
    
    public void initFiles(List<String> files, boolean add)
    {
		this.files.initFiles(files, add);
    }

    public boolean hasNext()
    {
		return files.hasNext();
    }

    public void next()
    {
		files.next();
    }

    public boolean hasPrevious()
    {
		return files.hasPrevious();
    }

    public void previous()
    {
		files.previous();
    }

    public String getCurrentPath()
    {
		return files.getCurrentPath();
    }

    public void deleteCurrent()
    {
		files.deleteCurrent();
    }
	
	public List<Folder> getFolders()
	{
		List<Folder> result = new ArrayList<Folder>();
		result.addAll(getDefault());
		result.addAll(folders.getFolders());
		return result;
	}
	
	private List<Folder> getDefault()
	{
		List<Folder> result = new ArrayList<Folder>();
		Set<String> dirs = new HashSet<String>();
        for (String env : Arrays.asList(
            "EXTERNAL_STORAGE",
            "SECONDARY_STORAGE",
            "SECOND_VOLUME_STORAGE"))
        {
            String dir = System.getenv(env);
            if (dir != null && dir.length() > 0 && !dirs.contains(dir))
            {
                dirs.add(dir);
				String wallpapers = getWallpapers(dir);
				if (wallpapers != null)
				{
					result.add(new Folder(wallpapers, FolderType.Default));
				}
            }
        }
		return result;
	}
	
	public String getWallpapers(String storage)
    {
        return getWallpapers(new File(storage));
    }

    public String getWallpapers(File storage)
    {
        if (storage.exists() && storage.isDirectory() && storage.canRead())
        {
            File wallpapers = new File(storage, "Wallpapers");
            if (wallpapers.exists() && wallpapers.isDirectory() && wallpapers.canRead())
            {
                return wallpapers.getAbsolutePath();
            }
        }
		return null;
    }
	
	public void addFolder(Folder folder)
	{
		folders.addFolder(folder);
	}
	
	public void excludeFolder(Folder folder)
	{
		folders.excludeFolder(folder);
	}
}
