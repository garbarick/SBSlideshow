package ru.net.serbis.slideshow.activity;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import ru.net.serbis.slideshow.*;
import ru.net.serbis.slideshow.adapter.*;
import ru.net.serbis.slideshow.data.*;
import ru.net.serbis.slideshow.db.*;
import ru.net.serbis.slideshow.mega.*;

public class Settings extends Activity
{
	private ListView list;
	private FoldersAdapter adapter;
	private DBHelper db;
	private MegaConnection connection = new MegaConnection();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		db = new DBHelper(this);
		list = (ListView) findViewById(R.id.list);
		adapter = new FoldersAdapter(this);
		list.setAdapter(adapter);
		adapter.addAll(db.getFolders());
		
		registerForContextMenu(list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.folders, menu);
		
		menu.findItem(R.id.add_sbmega_folder).setEnabled(connection.isBound());
		
		return true;
	}
	
	@Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo)
    {
        if (view.getId() == R.id.list)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			Folder folder = adapter.getItem(info.position);
            
			menu.setHeaderTitle(folder.getPath());
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.folder, menu);
			
			menu.findItem(R.id.exclude_folder).setEnabled(!FolderType.DEFAULT.equals(folder.getType()));
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
	
	public boolean onItemMenuSelected(int id, Folder item)
    {
		switch(id)
		{
			case R.id.add_system_folder:
				return true;
				
			case R.id.add_sbmega_folder:
				return true;
				
			case R.id.exclude_folder:
				return true;
		}
        return false;
    }
	
	@Override
    protected void onStart()
    {
        super.onStart();
        Intent intent = new Intent();
		intent.setClassName(Constants.MEGA_PACKAGE, Constants.MEGA_SERVICE);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        if (connection.isBound())
        {
            unbindService(connection);
        }
    }
}
