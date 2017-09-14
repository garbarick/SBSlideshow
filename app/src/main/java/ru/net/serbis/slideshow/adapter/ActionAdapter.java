package ru.net.serbis.slideshow.adapter;

import android.app.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.slideshow.*;

/**
 * SEBY0408
 */
public class ActionAdapter extends ArrayAdapter<Action> implements AdapterView.OnItemClickListener
{
    private Activity context;
    private int countChecked;
    private int maxCountChecked;
    private Map<Integer, Boolean> checked = new HashMap<Integer, Boolean>();
    private List<Integer> separators = new ArrayList<Integer>();

    public ActionAdapter(Activity context, int maxCountChecked)
    {
        super(context, R.layout.action);
        this.context = context;
        this.maxCountChecked = maxCountChecked;
        initItems();
    }

    private void initItems()
    {
        int position = 0;
        for (String name : context.getResources().getStringArray(R.array.actions))
        {
            Action action = Action.getAction(name);
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
        if (view == null)
        {
            view = context.getLayoutInflater().inflate(R.layout.action, null, true);
        }

        Action action = getItem(position);

        CheckedTextView checkText = (CheckedTextView) view.findViewById(R.id.text);
        checkText.setText(action.getName());
        checkText.setChecked(checked.get(position));

        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        imageView.setImageResource(action.getResource());

        return view;
    }

    public void onItemClick(AdapterView<?> adapter, View view, int position, long id)
    {
        toggle(position);
    }
}