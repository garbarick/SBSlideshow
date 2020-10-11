package ru.net.serbis.slideshow.activity;

import ru.net.serbis.slideshow.*;
import ru.net.serbis.slideshow.adapter.*;
import ru.net.serbis.slideshow.data.*;

public class Information extends Base<Info>
{
    @Override
    protected Adapter<Info> getAdapter()
    {
        return new InformationAdapter(this, db.information.get());
    }

    @Override
    protected int getOptionMenuId()
    {
        return R.menu.information;
    }

    @Override
    public boolean onItemMenuSelected(int id, Info item)
    {
        switch (id)
        {
            case R.id.refresh:
                initAdapter();
                return true;
        }
        return super.onItemMenuSelected(id, item);
    }
}
