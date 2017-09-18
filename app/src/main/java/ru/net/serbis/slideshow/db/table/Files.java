package ru.net.serbis.slideshow.db.table;

import android.database.*;
import android.database.sqlite.*;
import java.util.*;
import ru.net.serbis.slideshow.db.*;

public class Files extends Table
{
	public Files(DBHelper helper)
	{
		super(helper);
	}
	
	@Override
	public void init(SQLiteDatabase db)
	{
		if (!isTableExist(db, "files"))
		{
			createFilesTable(db);
		}
		if (!isTableExist(db, "current"))
		{
			createCutrentTable(db);
		}
	}
	
	private void createTables(SQLiteDatabase db)
    {
		createFilesTable(db);
		createCutrentTable(db);
    }
	
	private void createFilesTable(SQLiteDatabase db)
    {
        db.execSQL("create table files(id integer primary key autoincrement, path text)");
    }
	
	private void createCutrentTable(SQLiteDatabase db)
    {
        db.execSQL("create table current(path_id integer)");
    }

    private void dropTables(SQLiteDatabase db)
    {
        db.execSQL("drop table files");
        db.execSQL("drop table current");
    }
	
	public void initFiles(final List<String> files, boolean add)
    {
		if (add)
		{
			files.addAll(getFiles());
		}
		Collections.shuffle(files);
		execute(
			new Executer<Void>()
			{
			    public Void execute(SQLiteDatabase db)
				{
					initFiles(db, files);
					return null;
				}
			},
			true
		);
		executeUpdate(
			"insert into current(path_id)" +
			" select min(id) from files");
    }
	
	private List<String> getFiles()
	{
		return execute(
			new Executer<List<String>>()
			{
				public List<String> execute(SQLiteDatabase db)
				{
					List<String> result = new ArrayList<String>();
					Cursor cursor = db.rawQuery("select path from files", null);
					if (cursor.moveToFirst())
					{
						do
						{
							result.add(cursor.getString(0));
						}
						while(cursor.moveToNext());
					}
					return result;
				}
			}
		);
	}
	
	private void initFiles(SQLiteDatabase db, List<String> files)
    {
		dropTables(db);
		createTables(db);

		if (files.isEmpty())
		{
			return;
		}

		db.beginTransaction();
		SQLiteStatement insert = db.compileStatement("insert into files(path) values(?)");
		for (String file : files)
		{
			insert.clearBindings();
			insert.bindString(1, file);
			insert.execute();
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}

    public boolean hasNext()
    {
		return isExist(
			"select id" +
			"  from files," +
			"       current" +
			" where id > path_id limit 1");
    }
	
    public void next()
    {
		executeUpdate(
			"update current" +
			"   set path_id = (select min(id) from files where id > path_id)");
    }

    public boolean hasPrevious()
    {
		return isExist(
			"select id" +
			"  from files," +
			"       current" +
			" where id < path_id limit 1");
    }

    public void previous()
    {
		executeUpdate(
			"update current" +
			"   set path_id = (select max(id) from files where id < path_id)");
    }

    public String getCurrentPath()
    {
		return selectValue(
			"select path" +
			"  from files," +
			"       current" +
			" where id = path_id");
    }

    public void deleteCurrent()
    {
		executeUpdate(
			"delete from files" +
			" where id in (select path_id from current)");
    }
}
