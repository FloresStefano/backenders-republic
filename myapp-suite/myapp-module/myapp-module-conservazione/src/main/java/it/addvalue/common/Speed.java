package it.addvalue.common;

import java.util.concurrent.TimeUnit;

public class Speed
{

    public static long start()
    {
        return System.nanoTime();
    }

    public static long stop(long start)
    {
        return TimeUnit.MILLISECONDS.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS);
    }

}
