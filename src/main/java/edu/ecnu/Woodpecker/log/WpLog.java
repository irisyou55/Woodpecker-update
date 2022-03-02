package edu.ecnu.Woodpecker.log;

import edu.ecnu.Woodpecker.sql.IdealResultSet;

import javax.servlet.jsp.jstl.sql.Result;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static edu.ecnu.Woodpecker.executor.parser.WoodpeckerParserConstants.EOF;


public class WpLog
{
    /**
     * input general log and log level
     * WpLog.recordLog(LogLevelConstant.INFO,"kill all servers of master");等价于
     * Recorder.FunctionRecord(Log.getRecordMetadata(), "kill all servers of master",
                    LogLevelConstant.INFO);
     * @param logLevel INFO, ERROR, WARN, DEBUG, FATAL
     * @param logInfo 
     * @param args output to logInfo
     */
    public static void recordLog(String logLevel, String logInfo, Object... args)
    {
        String text = String.format(logInfo, args);
        Recorder.FunctionRecord(getRecordMetadata(), text, logLevel);
    }

    /**
     * input ideal result set
     * @param idealResultSet
     */
    public static void recordIdealResultSet(String logLevel, IdealResultSet idealResultSet)
    {
        Recorder.SQLRecord(idealResultSet, getRecordMetadata(), logLevel);
    }

    /**
     * input query result set
     * @param result
     */
    public static void recordQueryResultSet(String logLevel, Result result)
    {
        Recorder.SQLRecord(result, getRecordMetadata(), logLevel);
    }

    /**
     * input query result set and ideal result set
     */
    public static void recordQueryAndIdealResultSet(String logLevel, Result result, IdealResultSet idealResultSet)
    {
        Recorder.SQLRecord(result, idealResultSet, getRecordMetadata(), logLevel);
    }


    /**
     * @param logInfo include group name and case file name, or cluster start stop info
     * @param args output to logInfo
     */
    public static void recordTestflow(String logInfo, Object... args)
    {
        Recorder.WorkflowControllerRecord(getRecordMetadata(), logInfo);
    }

    /**
     * get exception info and occur line
     * 
     * @return exception info and line number
     */
    public static String getExceptionInfo(Exception exception)
    {
        return String.format("%s ; %s", exception.toString(), exception.getStackTrace()[0].toString());
    }


    /**
     * generate general report
     */
    public static void generateReport(ArrayList<String> failedCase, ArrayList<String> parseErrorCase)
    {
        RecordReport.recordException(failedCase, parseErrorCase);
    }



    public static void deleteLog()
    {
        RecordAnalysis.deleteLog();
    }
    
    /**
     * get module info, include class name, method name and line number
     * @return module info
     */
    public static String getRecordMetadata()
    {
        StackTraceElement element = Thread.currentThread().getStackTrace()[3];// 3 means higher 2 level
        return String.format("%s->%s:%d", element.getClassName(), element.getMethodName(), element.getLineNumber());
    }
}
