package ru.net.serbis.slideshow.data;

import ru.net.serbis.slideshow.*;

public enum FolderType
{
	System(R.string.folder_type_system),
	Mega(R.string.folder_type_mega),
	Default(R.string.folder_type_default);

	private int text;
	
	public FolderType(int text)
	{
		this.text = text;
	}
	
	public int getText()
	{
		return text;
	}
}
