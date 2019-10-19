package ru.net.serbis.slideshow.listener;

import android.hardware.*;
import android.content.*;

public class ShakeListener implements SensorListener
{
    public interface OnShakeListener
    {
        public void onShake();
    }

    protected Context context;
    protected OnShakeListener listener;

    public ShakeListener(Context context, OnShakeListener listener)
    {
        this.context = context;
        this.listener = listener;
    }

    public void start()
    {
        SensorManager manager = getSensorManager();
        if (manager == null)
        {
            return;
        }
        boolean result = manager.registerListener(this, SensorManager.SENSOR_ACCELEROMETER, SensorManager.SENSOR_DELAY_GAME);
        if (!result)
        {
            stop();
        }
    }

    public void stop()
    {
        SensorManager manager = getSensorManager();
        if (manager == null)
        {
            return;
        }
        manager.unregisterListener(this, SensorManager.SENSOR_ACCELEROMETER);
    }

    private SensorManager getSensorManager()
    {
        return (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    public void onSensorChanged(int sensor, float[] values)
    {
        switch(sensor)
        {
            case SensorManager.SENSOR_ACCELEROMETER:
                onAccelerometerChange(values);
                break;
        }
    }

    protected static final float GRAVITY = 2.7f;
    protected static final int TIME_OUT = 500;
    
    protected long lastTime;
    
    protected void onAccelerometerChange(float[] values)
    {
        long now = System.currentTimeMillis();
        long interval = now - lastTime;
        if (interval <= TIME_OUT)
        {
            return;
        }
        
        float x = values[SensorManager.DATA_X] / SensorManager.GRAVITY_EARTH;
        float y = values[SensorManager.DATA_Y] / SensorManager.GRAVITY_EARTH;
        float z = values[SensorManager.DATA_Z] / SensorManager.GRAVITY_EARTH;
        
        float force = Math.abs(x * x + y * y + z* z);
        if (force <= GRAVITY)
        {
            return;
        }
        
        lastTime = now;
        listener.onShake();
    }

    @Override
    public void onAccuracyChanged(int sensor, int accuracy)
    {
    }
}
