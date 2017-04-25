package ru.net.serbis.slideshow;

import java.util.HashMap;
import java.util.Map;

/**
 * SEBY0408
 */
public enum Action
{
    Separator("Separator", R.drawable.separator),
    Previous("Previous", R.drawable.previous),
    Next("Next", R.drawable.next),
    Open("Open", R.drawable.open),
    Delete("Delete", R.drawable.delete),
    Refresh("Refresh", R.drawable.refresh),
    Draw("Draw");

    private String name;
    private int resource;

    private static final Map<String, Action> actions = new HashMap<String, Action>()
    {
        {
            for (Action action : Action.values())
            {
                if (action.resource > 0)
                {
                    put(action.name, action);
                }
            }
        }
    };

    private Action(String name)
    {
        this.name = name;
    }

    private Action(String name, int resource)
    {
        this(name);
        this.resource = resource;
    }

    public String getName()
    {
        return name;
    }

    public int getResource()
    {
        return resource;
    }

    public static Action getAction(String name)
    {
        return actions.get(name);
    }
}
