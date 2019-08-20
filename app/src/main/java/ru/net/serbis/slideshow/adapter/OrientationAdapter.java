package ru.net.serbis.slideshow.adapter;

import android.content.*;
import android.content.pm.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.slideshow.*;

public class OrientationAdapter extends ArrayAdapter<Integer>
{
	private static int layoutId = R.layout.value;

	private static Map<Integer, Integer> DATA = new LinkedHashMap<Integer, Integer>()
	{
		{
			put(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED, R.string.bydefault);
			put(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, R.string.portrait);
			put(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE, R.string.landscape);
		}
	};
	
	public OrientationAdapter(Context context)
	{
		super(context, layoutId);
		addAll(DATA.keySet());
	}

	@Override
	public View getView(int position, View view, ViewGroup parent)
	{
		if (view == null)
		{
			view = LayoutInflater.from(getContext()).inflate(layoutId, parent, false);
		}
		
		Integer value = getItem(position);
		TextView text = (TextView)view.findViewById(R.id.value);
		text.setText(DATA.get(value));
		
		return view;
	}

	@Override
	public View getDropDownView(int position, View view, ViewGroup parent)
	{
		return getView(position, view, parent);
	}
}
