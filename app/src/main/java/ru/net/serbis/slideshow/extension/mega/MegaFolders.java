package ru.net.serbis.slideshow.extension.mega;

import android.content.*;
import ru.net.serbis.slideshow.activity.*;
import ru.net.serbis.slideshow.data.*;

public class MegaFolders implements ExtFolders
{
    @Override
    public Intent getFoldersIntent()
    {
        Intent intent = new Intent();
        intent.setClassName(Mega.PACKAGE, Mega.ACCOUNTS);
        intent.putExtra(Mega.SELECT_MODE, true);
        intent.putExtra(Mega.ACTION, Mega.ACTION_SELECT_ACCOUNT_PATH);
        return intent;
    }

    @Override
    public int getFolderResult()
    {
        return Mega.RESULT_CHOOSE_FOLDER;
    }

    @Override
    public Item getItem(int requestCode, Intent data)
    {
        if (requestCode == Mega.RESULT_CHOOSE_FOLDER &&
            data.hasExtra(Mega.SELECT_PATH))
        {
            String path = data.getStringExtra(Mega.SELECT_PATH);
            return new Item(path, FileType.Mega);
        }
        return null;
    }

    @Override
    public Item getItem(String path)
    {
        if (path.startsWith(Mega.PREFIX))
        {
            return new Item(path, FileType.Mega);
        }
        return null;
    }
}
