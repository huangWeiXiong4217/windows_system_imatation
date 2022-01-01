package utils;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class Test
{
    public static void main(String[] args)
    {
        Timer timer=new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                System.out.println("1");
            }
        },0,100);
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                System.out.println("2");
            }
        },200,100);
    }

}
