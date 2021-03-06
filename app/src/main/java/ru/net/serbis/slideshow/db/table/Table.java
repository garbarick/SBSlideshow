package ru.net.serbis.slideshow.db.table;

import android.database.*;
import android.database.sqlite.*;
import ru.net.serbis.slideshow.*;
import ru.net.serbis.slideshow.db.*;
import ru.net.serbis.slideshow.tools.*;

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

	protected void executeUpdate(final String query, final String... args)
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

    protected void executeUpdate(int queryResource, String... args)
    {
        executeUpdate(Utils.getRaw(helper, queryResource), args);
    }

	protected void executeUpdate(SQLiteDatabase db, String query, String... args)
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

    protected void executeUpdate(SQLiteDatabase db, int queryResource, String... args)
    {
        executeUpdate(db, Utils.getRaw(helper, queryResource), args);
    }

	protected boolean isExist(final String query, final String... args)
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

    protected boolean isExist(int queryResource, String... args)
    {
        return isExist(Utils.getRaw(helper, queryResource), args);
    }

    protected boolean isExist(SQLiteDatabase db, String query, String... args)
    {
        Cursor cursor = query(db, query, args);
        return cursor.moveToFirst();
	}

    protected boolean isExist(SQLiteDatabase db, int queryResource, String... args)
    {
        return isExist(db, Utils.getRaw(helper, queryResource), args);
    }

    protected Cursor query(SQLiteDatabase db, String query, String... args)
    {
        return db.rawQuery(query, args);
    }

    protected Cursor query(SQLiteDatabase db, int queryResource, String... args)
    {
        return query(db, Utils.getRaw(helper, queryResource), args);
    }

	protected String selectValue(final String query, final String... args)
    {
		return execute(
			new Executer<String>()
			{
			    public String execute(SQLiteDatabase db)
				{
					return selectValue(db, query, args);
				}
			}
		);
    }

    protected String selectValue(int queryResource, String... args)
    {
        return selectValue(Utils.getRaw(helper, queryResource), args);
    }

    protected String selectValue(SQLiteDatabase db, String query, String... args)
    {
        Cursor cursor = query(db, query, args);
        if (cursor.moveToFirst())
        {
            return cursor.getString(0);
        }
        return null;
    }

	protected boolean isTableExist(SQLiteDatabase db, String table)
	{
		return isExist(db, R.raw.is_table_exist, table);
	}

    protected boolean isCoumnExist(SQLiteDatabase db, String table, String column)
    {
        Cursor cursor = query(db, "pragma table_info(" + table + ")");
        if (cursor.moveToFirst())
        {
            do
            {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                if (column.equalsIgnoreCase(name))
                {
                    return true;
                }
            }
            while(cursor.moveToNext());
        }
        return false;
    }

    protected void addColumn(SQLiteDatabase db, String table, String column, String type)
    {
        if (isCoumnExist(db, table, column))
        {
            return;
        }
        db.execSQL(
            Utils.getRaw(
                helper, R.raw.add_column,
                "{table}", table,
                "{column}", column,
                "{type}", type));
    }
}
