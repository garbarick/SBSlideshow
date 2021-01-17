package ru.net.serbis.slideshow.tools;

import android.content.*;
import java.io.*;
import ru.net.serbis.slideshow.*;
import ru.net.serbis.slideshow.db.*;

public class Utils
{
    public static void close(Closeable o)
    {
        try
        {
            if (o != null)
            {
                o.close();
            }
        }
        catch (Throwable ignored)
        {
        }
    }

    public static String getString(InputStream stream)
    {
        try
        {
            byte[] data = new byte[stream.available()];
            stream.read(data);
            return new String(data);
        }
        catch(Exception e)
        {
            Log.error(Utils.class,e);
            return null;
        }
        finally
        {
            close(stream);
        }
    }
    
    public static String getRaw(Context context, int id, String... keyValues)
    {
        String result = getString(context.getResources().openRawResource(id));
        if (keyValues != null && keyValues.length > 0 && keyValues.length % 2 == 0)
        {
            for (int i = 0; i < keyValues.length; i += 2)
            {
                String key = keyValues[i];
                String value = keyValues[i + 1];
                result = result.replace(key, value);
            }
        }
        return result;
    }

    public static String getRaw(DBHelper helper, int id, String... keyValues)
    {
        return getRaw(helper.getContext(), id, keyValues);
    }
}
