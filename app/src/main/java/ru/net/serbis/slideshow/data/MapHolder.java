package ru.net.serbis.slideshow.data;

import java.util.*;

public class MapHolder<K, V>
{
    protected Map<K, V> map = new LinkedHashMap<K, V>();

    public void put(K key, V value)
    {
        map.put(key, value);
    }

    public Map<K, V> get()
    {
        return map;
    }
}
