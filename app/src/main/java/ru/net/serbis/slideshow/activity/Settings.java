package ru.net.serbis.slideshow.activity;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import ru.net.serbis.slideshow.*;
import ru.net.serbis.slideshow.data.*;
import ru.net.serbis.slideshow.db.*;

public class Settings extends Activity
{
	private ListView list;
	private ArrayAdapter<Folder> adapter;
	private DBHelper db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		db = new DBHelper(this);
		list = (ListView) findViewById(R.id.list);
		adapter = new ArrayAdapter<Folder>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
		list.setAdapter(adapter);
		adapter.addAll(db.getFolders());
		
		registerForContextMenu(list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.folders, menu);
		return true;
	}
	
	@Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo)
    {
        if (view.getId() == R.id.list)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(adapter.getItem(info.position).getPath());
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.folder, menu);
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
}
