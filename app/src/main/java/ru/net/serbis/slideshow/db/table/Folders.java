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

	public void addFolder(Folder folder)
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

	public void excludeFolder(Folder folder)
	{
		executeUpdate(
			"delete from folders where path = ?",
			folder.getPath());
	}

	public List<Folder> getFolders()
	{
		return execute(
			new Executer<List<Folder>>()
			{
				public List<Folder> execute(SQLiteDatabase db)
				{
					List<Folder> result = new ArrayList<Folder>();
					Cursor cursor = db.rawQuery("select path, type from folders order by path", null);
					if (cursor.moveToFirst())
					{
						do
						{
							Folder folder = new Folder(
								cursor.getString(0),
								FolderType.valueOf(cursor.getString(1)));
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
