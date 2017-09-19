package ru.net.serbis.slideshow.data;

import ru.net.serbis.slideshow.*;

public enum FileType
{
	System(R.string.file_type_system),
	Mega(R.string.file_type_mega),
	Default(R.string.file_type_default);

	private int text;
	
	public FileType(int text)
	{
		this.text = text;
	}
	
	public int getText()
	{
		return text;
	}
}
