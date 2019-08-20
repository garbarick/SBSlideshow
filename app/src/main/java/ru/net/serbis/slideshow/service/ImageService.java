package ru.net.serbis.slideshow.service;

import android.content.*;
import android.graphics.*;
import android.service.wallpaper.*;
import android.view.*;
import java.util.*;
import ru.net.serbis.slideshow.*;
import ru.net.serbis.slideshow.data.*;
import ru.net.serbis.slideshow.image.*;

public class ImageService extends WallpaperService
{
    private static volatile ImageService instance;

    private List<SlideShowEngine> engines = new ArrayList<SlideShowEngine>();
    private SlideShowRunner runner = new SlideShowRunner(this);
    
    private class SlideShowEngine extends Engine
    {
        private GestureDetector doubleTapDetector;
        private boolean visible = true;

        @Override
        public void onCreate(SurfaceHolder surfaceHolder)
        {
            super.onCreate(surfaceHolder);
            doubleTapDetector = new GestureDetector(ImageService.this, new GestureDetector.SimpleOnGestureListener()
            {
                @Override
                public boolean onDoubleTap(MotionEvent e)
                {
                    runner.runAction(Action.Next);
                    return true;
                }
            });
            setTouchEventsEnabled(true);
            engines.add(this);
        }

        @Override
        public void onDestroy()
        {
            engines.remove(this);
            super.onDestroy();
        }

        @Override
        public void onVisibilityChanged(boolean visible)
        {
            super.onVisibilityChanged(visible);
            this.visible = visible;
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height)
        {
            super.onSurfaceChanged(holder, format, width, height);
            runner.runActionInThread(Action.Draw);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder)
        {
            super.onSurfaceDestroyed(holder);
            visible = false;
        }

        private void draw()
        {
            if (visible)
            {
				runner.images.initCurrent(
					new Maker()
					{
						public void make(String fileName)
						{
							runner.drawCurrent(SlideShowEngine.this, fileName);
						}
					}
				);
            }
        }

        @Override
        public void onTouchEvent(MotionEvent event)
        {
            super.onTouchEvent(event);
            doubleTapDetector.onTouchEvent(event);
        }
    }
    
    private class SlideShowRunner extends Runner
    {
        public SlideShowRunner(Context context)
        {
            super(context);
        }

        @Override
        public void drawAction()
        {
            draw();
        }
		
		public void drawCurrent(SlideShowEngine engine, String fileName)
		{
			SurfaceHolder holder = engine.getSurfaceHolder();
			Canvas canvas = null;
			try
			{
				canvas = holder.lockCanvas();
				drawer.drawImage(canvas, fileName);
			}
			catch (Throwable e)
			{
				Log.info(this, "error on draw", e);
			}
			finally
			{
				if (canvas != null)
				{
					holder.unlockCanvasAndPost(canvas);
				}
			}
		}
    }

    public static ImageService getInstance()
    {
        return instance;
    }

    @Override
    public Engine onCreateEngine()
    {
        return new SlideShowEngine();
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        instance = null;
    }

	@Override
	public void onTaskRemoved(Intent rootIntent)
	{
		super.onTaskRemoved(rootIntent);
		instance = null;
	}
    
    private void draw()
    {
        for (SlideShowEngine engine : engines)
        {
            engine.draw();
        }
    }
    
    public Runner getRunner()
    {
        return runner;
    }
}
