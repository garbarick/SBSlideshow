package ru.net.serbis.slideshow.db.table;

import android.database.*;
import android.database.sqlite.*;
import java.util.*;
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
		db.execSQL("create table folders(path text primary key, type text)");
	}

	public void addFolder(Item folder)
	{
		if (!isExist(
				"select 1 from folders where path = ?",
				folder.getPath()))
		{
			executeUpdate(
				"insert into folders(path, type)" +
				" values(?, ?)",
				folder.getPath(),
				folder.getType().toString());
		}
	}

	public void excludeFolder(Item folder)
	{
		executeUpdate(
			"delete from folders where path = ?",
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
					Cursor cursor = query(db, "select path, type from folders order by path");
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
