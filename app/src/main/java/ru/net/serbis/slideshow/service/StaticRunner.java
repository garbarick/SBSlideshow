package ru.net.serbis.slideshow.service;

import android.app.*;
import android.content.*;
import android.graphics.*;
import java.io.*;
import ru.net.serbis.slideshow.*;
import ru.net.serbis.slideshow.image.*;
import android.view.*;

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
                setImage(file, manager);
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

    private void setImage(File file, WallpaperManager manager) throws IOException
    {
        Bitmap image = null;
        try
        {
            image = drawer.load(file);
            if (image != null)
            {
                Point size = getDisplaySize();
                image = drawer.getScaled(image, size);
                manager.suggestDesiredDimensions(size.x, size.y);
                manager.setBitmap(image);
            }
        }
        catch (Throwable e)
        {
            Log.info(this, "error on drawImage", e);
        }
        finally
        {
            if (image != null)
            {
                image.recycle();
            }
        }
    }

    private Point getDisplaySize()
    {
        android.util.DisplayMetrics metrics = new android.util.DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(metrics);
        return new Point(metrics.widthPixels, metrics.heightPixels);
    }
}
