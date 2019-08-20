package ru.net.serbis.slideshow.db.table;

import android.content.pm.*;
import android.database.sqlite.*;
import ru.net.serbis.slideshow.*;
import ru.net.serbis.slideshow.db.*;

public class Parameters extends Table
{
	public Parameters(DBHelper helper)
	{
		super(helper);
	}
	
	@Override
	public void init(SQLiteDatabase db)
	{
		if (!isTableExist(db, "parameters"))
		{
			createTable(db);
		}
	}

	private void createTable(SQLiteDatabase db)
	{
		db.execSQL("create table parameters(name text primary key, value text)");
	}
	
	public String getValue(String name, String defaultValue)
	{
		String result = selectValue("select value from parameters where name = ?", name);
		return result == null ? defaultValue : result;
	}
	
	public int getIntValue(String name, int defaultValue)
	{
		String result = getValue(name, null);
		return result == null ? defaultValue : Integer.parseInt(result);
	}
	
	public void setValue(String name, String value)
	{
		executeUpdate("delete from parameters where name = ?", name);
		executeUpdate("insert into parameters(name, value) values(?, ?)", name, value);
	}
	
	public void setValue(String name, int value)
	{
		setValue(name, String.valueOf(value));
	}
	
	public int getOrientation()
	{
		return getIntValue(Constants.ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
	}
	
	public void setOrientation(int value)
	{
		setValue(Constants.ORIENTATION, value);
	}
}
