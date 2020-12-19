package ru.net.serbis.slideshow.db.table;

import android.database.*;
import android.database.sqlite.*;
import android.util.*;
import java.util.*;
import ru.net.serbis.slideshow.db.*;
import ru.net.serbis.slideshow.data.*;

public class Information extends Table
{
    public Information(DBHelper helper)
    {
        super(helper);
	}

    @Override
    public void init(SQLiteDatabase db)
    {
    }

    private String getQuery()
    {
        return "select " +
            "(select count(1) from files) \"Count Files\", " +
            "(select count(1) from files where id <= path_id) \"Passed Files\", " +
            "(select count(1) from files where id > path_id) \"Left Files\", " +
            "(select path from files where id = path_id) \"Current File\" " +
            "from current";
    }
    
    public List<Info> get()
    {
        return execute(
            new Executer<List<Info>>()
            {
                public List<Info> execute(SQLiteDatabase db)
                {
                    List<Info> result = new ArrayList<Info>();
                    Cursor cursor = query(db, getQuery());
                    if (cursor.moveToFirst())
                    {
                        for(int i = 0; i < cursor.getColumnCount(); i++)
                        {
                            Info info = new Info(cursor.getColumnName(i), cursor.getString(i));
                            result.add(info);
                        }
					}
                    return result;
                }
            }
		);
    }
}
