package ru.net.serbis.slideshow.data;

public class Item
{
	private String path;
	private FileType type;

	public Item(String path, FileType type)
	{
		this.path = path;
		this.type = type;
	}

	public String getPath()
	{
		return path;
	}

	public FileType getType()
	{
		return type;
	}

	@Override
	public String toString()
	{
		return path;
	}
}
