package ru.net.serbis.slideshow.db.table;

import android.database.sqlite.*;
import android.text.*;
import ru.net.serbis.slideshow.*;
import ru.net.serbis.slideshow.data.*;
import ru.net.serbis.slideshow.db.*;
import ru.net.serbis.slideshow.tools.*;

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
        executeUpdate(db, R.raw.create_files_table);
    }

	private void createCurrentTable(SQLiteDatabase db)
    {
        executeUpdate(db, R.raw.create_current_table);
    }

    private void createTempTable(SQLiteDatabase db)
    {
        executeUpdate(db, R.raw.create_temp_table);
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

        insertFile = db.compileStatement(Utils.getRaw(helper, R.raw.add_into_temp));
		finder.find(this);

        boolean hasNext = hasNext(db);
        if (hasNext)
        {
            updateExist(db, FILES, TEMP);
            excludeTableFromTable(db, TEMP, FILES);
            executeUpdate(db, R.raw.add_temp_files_after_current);
            executeUpdate(db, R.raw.exclude_files_after_current);
        }
        else
        {
            dropTable(db, FILES);        
            createFilesTable(db);
        }
        
        executeUpdate(db, R.raw.add_random_files);
        
        if (hasNext)
        {
        }
        else
        {
            executeUpdate(db, R.raw.update_current);
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

    private void excludeTableFromTable(SQLiteDatabase db, String table, String byTable)
    {
        executeUpdate(
            db,
            Utils.getRaw(
                helper, R.raw.exclude_table_by_table,
                "{table}", table,
                "{byTable}", byTable));
    }

    private void updateExist(SQLiteDatabase db, String table, String byTable)
    {
        executeUpdate(
            db,
            Utils.getRaw(
                helper, R.raw.update_exist,
                "{table}", table,
                "{byTable}", byTable));        
    }

    public boolean hasNext()
    {
        return isExist(R.raw.has_next);
    }

    private boolean hasNext(SQLiteDatabase db)
    {
        return isExist(db, R.raw.has_next);
    }

    public void next()
    {
		executeUpdate(R.raw.set_next);
    }

    public boolean hasPrevious()
    {
        return isExist(R.raw.had_previous);
    }

    public void previous()
    {
        executeUpdate(R.raw.set_previous);
    }

    public Item getCurrentItem()
    {
		String current = selectValue(R.raw.get_current_item);
        Log.info(this, "current=" + current);

		FileType type = !TextUtils.isEmpty(current) &&
			current.startsWith(Constants.MEGA_PREFIX) ?
			FileType.Mega : FileType.System;

		return new Item(current, type);
    }

    public void deleteCurrent()
    {
		executeUpdate(R.raw.delete_current);
    }

    public void clearExist()
    {
        executeUpdate(R.raw.clear_exist);
    }

    public void excludeNoExist()
    {
        executeUpdate(R.raw.exclude_no_exist);
    }
}
