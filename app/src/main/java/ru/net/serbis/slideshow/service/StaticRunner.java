package ru.net.serbis.slideshow.service;

import android.app.*;
import android.content.*;
import android.graphics.*;
import java.io.*;
import ru.net.serbis.slideshow.*;
import ru.net.serbis.slideshow.image.*;

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
                Bitmap bitmap = drawer.load(file);
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
}
