package ru.net.serbis.slideshow.extension.share;

import android.content.*;
import ru.net.serbis.slideshow.activity.*;
import ru.net.serbis.slideshow.data.*;
import ru.net.serbis.slideshow.extension.share.*;

public class ShareFolders implements ExtFolders
{
    @Override
    public Intent getFoldersIntent()
    {
        Intent intent = new Intent();
        intent.setClassName(Share.PACKAGE, Share.ACCOUNTS);
        intent.putExtra(Share.SELECT_MODE, true);
        intent.putExtra(Share.ACTION, Share.ACTION_SELECT_ACCOUNT_PATH);
        return intent;
    }

    @Override
    public int getFolderResult()
    {
        return Share.RESULT_CHOOSE_FOLDER;
    }

    @Override
    public Item getItem(int requestCode, Intent data)
    {
        if (requestCode == Share.RESULT_CHOOSE_FOLDER &&
            data.hasExtra(Share.SELECT_PATH))
        {
            String path = data.getStringExtra(Share.SELECT_PATH);
            return new Item(path, FileType.Share);
        }
        return null;
    }

    @Override
    public Item getItem(String path)
    {
        if (path.startsWith(Share.PREFIX))
        {
            return new Item(path, FileType.Share);
        }
        return null;
    }
}
