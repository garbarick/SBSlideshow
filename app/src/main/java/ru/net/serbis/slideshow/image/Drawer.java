package ru.net.serbis.slideshow.image;

import android.content.pm.*;
import android.graphics.*;
import java.io.*;
import ru.net.serbis.slideshow.*;
import ru.net.serbis.slideshow.db.table.*;

/**
 * SEBY0408
 */
public class Drawer
{
	private Parameters parameters;

	public Drawer(Parameters parameters)
	{
		this.parameters = parameters;
	}

    public void drawImage(Canvas canvas, String imageName) throws Exception
    {
        canvas.drawColor(Color.BLACK);
		File file = FileHelper.getFile(imageName);
		if (file != null)
        {
            drawImage(file, canvas);
        }
    }

	public Bitmap load(File source)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;

        return BitmapFactory.decodeFile(source.getAbsolutePath(), options);
    }

    private void drawImage(File file, Canvas canvas)
    {
        Bitmap image = null;
        try
        {
            image = load(file);
            if (image != null)
            {
                drawBitmap(image, canvas);
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

    private void drawBitmap(Bitmap image, Canvas canvas)
    {
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
		Matrix matrix = getMatrix(image, canvas);
		canvas.drawBitmap(image, matrix, paint);
		matrix.reset();
    }

    private Matrix getMatrix(Bitmap image, Canvas canvas)
	{
        android.util.Size size = new android.util.Size(canvas.getWidth(), canvas.getHeight());
        return getMatrix(image, size);
    }
    
	private Matrix getMatrix(Bitmap image, android.util.Size size)
	{
        //Log.info(this, "size=" + size);
		int orientation = parameters.getIntValue(Constants.ORIENTATION);
		boolean rotate = false;
		if (orientation != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED &&
		    orientation != getOrientation(size))
		{
			rotate = true;
		}

		float scale = getScale(rotate, size, image);
		Matrix matrix = new Matrix();

		matrix.postTranslate(-image.getWidth() / 2, -image.getHeight() / 2);
		matrix.postScale(scale, scale);
		if (rotate)
		{
			matrix.postRotate(-90);
		}
		matrix.postTranslate(size.getWidth() / 2, size.getHeight() / 2);
		return matrix;
	}

	private float getScale(boolean rotate, android.util.Size size, Bitmap image)
	{
		if (rotate)
		{
			return Math.max(
				(float) size.getWidth() / image.getHeight(),
				(float) size.getHeight() / image.getWidth());
		}
		else
		{
			return Math.max(
				(float) size.getWidth() / image.getWidth(),
				(float) size.getHeight() / image.getHeight());
		}
	}

	private int getOrientation(android.util.Size size)
	{
		if (size.getWidth() > size.getHeight())
		{
			return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
		}
		return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
	}
    
    public Bitmap getScaled(Bitmap image, android.util.Size size)
    {
        Matrix matrix = getMatrix(image, size);
        Bitmap result = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, false);
        image.recycle();
        return result;
    }
}
