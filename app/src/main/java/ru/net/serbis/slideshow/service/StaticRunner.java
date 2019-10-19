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
                Bitmap bitmap = drawer.load(file);
                bitmap = drawer.getScaled(bitmap, getDisplaySize());
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

    private Point getDisplaySize()
    {
        android.util.DisplayMetrics metrics = new android.util.DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(metrics);
        return new Point(metrics.widthPixels, metrics.heightPixels);
    }
}
