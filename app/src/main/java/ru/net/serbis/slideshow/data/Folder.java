package ru.net.serbis.slideshow.data;

public class Folder
{
	private String path;
	private FolderType type;

	public Folder(String path, FolderType type)
	{
		this.path = path;
		this.type = type;
	}

	public String getPath()
	{
		return path;
	}

	public FolderType getType()
	{
		return type;
	}

	@Override
	public String toString()
	{
		return path;
	}
}
