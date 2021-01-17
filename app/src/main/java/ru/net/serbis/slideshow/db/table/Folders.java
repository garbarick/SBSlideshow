package ru.net.serbis.slideshow.db.table;

import android.database.*;
import android.database.sqlite.*;
import java.util.*;
import ru.net.serbis.slideshow.*;
import ru.net.serbis.slideshow.data.*;
import ru.net.serbis.slideshow.db.*;

public class Folders extends Table
{
	public Folders(DBHelper helper)
	{
		super(helper);
	}

	@Override
	public void init(SQLiteDatabase db)
	{
		if (!isTableExist(db, "folders"))
		{
			createTable(db);
		}
	}

	private void createTable(SQLiteDatabase db)
	{
        executeUpdate(db, R.raw.create_folders_table);
	}

	public void addFolder(Item folder)
	{
		if (!isExist(
				R.raw.is_folder_exist,
				folder.getPath()))
		{
			executeUpdate(
				R.raw.add_folder,
				folder.getPath(),
				folder.getType().toString());
		}
	}

	public void excludeFolder(Item folder)
	{
		executeUpdate(
			R.raw.exclude_folder,
			folder.getPath());
	}

	public List<Item> getFolders()
	{
		return execute(
			new Executer<List<Item>>()
			{
				public List<Item> execute(SQLiteDatabase db)
				{
					List<Item> result = new ArrayList<Item>();
					Cursor cursor = query(db, R.raw.get_folders);
					if (cursor.moveToFirst())
					{
						do
						{
							Item folder = new Item(
								cursor.getString(0),
								FileType.valueOf(cursor.getString(1)));
							result.add(folder);
						}
						while(cursor.moveToNext());
					}
					return result;
				}
			}
		);
	}
}
