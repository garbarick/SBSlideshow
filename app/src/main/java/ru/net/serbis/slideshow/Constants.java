package ru.net.serbis.slideshow;

import java.util.*;
import ru.net.serbis.slideshow.*;

public interface Constants
{
	String WIDGET_PREFERENCE = "widget_preference";
	String TYPE = Constants.class.getPackage().getName();
	int CHOOSE_MEGA_FOLDER = 100;
	
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
	
	String MEGA_PACKAGE = "ru.net.serbis.mega";
	String MEGA_SERVICE = MEGA_PACKAGE + ".service.FilesService";
	String MEGA_ACCOUNTS = MEGA_PACKAGE + ".activity.Accounts";
	String MEGA_SELECT_MODE = MEGA_PACKAGE + ".SELECT_MODE";
	String MEGA_ACTION = MEGA_PACKAGE + ".ACTION";
	String MEGA_SELECT_PATH = MEGA_PACKAGE + ".SELECT_PATH";
	String MEGA_PATH = MEGA_PACKAGE + ".PATH";
	String MEGA_FILES_LIST = MEGA_PACKAGE + ".FILES_LIST";
	String MEGA_FILE = MEGA_PACKAGE + ".FILE";
	String MEGA_RESULT = MEGA_PACKAGE + ".RESULT";
	String MEGA_ERROR_CODE = MEGA_PACKAGE + ".ERROR_CODE";
	
	int MEGA_ACTION_SELECT_ACCOUNT_PATH = 102;
	int MEGA_ACTION_GET_FILES_LIST = 103;
	int MEGA_ACTION_GET_FILE = 104;
	int MEGA_ACTION_REMOVE_FILE = 105;
	
	String MEGA_PREFIX = "//sbmega/";
	String MEGA_SUCCESS = "SUCCESS";
	
	int MEGA_ERROR_FILE_NOT_FOUND = 401;
}
