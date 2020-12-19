package ru.net.serbis.slideshow.db.table;

import android.database.*;
import android.database.sqlite.*;
import android.text.*;
import java.util.*;
import ru.net.serbis.slideshow.*;
import ru.net.serbis.slideshow.data.*;
import ru.net.serbis.slideshow.db.*;

public class Files extends Table
{
    private static final String FILES = "files";
    private static final String TEMP = "temp";
    private static final String CURRENT = "current";
    
    private SQLiteStatement insertFile;

	public Files(DBHelper helper)
	{
		super(helper);
	}

	@Override
	public void init(SQLiteDatabase db)
	{
		if (!isTableExist(db, FILES))
		{
			createFilesTable(db);
		}
        addColumn(db, FILES, "exist", "integer default 0");
		if (!isTableExist(db, CURRENT))
		{
			createCurrentTable(db);
		}
	}

	private void createFilesTable(SQLiteDatabase db)
    {
        db.execSQL("create table " + FILES + "(id integer primary key autoincrement, path text, exist integer default 0)");
    }

	private void createCurrentTable(SQLiteDatabase db)
    {
        db.execSQL("create table " + CURRENT + "(path_id integer)");
    }

    private void createTempTable(SQLiteDatabase db)
    {
        db.execSQL("create table " + TEMP + "(path text, exist integer)");
    }

    private void dropTable(SQLiteDatabase db, String table)
    {
        if (isTableExist(db, table))
        {
            db.execSQL("drop table " + table);
        }
    }

	public void initFiles(final FilesFinder finder)
    {
		execute(
			new Executer<Void>()
			{
			    public Void execute(SQLiteDatabase db)
				{
					initFiles(db, finder);
					return null;
				}
			},
			true
		);
    }

	private void initFiles(SQLiteDatabase db, FilesFinder finder)
    {
        Log.info(this, "start initFiles");
        db.beginTransaction();

        dropTable(db, TEMP);
        createTempTable(db);

        insertFile = db.compileStatement("insert into " + TEMP + "(path, exist) values(?, 1)");
		finder.find(this);

        boolean hasNext = hasNext(db);
        if (hasNext)
        {
            updateExist(db, FILES, TEMP);
            excludeTableFromTable(db, TEMP, FILES);
            executeUpdate(
                db,
                "insert into " + TEMP + "(path, exist)" +
                " select path, exist from " + FILES +
                "  where id > (" +
                " select path_id from " + CURRENT + ")");
            executeUpdate(
                db,
                "delete from " + FILES +
                " where id > (" +
                " select path_id from " + CURRENT + ")");
        }
        else
        {
            dropTable(db, FILES);        
            createFilesTable(db);
        }
        
        executeUpdate(
            db,
            "insert into " + FILES + "(path, exist)" +
            " select path, exist from " + TEMP + " order by random()");
        
        if (hasNext)
        {
        }
        else
        {
            executeUpdate(
                db,
                "update " + CURRENT +
                "   set path_id = (" +
                " select min(id) from " + FILES + ")");
        }

        dropTable(db, TEMP);

        db.setTransactionSuccessful();
        db.endTransaction();
        Log.info(this, "finish initFiles");
    }

    public void addFile(String fileName)
    {
        insertFile.clearBindings();
        insertFile.bindString(1, fileName);
        insertFile.execute();
    }

    private void count(SQLiteDatabase db, String table)
    {
        String count = selectValue(db, "select count(1) from " + table);
        Log.info(this, "count in " + table + ": " + count);
    }

    private void excludeTableFromTable(SQLiteDatabase db, String fromTable, String byTable)
    {
        executeUpdate(
            db,
            "delete from " + fromTable +
            " where path in (" +
            "select path from " + byTable + ")");
    }

    private void updateExist(SQLiteDatabase db, String table, String byTable)
    {
        executeUpdate(
            db,
            "update " + table +
            " set exist = 1" +
            " where path in (" +
            "select path from " + byTable + ")");        
    }

    private String HAS_NEXT =
        "select id" +
        "  from " + FILES + "," + CURRENT +
        " where id > path_id limit 1";

    public boolean hasNext()
    {
        return isExist(HAS_NEXT);
    }

    private boolean hasNext(SQLiteDatabase db)
    {
        return isExist(db, HAS_NEXT);
    }

    public void next()
    {
		executeUpdate(
            "update " + CURRENT +
            "   set path_id = (" +
            "select min(id) from " + FILES +
            " where id > path_id)");
    }

    public boolean hasPrevious()
    {
        return isExist(
            "select id" +
            "  from " + FILES + "," + CURRENT +
            " where id < path_id limit 1");
    }

    public void previous()
    {
        executeUpdate(
            "update " + CURRENT +
            "   set path_id = (" +
            "select max(id) from " + FILES +
            " where id < path_id)");
    }

    public Item getCurrentItem()
    {
		String current = selectValue(
			"select path" +
            "  from " + FILES + "," + CURRENT +
			" where id = path_id");
        
        Log.info(this, "current=" + current);
        
		FileType type = !TextUtils.isEmpty(current) &&
			current.startsWith(Constants.MEGA_PREFIX) ?
			FileType.Mega : FileType.System;

		return new Item(current, type);
    }

    public void deleteCurrent()
    {
		executeUpdate(
			"delete from " + FILES +
			" where id in (" +
            "select path_id from " + CURRENT + ")");
    }

    public void clearExist()
    {
        executeUpdate("update " + FILES + " set exist = 0");
    }

    public void excludeNoExist()
    {
        executeUpdate("delete from " + FILES + " where exist = 0");
    }
}
