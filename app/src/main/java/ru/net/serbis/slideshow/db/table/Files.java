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
    private static final String TEMP2 = "temp2";
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
		if (!isTableExist(db, CURRENT))
		{
			createCurrentTable(db);
		}
	}

	private void createFilesTable(SQLiteDatabase db)
    {
        db.execSQL("create table " + FILES + "(id integer primary key autoincrement, path text)");
    }

	private void createCurrentTable(SQLiteDatabase db)
    {
        db.execSQL("create table " + CURRENT + "(path_id integer)");
    }

    private void createTempTable(SQLiteDatabase db)
    {
        db.execSQL("create table " + TEMP + "(path text)");
    }
    
    private void createTemp2Table(SQLiteDatabase db)
    {
        db.execSQL("create table " + TEMP2 + "(path text)");
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
        
        boolean hasNext = hasNext(db);
        if (hasNext)
        {
            dropTable(db, TEMP2);
            createTemp2Table(db);
            executeUpdate(
                db,
                "insert into " + TEMP2 + "(path)" +
                " select path from " + FILES +
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

        insertFile = db.compileStatement("insert into " + TEMP + "(path) values(?)");
		finder.find(this);

        if (hasNext)
        {
            excludeTableFromTable(db, TEMP, FILES);
            excludeTableFromTable(db, TEMP2, TEMP);
            excludeTableFromTable(db, TEMP, TEMP2);
            executeUpdate(
                db,
                "insert into " + TEMP + "(path)" +
                "select path from " + TEMP2);
            dropTable(db, TEMP2);
        }
        
        executeUpdate(
            db,
            "insert into " + FILES + "(path)" +
            " select path from " + TEMP + " order by random()");
        count(db, FILES);
        
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
        finder.finish();
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
}
