package ru.net.serbis.slideshow.adapter;

import android.content.*;
import android.view.*;
import android.widget.*;
import ru.net.serbis.slideshow.*;
import ru.net.serbis.slideshow.data.*;
import ru.net.serbis.slideshow.tools.*;

public class ControlsAdapter extends Adapter<Action>
{
    private class Holder
    {
        private ImageView image;
        private TextView text;
	}

    public ControlsAdapter(Context context)
    {
        super(context, R.layout.control);
        init();
    }

    private void init()
    {
        for (String name : getContext().getResources().getStringArray(R.array.actions))
        {
            Action action = Action.get(name);
            if (action != null && !Action.Separator.equals(action))
            {
                add(action);
            }
        }
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
        holder.image.setImageResource(action.getDrawable());

        return view;
    }
}
