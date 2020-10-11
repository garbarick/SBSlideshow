package ru.net.serbis.slideshow.activity;

import ru.net.serbis.slideshow.adapter.*;

public class Parameters extends Base<ParameterData>
{
    @Override
    protected Adapter<ParameterData> getAdapter()
    {
        return new ParametersAdapter(this);
    }
}
