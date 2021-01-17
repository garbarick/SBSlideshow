package ru.net.serbis.slideshow.db.table;

import android.database.*;
import android.database.sqlite.*;
import java.util.*;
import ru.net.serbis.slideshow.*;
import ru.net.serbis.slideshow.data.*;
import ru.net.serbis.slideshow.db.*;

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

    public List<Info> get()
    {
        return execute(
            new Executer<List<Info>>()
            {
                public List<Info> execute(SQLiteDatabase db)
                {
                    List<Info> result = new ArrayList<Info>();
                    Cursor cursor = query(db, R.raw.information);
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
