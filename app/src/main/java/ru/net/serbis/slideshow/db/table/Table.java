package ru.net.serbis.slideshow.db.table;

import android.database.*;
import android.database.sqlite.*;
import ru.net.serbis.slideshow.db.*;

public abstract class Table
{
	protected interface Executer<T>
	{
		T execute(SQLiteDatabase db);
	}
	
	protected DBHelper helper;

	public Table(DBHelper helper)
	{
		this.helper = helper;
	}
	
	public abstract void init(SQLiteDatabase db);
	
	protected <T> T execute(Executer<T> executer, boolean write)
	{
		SQLiteDatabase db = write ? helper.getWritableDatabase() : helper.getReadableDatabase();
		try
        {
			return executer.execute(db);
        }
        finally
        {
            db.close();
        }
	}
	
	protected <T> T execute(Executer<T> executer)
	{
		return execute(executer, false);
	}
	
	public void executeUpdate(final String query, final String... args)
    {
		execute(
			new Executer<Void>()
			{
			    public Void execute(SQLiteDatabase db)
				{
					executeUpdate(db, query, args);
					return null;
				}
			},
			true
		);
    }
	
	public void executeUpdate(SQLiteDatabase db, String query, String... args)
    {
		if (args == null || args.length == 0)
		{
			db.execSQL(query);
		}
		else
		{
			SQLiteStatement statement = db.compileStatement(query);
			int i = 1;
			for (String arg: args)
			{
				statement.bindString(i, arg);
				i ++;
			}
			statement.execute();
		}
	}
		
	public boolean isExist(final String query, final String... args)
    {
		return execute(
			new Executer<Boolean>()
			{
			    public Boolean execute(SQLiteDatabase db)
				{
					return isExist(db, query, args);
				}
			}
		);
    }
	
	private boolean isExist(SQLiteDatabase db, String query, String... args)
    {
		Cursor cursor = db.rawQuery(query, args);
		return cursor.moveToFirst();
	}
	
	public String selectValue(final String query, final String... args)
    {
		return execute(
			new Executer<String>()
			{
			    public String execute(SQLiteDatabase db)
				{
					Cursor cursor = db.rawQuery(query, args);
					if (cursor.moveToFirst())
					{
						return cursor.getString(0);
					}
					return null;
				}
			}
		);
    }

	protected boolean isTableExist(SQLiteDatabase db, String table)
	{
		return isExist(
			db,
			"select 1" +
			"  from sqlite_master" +
			" where type = 'table'" +
			"   and name = ?",
			table);
	}
}
