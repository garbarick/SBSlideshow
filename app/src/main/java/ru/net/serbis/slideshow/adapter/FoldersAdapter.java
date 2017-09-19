package ru.net.serbis.slideshow.adapter;

import android.content.*;
import android.view.*;
import android.widget.*;
import ru.net.serbis.slideshow.*;
import ru.net.serbis.slideshow.data.*;

public class FoldersAdapter extends ArrayAdapter<Item>
{
	private static int layoutId = R.layout.folder;
	
	private class Holder
	{
		private TextView path;
		private TextView type;
	}
	
	public FoldersAdapter(Context context)
	{
		super(context, layoutId);
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent)
	{
		Holder holder;
		if (view == null)
		{
			view = LayoutInflater.from(getContext()).inflate(layoutId, parent, false);
			holder = new Holder();
			holder.path = (TextView) view.findViewById(R.id.path);
			holder.type = (TextView) view.findViewById(R.id.type);

			view.setTag(holder);
		}
		else
		{
			holder = (Holder) view.getTag();
		}

		Item folder = getItem(position);
		holder.path.setText(folder.getPath());
		holder.type.setText(folder.getType().getText());
		
		return view;
	}
}
