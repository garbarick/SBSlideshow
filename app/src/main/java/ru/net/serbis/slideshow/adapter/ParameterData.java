package ru.net.serbis.slideshow.adapter;

import ru.net.serbis.slideshow.data.*;
import java.util.*;

public class ParameterData
{
    private int nameId;
    private int layoutId;
    private Parameter param;
    private Map<Integer, Integer> data;

    public ParameterData(int nameId, int layourId, Parameter param)
    {
        this.nameId = nameId;
        this.layoutId = layourId;
        this.param = param;
    }
    
    public ParameterData(int nameId, int layoutId, Parameter param, Map<Integer, Integer> data)
    {
        this(nameId, layoutId, param);
        this.data = data;
    }

    public int getNameId()
    {
        return nameId;
    }

    public int getLayoutId()
    {
        return layoutId;
    }

    public Parameter getParam()
    {
        return param;
    }

    public Map<Integer, Integer> getData()
    {
        return data;
    }
}
