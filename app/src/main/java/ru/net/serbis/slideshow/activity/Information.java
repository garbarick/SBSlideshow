package ru.net.serbis.slideshow.activity;

import ru.net.serbis.slideshow.adapter.*;

public class Information extends Base
{
    @Override
    protected void initAdapter()
    {
        setListAdapter(new InformationAdapter(this, db));
    }
}
