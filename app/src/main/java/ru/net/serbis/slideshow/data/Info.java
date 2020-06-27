package ru.net.serbis.slideshow.data;

public class Info
{
    private String name;
    private String value;

    public Info(String name, String value)
    {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString()
    {
        return name + ": " + value;
    }
}
