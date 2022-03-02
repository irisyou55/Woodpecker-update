package edu.ecnu.Woodpecker.systemfunction;

import edu.ecnu.Woodpecker.constant.LogLevelConstant;
import edu.ecnu.Woodpecker.log.Recorder;
import edu.ecnu.Woodpecker.log.WpLog;

import edu.ecnu.Woodpecker.util.Log;

public class Shell
{

    /**
     * 暂停当前线程指定的时间
     * 
     * @param time 时间
     * @param timeUnit 时间单位
     */
    public static void sleep(int time, String timeUnit)
    {
        if (timeUnit.equals("microsecond"))
        {
            time = time / 1000;
            sleep(time);
        }
        else if (timeUnit.equals("millisecond"))
        {
            sleep(time);
        }
        else if (timeUnit.equals("second"))
        {
            time = time * 1000;
            sleep(time);
        }
        else if (timeUnit.equals("minute"))
        {
            time = time * 1000 * 60;
            sleep(time);
        }
        else if (timeUnit.equals("hour"))
        {
            time = time * 1000 * 60 * 60;
            sleep(time);
        }
        else
        {
            System.out.println("单位错误");
            Recorder.FunctionRecord(Log.getRecordMetadata(), "time unit's input is wrong",
                    LogLevelConstant.ERROR);
        }
    }


    private static void sleep(int time)
    {
        try
        {
            Thread.sleep(time);
            Recorder.FunctionRecord(Log.getRecordMetadata(), "SLEEP " + time, LogLevelConstant.INFO);
        }
        catch (InterruptedException e)
        {
            WpLog.recordLog(LogLevelConstant.ERROR, WpLog.getExceptionInfo(e));
        }
    }
}
