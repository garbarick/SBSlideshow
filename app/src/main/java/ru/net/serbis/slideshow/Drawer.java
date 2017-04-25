package ru.net.serbis.slideshow;

import android.graphics.*;

/**
 * SEBY0408
 */
public class Drawer
{
    public void drawImage(Canvas canvas, String imageName) throws Exception
    {
        canvas.drawColor(Color.BLACK);
        if (FileHelper.exist(imageName))
        {
            drawImage(imageName, canvas);
        }
    }

    private void drawImage(String imageName, Canvas canvas)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;

        Bitmap image = null;
        try
        {
            image = BitmapFactory.decodeFile(imageName, options);
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
        Rect imageRect = new Rect(0, 0, image.getWidth(), image.getHeight());
        canvas.drawBitmap(image, imageRect, getCanvasRect(canvas, imageRect), paint);
    }

    private Rect getCanvasRect(Canvas canvas, Rect imageRect)
    {
        int screenWidth = canvas.getWidth();
        int screenHeight = canvas.getHeight();

        if (screenWidth == imageRect.width() && screenHeight == imageRect.height())
        {
            return new Rect(0, 0, screenWidth, screenHeight);
        }
        else
        {
            float coefficient = Math.max((float) screenWidth / imageRect.width(), (float) screenHeight / imageRect.height());
            int width = (int) (imageRect.width() * coefficient);
            int height = (int) (imageRect.height() * coefficient);
            int left = (screenWidth - width) / 2;
            int top = (screenHeight - height) / 2;
            int right = left + width;
            int bottom = top + height;

            return new Rect(left, top, right, bottom);
        }
    }
}
