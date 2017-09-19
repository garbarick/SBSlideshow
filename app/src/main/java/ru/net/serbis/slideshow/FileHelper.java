package ru.net.serbis.slideshow;

import java.io.*;
import java.util.*;
import ru.net.serbis.slideshow.data.*;

public class FileHelper
{
    private FileHelper()
    {
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

    public static void initWallpapers(Item folder, List<String> fileNames)
    {
		initFileNames(new File(folder.getPath()), fileNames);
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
                    if (checkExt(file))
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
	
	public static boolean checkExt(String fileName)
	{
		return Constants.EXTENSIONS.contains(getExt(fileName));
	}
	
	public static boolean checkExt(File file)
	{
		return checkExt(file.getName());
	}
	
	public static File getFile(String fileName)
    {
        if (exist(fileName))
        {
            return new File(fileName);
        }
        return null;
    }
}
