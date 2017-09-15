package ru.net.serbis.slideshow;

import java.util.*;
import ru.net.serbis.slideshow.*;

public interface Constants
{
	String WIDGET_PREFERENCE = "widget_preference";
	String MEGA_PACKAGE = "ru.net.serbis.mega";
	String MEGA_SERVICE = "ru.net.serbis.mega.service.FilesService";
	
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
}
