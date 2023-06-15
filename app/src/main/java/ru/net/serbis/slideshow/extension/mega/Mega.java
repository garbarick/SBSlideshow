package ru.net.serbis.slideshow.extension.mega;

public interface Mega
{
    String PACKAGE = "ru.net.serbis.mega";
    String SERVICE = PACKAGE + ".service.FilesService";
    String ACCOUNTS = PACKAGE + ".activity.Accounts";
    String SELECT_MODE = PACKAGE + ".SELECT_MODE";
    String ACTION = PACKAGE + ".ACTION";
    String SELECT_PATH = PACKAGE + ".SELECT_PATH";
    String PATH = PACKAGE + ".PATH";
    String FILES_LIST = PACKAGE + ".FILES_LIST";
    String FILE = PACKAGE + ".FILE";
    String RESULT = PACKAGE + ".RESULT";
    String ERROR_CODE = PACKAGE + ".ERROR_CODE";

    int ACTION_SELECT_ACCOUNT_PATH = 102;
    int ACTION_GET_FILES_LIST = 103;
    int ACTION_GET_FILE = 104;
    int ACTION_REMOVE_FILE = 105;

    int RESULT_CHOOSE_FOLDER = 100;

    String PREFIX = "//sbmega/";
    String SUCCESS = "SUCCESS";
}
