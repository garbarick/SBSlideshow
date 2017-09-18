package ru.net.serbis.slideshow.data;

import ru.net.serbis.slideshow.*;

/**
 * SEBY0408
 */
public enum Action
{
    Separator(R.string.action_separator, R.drawable.separator),
    Previous(R.string.action_previous, R.drawable.previous),
    Next(R.string.action_next, R.drawable.next),
    Open(R.string.action_open, R.drawable.open),
    Delete(R.string.action_delete, R.drawable.delete),
    Refresh(R.string.action_refresh, R.drawable.refresh),
    Draw;

	private int text;
    private int drawable;

    private Action()
    {
    }

    private Action(int text, int drawable)
    {
		this.text = text;
        this.drawable = drawable;
    }

	public int getText()
	{
		return text;
	}
	
    public int getDrawable()
    {
        return drawable;
    }
	
	public static Action get(String name)
	{
		try
		{
			return valueOf(name);
		}
		catch (Throwable e)
		{
			return null;
		}
	}
}
