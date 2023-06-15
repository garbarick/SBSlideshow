package ru.net.serbis.slideshow.activity;

import android.content.*;
import ru.net.serbis.slideshow.data.*;

public interface ExtFolders
{
    Intent getFoldersIntent();
    int getFolderResult();
    Item getItem(int requestCode, Intent data);
    Item getItem(String path);
}
