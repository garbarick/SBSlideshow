package ru.net.serbis.slideshow.tools;

import android.view.*;

public class UITools
{
    public static <T> T findView(View view, int id)
    {
        return (T) view.findViewById(id);
    }
}
