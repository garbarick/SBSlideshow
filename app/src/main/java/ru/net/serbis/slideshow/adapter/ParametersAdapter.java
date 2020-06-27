package ru.net.serbis.slideshow.adapter;

import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.slideshow.*;
import ru.net.serbis.slideshow.data.*;
import ru.net.serbis.slideshow.db.*;
import ru.net.serbis.slideshow.db.table.*;
import ru.net.serbis.slideshow.service.*;
import ru.net.serbis.slideshow.tools.*;

public class ParametersAdapter extends ArrayAdapter<ParameterView>
{
    private static List<ParameterView> getList()
    {
        List<ParameterView> result = new ArrayList<ParameterView>();
        result.add(new ParameterView(R.string.orientation, R.layout.param_spinner, Constants.ORIENTATION, Constants.ORIENTATIONS));
        result.add(new ParameterView(R.string.double_click_change, R.layout.param_switch, Constants.DOUBLE_CLICK_CHANGE));
        result.add(new ParameterView(R.string.shake_change, R.layout.param_switch, Constants.SHAKE_CHANGE));
        return result;
    };
    
    private Parameters parameters;
    
    public ParametersAdapter(Context context)
    {
        super(context, 0, getList());
        parameters = new DBHelper(context).parameters;
        initParameters();
    }
    
    private void initParameters()
    {
        for(int i = 0; i < getCount(); i++)
        {
            ParameterView view = getItem(i);
            Parameter param = view.getParam();
            param.setValue(parameters.getValue(param));
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        ParameterView paramView = getItem(position);
        view = LayoutInflater.from(getContext()).inflate(paramView.getLayoutId(), null);
        
        TextView text = UITools.findView(view, R.id.name);
        text.setText(paramView.getNameId());

        switch(paramView.getLayoutId())
        {
            case R.layout.param_switch:
                initSwitch(view, paramView);
                break;
                
            case R.layout.param_spinner:
                initSpinner(view, paramView);
        }
        
        return view;
	}

    private void initSwitch(View view, ParameterView paramView)
    {
        final Parameter param = paramView.getParam();
        Switch item = UITools.findView(view, R.id.value);
        item.setChecked(param.getBoolValue());
        item.setOnCheckedChangeListener(
            new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton button, boolean value)
                {
                    param.setValue(value);
                    parameters.setValue(param);
                    switchLogic(param);
                }
            }
        );
    }
    
    private void initSpinner(View view, ParameterView paramView)
    {
        final Parameter param = paramView.getParam();
        Spinner item = UITools.findView(view, R.id.value);
        final SpinnerAdapter adapter = new SpinnerAdapter(getContext(), paramView.getData());
        item.setAdapter(adapter);
        int current = param.getIntValue();
        item.setSelection(adapter.getPosition(current));
        item.setOnItemSelectedListener(
            new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(AdapterView parent, View view, int position, long id)
                {
                    param.setValue(adapter.getItem(position));
                    parameters.setValue(param);
                }

                @Override
                public void onNothingSelected(AdapterView parent)
                {
                }
            }
		);
    }
    
    private void switchLogic(Parameter param)
    {
        ImageService service = ImageService.getInstance();
        if (service == null)
        {
            return;
        }
        if (Constants.DOUBLE_CLICK_CHANGE.equals(param))
        {
            service.switchDoubleClickListener();
        }
        else if (Constants.SHAKE_CHANGE.equals(param))
        {
            service.switchShakeListener();
        }
    }
}
