package ru.net.serbis.slideshow.adapter;

import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.slideshow.*;
import ru.net.serbis.slideshow.data.*;
import ru.net.serbis.slideshow.tools.*;

public class ActionsAdapter extends Adapter<Action> implements AdapterView.OnItemClickListener
{
	private class Holder
	{
		private ImageView image;
		private CheckedTextView text;
	}
	
    private int countChecked;
    private int maxCountChecked;
    private Map<Integer, Boolean> checked = new HashMap<Integer, Boolean>();
    private List<Integer> separators = new ArrayList<Integer>();

    public ActionsAdapter(Context context, int maxCountChecked)
    {
        super(context, R.layout.action);
        this.maxCountChecked = maxCountChecked;
        init();
    }

    private void init()
    {
        int position = 0;
        for (String name : getContext().getResources().getStringArray(R.array.actions))
        {
            Action action = Action.get(name);
            if (action != null)
            {
                add(action);
                checked.put(position, false);

                if (Action.Separator.equals(action))
                {
                    separators.add(position);
                }
                position++;
            }
        }
        for (int i = 0; i < getCount() && countChecked < maxCountChecked; i++)
        {
            if (!separators.contains(i))
            {
                checked.put(i, true);
                countChecked++;
            }
        }
        for (int i = 0; i < separators.size() && countChecked < maxCountChecked; i++)
        {
            checked.put(separators.get(i), true);
            countChecked++;
        }
    }

    private void toggle(int position)
    {
        if (checked.get(position))
        {
            checked.put(position, false);
            countChecked--;
        }
        else if (countChecked == maxCountChecked)
        {
            checked.put(getCheckedPositions().iterator().next(), false);
            checked.put(position, true);
        }
        else
        {
            checked.put(position, true);
            countChecked++;
        }

        notifyDataSetChanged();
    }

    public List<Integer> getCheckedPositions()
    {
        List<Integer> result = new ArrayList<Integer>();
        for (Map.Entry<Integer, Boolean> entry : checked.entrySet())
        {
            if (entry.getValue())
            {
                result.add(entry.getKey());
            }
        }
        return result;
    }

    public List<String> getCheckedActions()
    {
        List<String> result = new ArrayList<String>();
        List<Integer> checked = getCheckedPositions();
        Collections.sort(checked);
        for (int position : checked)
        {
            result.add(getItem(position).name());
        }
        return result;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
		Holder holder;
		if (view == null)
		{
			view = makeView(parent);
			holder = new Holder();
			holder.image = UITools.get().findView(view, R.id.image);
			holder.text = UITools.get().findView(view, R.id.text);

			view.setTag(holder);
		}
		else
		{
			holder = (Holder) view.getTag();
		}

        Action action = getItem(position);

		holder.text.setText(action.getText());
        holder.text.setChecked(checked.get(position));
        holder.image.setImageResource(action.getDrawable());

        return view;
    }

    public void onItemClick(AdapterView<?> adapter, View view, int position, long id)
    {
        toggle(position);
    }
}
