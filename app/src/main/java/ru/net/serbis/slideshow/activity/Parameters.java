package ru.net.serbis.slideshow.activity;

import ru.net.serbis.slideshow.adapter.*;

public class Parameters extends Base
{
    @Override
    protected void initAdapter()
    {
        setListAdapter(new ParametersAdapter(this));
    }
}
