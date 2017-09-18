package ru.net.serbis.slideshow.service;

import android.app.*;
import android.content.*;
import android.graphics.*;
import java.io.*;
import ru.net.serbis.slideshow.*;

public class StaticRunner extends Runner
{
    public StaticRunner(Context context)
    {
        super(context);
    }
	
	@Override
    public void drawAction()
    {
		images.initCurrent(
			new Maker()
			{
				public void make(String fileName)
				{
					drawAction(fileName);
				}
			}
		);
	}

    private void drawAction(String fileName)
    {
        try
        {
            WallpaperManager manager = WallpaperManager.getInstance(context);
            File file = FileHelper.getFile(fileName);
            if (file != null)
            {
                Bitmap bitmap = load(file);
                manager.setBitmap(bitmap);
            }
            else
            {
                manager.clear();
            }
        }
        catch (Throwable e)
        {
            Log.info(this, "error on set wallpaper", e);
        }
    }
    
    private Bitmap load(File source)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;

        return BitmapFactory.decodeFile(source.getAbsolutePath(), options);
    }
}
