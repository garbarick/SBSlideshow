package ru.net.serbis.slideshow.tools;

import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class UITools
{
    public static <T> T findView(View view, int id)
    {
        return (T) view.findViewById(id);
    }

    public static void toast(final Context context, final String text)
    {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(
            new Runnable() {
                public void run()
                {
                    Toast.makeText(context, text, Toast.LENGTH_LONG).show();
                }
            });
    }
}
