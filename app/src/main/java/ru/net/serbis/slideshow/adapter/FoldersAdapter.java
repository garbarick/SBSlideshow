package ru.net.serbis.slideshow.adapter;

import android.content.*;
import android.view.*;
import android.widget.*;
import ru.net.serbis.slideshow.*;
import ru.net.serbis.slideshow.data.*;
import ru.net.serbis.slideshow.tools.*;

public class FoldersAdapter extends Adapter<Item>
{
	private class Holder
	{
		private TextView path;
		private TextView type;
	}
	
	public FoldersAdapter(Context context)
	{
		super(context, R.layout.folder);
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent)
	{
		Holder holder;
		if (view == null)
		{
			view = makeView(parent);
			holder = new Holder();
			holder.path = UITools.get().findView(view, R.id.path);
			holder.type = UITools.get().findView(view, R.id.type);

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
