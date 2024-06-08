package ru.net.serbis.slideshow.tools;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import ru.net.serbis.slideshow.*;

public class UITools
{
    private static final UITools instance = new UITools();
    private Handler handler = new Handler(Looper.getMainLooper());

    public static UITools get()
    {
        return instance;
    }

    public <T> T findView(View view, int id)
    {
        return (T) view.findViewById(id);
    }

    public <T> T findView(Activity view, int id)
    {
        return (T) view.findViewById(id);
    }

    public void toast(final Context context, final String text)
    {
        try
        {
            handler.post(
                new Runnable() {
                    public void run()
                    {
                        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
                    }
                });
        }
        catch (Exception e)
        {
            Log.error(this, e);
        }
    }
}
