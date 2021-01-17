package ru.net.serbis.slideshow.db;

import android.content.*;
import android.database.sqlite.*;
import java.io.*;
import java.util.*;
import ru.net.serbis.slideshow.*;
import ru.net.serbis.slideshow.data.*;
import ru.net.serbis.slideshow.db.table.*;

public class DBHelper extends SQLiteOpenHelper
{
	public Files files = new Files(this);
	public Folders folders = new Folders(this);
	public Parameters parameters = new Parameters(this);
	public Information information = new Information(this);
    private Context context;
    
    public DBHelper(Context context)
    {
        super(context, "db", null, 4);
        this.context = context;
    }

    public Context getContext()
    {
        return context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
		for (Table table : Arrays.asList(files, folders, parameters))
		{
			try
			{
				table.init(db);
			}
			catch (Exception e)
			{
				Log.error(this, "Error on init", e);
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

	public List<Item> getFolders()
	{
		List<Item> result = new ArrayList<Item>();
		result.addAll(getDefault());
		result.addAll(folders.getFolders());
		return result;
	}
	
	private List<Item> getDefault()
	{
		List<Item> result = new ArrayList<Item>();
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
					result.add(new Item(wallpapers, FileType.Default));
				}
            }
        }
		return result;
	}
	
	private String getWallpapers(String storage)
    {
        return getWallpapers(new File(storage));
    }

    private String getWallpapers(File storage)
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
}
