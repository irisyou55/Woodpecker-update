package edu.ecnu.Woodpecker.executor.keyword;

import edu.ecnu.Woodpecker.constant.LogLevelConstant;
import edu.ecnu.Woodpecker.executor.Executor;
import edu.ecnu.Woodpecker.executor.GrammarType;
import edu.ecnu.Woodpecker.log.WpLog;

public class ErrorProcessor extends Executor implements Keyword
{
    public ErrorProcessor()
    {}

    @Override
    public void handle(String keyword, GrammarType type) throws Exception
    {
        WpLog.recordLog(LogLevelConstant.ERROR, "ERROR keyword");
        throw new Exception(String.format("Occur ERROR keyword in line %d", lineNumber));
    }
}
