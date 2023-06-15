package ru.net.serbis.slideshow.activity;

import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import ru.net.serbis.slideshow.*;
import ru.net.serbis.slideshow.adapter.*;
import ru.net.serbis.slideshow.data.*;
import ru.net.serbis.slideshow.extension.mega.*;
import ru.net.serbis.slideshow.extension.share.*;

import ru.net.serbis.slideshow.adapter.Adapter;

public class Folders extends Base<Item>
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        registerForContextMenu(getListView());
    }

    @Override
    protected Adapter<Item> getAdapter()
    {
        return new FoldersAdapter(this);
    }

    @Override
    protected void initAdapter()
    {
        super.initAdapter();
        refreshAdapter();
    }
    
    private void refreshAdapter()
    {
        adapter.setNotifyOnChange(false);
        adapter.clear();
        adapter.addAll(db.getFolders());
        adapter.setNotifyOnChange(true);
        adapter.notifyDataSetChanged();
	}

    @Override
    protected int getOptionMenuId()
    {
        return R.menu.folders;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        menu.findItem(R.id.add_mega_folder).setEnabled(app.getMegaConnection().isBound());
        menu.findItem(R.id.add_share_folder).setEnabled(app.getShareConnection().isBound());
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        Item folder = adapter.getItem(info.position);

        menu.setHeaderTitle(folder.getPath());
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.folder, menu);

        menu.findItem(R.id.exclude_folder).setEnabled(!FileType.Default.equals(folder.getType()));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (onItemMenuSelected(item.getItemId(), adapter.getItem(info.position)))
        {
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onItemMenuSelected(int id, Item item)
    {
        switch (id)
        {
            case R.id.add_system_folder:
                addSystemFolder();
                return true;

            case R.id.add_mega_folder:
            case R.id.add_share_folder:
                addExtFolder(id);
                return true;

            case R.id.exclude_folder:
                db.folders.excludeFolder(item);
                refreshAdapter();
                return true;
        }
        return super.onItemMenuSelected(id, item);
    }

    private void addSystemFolder()
    {
        new FileChooser(this, R.string.choose_folder, true)
        {
            public void onChoose(String path)
            {
                db.folders.addFolder(new Item(path, FileType.System));
                refreshAdapter();
            }
        };
    }

    private void addExtFolder(int id)
    {
        ExtFolders folders = null;
        switch(id)
        {
            case R.id.add_mega_folder:
                folders = new MegaFolders();
                break;

            case R.id.add_share_folder:
                folders = new ShareFolders();
                break;
        }
        if (folders != null)
        {
            startActivityForResult(folders.getFoldersIntent(), folders.getFolderResult());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (RESULT_OK == resultCode)
        {
            try
            {
                Item item = new MegaFolders().getItem(requestCode, data);
                if (item != null)
                {
                    db.folders.addFolder(item);
                    refreshAdapter();
                    return;
                }
                item = new ShareFolders().getItem(requestCode, data);
                if (item != null)
                {
                    db.folders.addFolder(item);
                    refreshAdapter();
                    return;
                }
            }
            catch (Throwable e)
            {
                Log.error(this, e);
            }
        }
    }
}
