package ru.net.serbis.slideshow.adapter;

import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.slideshow.*;
import ru.net.serbis.slideshow.tools.*;

public class SpinnerAdapter extends Adapter<Integer>
{
	private Map<Integer, Integer> data;
	
	public SpinnerAdapter(Context context, Map<Integer, Integer> data)
	{
		super(context, R.layout.value);
        this.data = data;
		addAll(data.keySet());
	}

	@Override
	public View getView(int position, View view, ViewGroup parent)
	{
		if (view == null)
		{
			view = makeView(parent);
		}
		
		Integer value = getItem(position);
		TextView text = UITools.findView(view, R.id.value);
		text.setText(data.get(value));
		
		return view;
	}

	@Override
	public View getDropDownView(int position, View view, ViewGroup parent)
	{
		return getView(position, view, parent);
	}
}
