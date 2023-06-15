package ru.net.serbis.slideshow;

import java.util.*;
import ru.net.serbis.slideshow.*;
import ru.net.serbis.slideshow.data.*;
import android.content.pm.*;

public interface Constants
{
	String WIDGET_PREFERENCE = "widget_preference";
	String TYPE = Constants.class.getPackage().getName();

	List<Integer> VIEWS = Arrays.asList
	(
		new Integer[]
		{
			R.id.id1,
			R.id.id2,
			R.id.id3,
			R.id.id4,
			R.id.id5,
			R.id.id6,
			R.id.id7
		}
	);

	List<String> EXTENSIONS = Arrays.asList
	(
		new String[]
		{
			"png",
			"jpg",
			"jpeg",
			"gif"
		}
	);

	int ERROR_FILE_NOT_FOUND = 401;
    
	Parameter ORIENTATION = new Parameter("orientation", ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    Map<Integer, Integer> ORIENTATIONS = new MapHolder<Integer, Integer>()
    {
        {
            put(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED, R.string.bydefault);
            put(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, R.string.portrait);
            put(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE, R.string.landscape);
        }
	}.get();

    Parameter DOUBLE_CLICK_CHANGE = new Parameter("double_click_change", true);
    Parameter SHAKE_CHANGE = new Parameter("shake_change", false);
}
