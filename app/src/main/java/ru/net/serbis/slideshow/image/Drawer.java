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
		int orientation = parameters.getOrientation();
		boolean rotate = false;
		if (orientation != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED &&
		    orientation != getOrientation(canvas))
		{
			rotate = true;
		}

		float scale = getScale(rotate, canvas, image);
		Matrix matrix = new Matrix();

		matrix.postTranslate(-image.getWidth() / 2, -image.getHeight() / 2);
		matrix.postScale(scale, scale);
		if (rotate)
		{
			matrix.postRotate(-90);
		}
		matrix.postTranslate(canvas.getWidth() / 2, canvas.getHeight() / 2);
		return matrix;
	}

	private float getScale(boolean rotate, Canvas canvas, Bitmap image)
	{
		if (rotate)
		{
			return Math.max(
				(float) canvas.getWidth() / image.getHeight(),
				(float) canvas.getHeight() / image.getWidth());
		}
		else
		{
			return Math.max(
				(float) canvas.getWidth() / image.getWidth(),
				(float) canvas.getHeight() / image.getHeight());
		}
	}

	private int getOrientation(Canvas canvas)
	{
		if (canvas.getWidth() > canvas.getHeight())
		{
			return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
		}
		return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
	}
}
