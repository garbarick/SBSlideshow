package ru.net.serbis.slideshow;
import android.content.*;

public class Runner
{
    private boolean progress;
    protected Images images;
    protected Context context;
    
    public Runner(Context context)
    {
        images = new Images(context);
        this.context = context;
    }
    
    public void runAction(Action action)
    {
        if (!isProgress())
        {
            try
            {
                setProgress(true);
                runActionWithoutCheck(action);
            }
            finally
            {
                setProgress(false);
            }
        }
    }

    private void runAction(Runnable runnable)
    {
        new Thread(runnable).start();
    }

    public void runActionInThread(final Action action)
    {
        runAction(new Runnable()
            {
                public void run()
                {
                    runAction(action);
                }
            });
    }

    private synchronized boolean isProgress()
    {
        return progress;
    }

    private synchronized void setProgress(boolean progress)
    {
        this.progress = progress;
    }
    
    private void runActionWithoutCheck(Action action)
    {
        switch (action)
        {
            case Previous:
                previousAction();
                break;

            case Next:
                nextAction();
                break;

            case Open:
                openAction();
                break;

            case Delete:
                deleteAction();
                break;

            case Refresh:
                refreshAction();
                break;

            case Draw:
                drawAction();
                break;
        }
    }
    
    protected void previousAction()
    {
        images.previous();
        drawAction();
    }
    
    protected void nextAction()
    {
        images.next();
        drawAction();
    }
    
    protected void openAction()
    {
        images.open();
    }
 
    protected void deleteAction()
    {
        images.deleteCurrent();
        drawAction();
    }
    
    protected void refreshAction()
    {
        images.init();
        drawAction();
    }
    
    protected void drawAction()
    {
    }
}
