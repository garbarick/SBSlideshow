package ru.net.serbis.slideshow;

import java.io.*;
import java.util.*;
import ru.net.serbis.slideshow.data.*;

public class FileHelper
{
    private FileHelper()
    {
    }

    private static String getExt(File file)
    {
        return getExt(file.getName());
    }

    private static String getExt(String fileName)
    {
        String ext = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0)
        {
            ext = fileName.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    public static void initWallpapers(Folder folder, List<String> fileNames)
    {
		switch(folder.getType())
		{
			case DEFAULT:
		  	case SYSTEM:
        		initFileNames(new File(folder.getPath()), fileNames);
				break;
		}
    }

    private static void initFileNames(File dir, List<String> fileNames)
    {
        File[] files = dir.listFiles();
        if (files != null)
        {
            for (File file : files)
            {
                if (file.isDirectory())
                {
                    initFileNames(file, fileNames);
                }
                else
                {
                    if (Constants.EXTENSIONS.contains(getExt(file)))
                    {
                        fileNames.add(file.getAbsolutePath());
                    }
                }
            }
        }
    }

    public static boolean exist(String fileName)
    {
        return fileName != null && new File(fileName).exists();
    }
}
