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
        Point size = new Point(canvas.getWidth(), canvas.getHeight());
        return getMatrix(image, size);
    }
    
	private Matrix getMatrix(Bitmap image, Point size)
	{
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
		matrix.postTranslate(size.x / 2, size.y / 2);
		return matrix;
	}

	private float getScale(boolean rotate, Point size, Bitmap image)
	{
		if (rotate)
		{
			return Math.max(
				(float) size.x / image.getHeight(),
				(float) size.y / image.getWidth());
		}
		else
		{
			return Math.max(
				(float) size.x / image.getWidth(),
				(float) size.y / image.getHeight());
		}
	}

	private int getOrientation(Point size)
	{
		if (size.x > size.y)
		{
			return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
		}
		return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
	}
    
    public Bitmap getScaled(Bitmap image, Point size)
    {
        Matrix matrix = getMatrix(image, size);
        Bitmap result = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, false);
        image.recycle();
        return result;
    }
    
    public void saveImage(Bitmap image, File file)
    {
        OutputStream out = null;
        try
        {
            out = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 85, out);
        }
        catch(Exception e)
        {
            Log.info(this, "error on saveImage", e);
        }
        finally
        {
            Utils.close(out);
        }
    }
}
