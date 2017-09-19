package ru.net.serbis.slideshow.activity;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import ru.net.serbis.slideshow.*;
import ru.net.serbis.slideshow.adapter.*;
import ru.net.serbis.slideshow.data.*;
import ru.net.serbis.slideshow.db.*;

public class Settings extends Activity
{
	private App app;
	private ListView list;
	private FoldersAdapter adapter;
	private DBHelper db;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		app = (App) getApplication();
		db = new DBHelper(this);
		list = (ListView) findViewById(R.id.list);
		adapter = new FoldersAdapter(this);
		list.setAdapter(adapter);
		initList();

		registerForContextMenu(list);
	}

	private void initList()
	{
		adapter.setNotifyOnChange(false);
		adapter.clear();
		adapter.addAll(db.getFolders());
		adapter.setNotifyOnChange(true);
		adapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.folders, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		menu.findItem(R.id.add_mega_folder).setEnabled(app.getMegaConnection().isBound());
		return true;
	}

	@Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo)
    {
        if (view.getId() == R.id.list)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			Item folder = adapter.getItem(info.position);

			menu.setHeaderTitle(folder.getPath());
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.folder, menu);

			menu.findItem(R.id.exclude_folder).setEnabled(!FileType.Default.equals(folder.getType()));
        }
    }

	@Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (onItemMenuSelected(item.getItemId(), null))
        {
            return true;
		}
		return super.onOptionsItemSelected(item);
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

	public boolean onItemMenuSelected(int id, Item folder)
    {
		switch (id)
		{
			case R.id.add_system_folder:
				addSystemFolder();
				return true;

			case R.id.add_mega_folder:
				addMegaFolder();
				return true;

			case R.id.exclude_folder:
				db.excludeFolder(folder);
				initList();
				return true;
		}
        return false;
    }

	private void addSystemFolder()
	{
        new FileChooser(this, R.string.choose_folder, true)
		{
			public void onChoose(String path)
			{
				db.addFolder(new Item(path, FileType.System));
				initList();
			}
		};
	}

	private void addMegaFolder()
	{
		Intent intent = new Intent();
		intent.setClassName(Constants.MEGA_PACKAGE, Constants.MEGA_ACCOUNTS);
		intent.putExtra(Constants.MEGA_SELECT_MODE, true);
        intent.putExtra(Constants.MEGA_ACTION, Constants.MEGA_ACTION_SELECT_ACCOUNT_PATH);
		startActivityForResult(intent, Constants.CHOOSE_MEGA_FOLDER);
	}

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (RESULT_OK == resultCode)
        {
			switch (requestCode)
			{
				case  Constants.CHOOSE_MEGA_FOLDER:
					{
						String path = data.getStringExtra(Constants.MEGA_SELECT_PATH);
						db.addFolder(new Item(path, FileType.Mega));
						initList();
					}
					break;
            }
		}
    }
}
