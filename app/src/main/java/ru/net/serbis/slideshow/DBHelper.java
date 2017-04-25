package ru.net.serbis.slideshow;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;

import java.util.List;

/**
 * SEBY0408
 */
public class DBHelper extends SQLiteOpenHelper
{
    public DBHelper(Context context)
    {
        super(context, "db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }

    private void createTables(SQLiteDatabase db)
    {
        db.execSQL("create table files(id integer primary key autoincrement, path text)");
        db.execSQL("create table current(path_id integer)");
    }

    private void dropTables(SQLiteDatabase db)
    {
        db.execSQL("drop table files");
        db.execSQL("drop table current");
    }
    
    public void init(List<String> files)
    {
        SQLiteDatabase db = getWritableDatabase();
        try
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

            db.execSQL("insert into current(path_id) select min(id) from files");
        }
        finally
        {
            db.close();
        }
    }

    public boolean hasNext()
    {
        SQLiteDatabase db = getReadableDatabase();
        try
        {
            Cursor cursor = db.rawQuery("select id from files, current where id > path_id limit 1", null);
            return cursor.moveToFirst();
        }
        finally
        {
            db.close();
        }
    }

    public void next()
    {
        SQLiteDatabase db = getWritableDatabase();
        try
        {
            db.execSQL("update current set path_id = (select min(id) from files where id > path_id)");
        }
        finally
        {
            db.close();
        }
    }

    public boolean hasPrevious()
    {
        SQLiteDatabase db = getReadableDatabase();
        try
        {
            Cursor cursor = db.rawQuery("select id from files, current where id < path_id limit 1", null);
            return cursor.moveToFirst();
        }
        finally
        {
            db.close();
        }
    }

    public void previous()
    {
        SQLiteDatabase db = getWritableDatabase();
        try
        {
            db.execSQL("update current set path_id = (select max(id) from files where id < path_id)");
        }
        finally
        {
            db.close();
        }
    }

    public String getCurrentPath()
    {
        SQLiteDatabase db = getReadableDatabase();
        try
        {
            Cursor cursor = db.rawQuery("select path from files, current where id = path_id", null);
            if (cursor.moveToFirst())
            {
                return cursor.getString(0);
            }
            return null;
        }
        finally
        {
            db.close();
        }
    }

    public void deleteCurrent()
    {
        SQLiteDatabase db = getWritableDatabase();
        try
        {
            db.execSQL("delete from files where id in (select path_id from current)");
        }
        finally
        {
            db.close();
        }
    }
}
