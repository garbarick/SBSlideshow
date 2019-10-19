package ru.net.serbis.slideshow.adapter;

import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.slideshow.*;

public class SpinnerAdapter extends ArrayAdapter<Integer>
{
	private static int layoutId = R.layout.value;

	private Map<Integer, Integer> data;
	
	public SpinnerAdapter(Context context, Map<Integer, Integer> data)
	{
		super(context, layoutId);
        this.data = data;
		addAll(data.keySet());
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
		text.setText(data.get(value));
		
		return view;
	}

	@Override
	public View getDropDownView(int position, View view, ViewGroup parent)
	{
		return getView(position, view, parent);
	}
}
