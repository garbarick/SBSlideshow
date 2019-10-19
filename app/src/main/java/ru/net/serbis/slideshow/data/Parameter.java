package ru.net.serbis.slideshow.data;

public class Parameter
{
    protected String name;
    protected String value;

    public Parameter(String name)
    {
        this.name = name;
    }
    
    public Parameter(String name, String value)
    {
        this(name);
        setValue(value);
    }
    
    public Parameter(String name, int value)
    {
        this(name);
        setValue(value);
    }
    
    public Parameter(String name, boolean value)
    {
        this(name);
        setValue(value);
    }

    public String getName()
    {
        return name;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
    
    public void setValue(int value)
    {
        setValue(String.valueOf(value));
    }

    public void setValue(boolean value)
    {
        setValue(value ? 1 : 0);
    }
    
    public String getValue()
    {
        return value;
    }
    
    public int getIntValue()
    {
        return Integer.valueOf(getValue());
    }
    
    public boolean getBoolValue()
    {
        return getIntValue() == 1;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Parameter)
        {
            Parameter that = (Parameter) obj;
            return getName().equals(that.getName());
        }
        return super.equals(obj);
    }
}
