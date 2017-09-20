package ru.net.serbis.slideshow;

import java.io.*;
import java.util.*;
import ru.net.serbis.slideshow.data.*;
import ru.net.serbis.slideshow.db.table.*;

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
    
    public static void findFiles(List<Item> folders, Files files)
    {
        for (Item folder : folders)
        {
		    findFiles(new File(folder.getPath()), files);
        }
    }

    private static void findFiles(File dir, final Files files)
    {
        dir.listFiles(
            new FileFilter()
            {
                public boolean accept(File file)
                {
                    if (file.isDirectory())
                    {
                        findFiles(file, files);
                    }
                    else
                    {
                        if (checkExt(file))
                        {
                            files.addFile(file.getAbsolutePath());
                        }
                    }
                    return false;
                }
            }
        );
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
