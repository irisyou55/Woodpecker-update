package edu.ecnu.Woodpecker.executor.keyword;

import edu.ecnu.Woodpecker.constant.LogLevelConstant;
import edu.ecnu.Woodpecker.constant.SignConstant;
import edu.ecnu.Woodpecker.controller.TestController;
import edu.ecnu.Woodpecker.executor.Executor;
import edu.ecnu.Woodpecker.executor.GrammarType;
import edu.ecnu.Woodpecker.log.WpLog;
import edu.ecnu.Woodpecker.tools.BenchmarkSQLTools;
import edu.ecnu.Woodpecker.util.Util;

/**
 * @author xiangzhaokun
 * @ClassName BenchmarkSQLProcessor.java
 * @Description TODO
 * @createTime 2021年11月09日 20:44:00
 */
public class BenchmarkSQLProcessor extends Executor implements Keyword{
    public BenchmarkSQLProcessor(){

    }

    @Override
    public void handle(String keyword, GrammarType type) throws Exception{
        WpLog.recordLog(LogLevelConstant.INFO, "BenchmarkSQL: %s", keyword);
        switch (type)
        {
            case FIRST_GRAMMAR:
                handleFirstGrammar(keyword);
                break;
            case SECOND_GRAMMAR:
                // Index 0 is variable name, 1 is keyword
                String[] keywordParts = Util.removeBlankElement(keyword.split(SignConstant.ASSIGNMENT_STR, 2));
                handleSecondGrammar(keywordParts[0], keywordParts[1]);
                break;
            case THIRD_GRAMMAR:
                // Index 0 is data type and variable name, 1 is keyword
                keywordParts = Util.removeBlankElement(keyword.split(SignConstant.ASSIGNMENT_STR, 2));
                // Index 0 is data type, 1 is variable name
                String[] decVar = Util.removeBlankElement(keywordParts[0].split("\\s"));
                handleThirdGrammar(decVar[0], decVar[1], keywordParts[1]);
                break;
            default:
                throw new Exception("Grammar error");
        }
    }
    /**
     *
     * @param keyword Starts with "benchmarksql["
     */
    private void handleFirstGrammar(String keyword) throws Exception {
        String[] parts = Util.removeBlankElement(keyword.split("\\[|;|]"));
        if(Integer.parseInt(parts[4])!=0 && Integer.parseInt(parts[5])!=0){
            throw new Exception("runTxnsPerTerminal和runMins必须有一个为0");
        }
        BenchmarkSQLTools benchmarkSQLTools = new BenchmarkSQLTools(Integer.parseInt(parts[1]),Integer.parseInt(parts[2]),
                Integer.parseInt(parts[3]),Integer.parseInt(parts[4]),Integer.parseInt(parts[5]),Integer.parseInt(parts[6]));
        benchmarkSQLTools.setConfig();
        try {
            benchmarkSQLTools.loadData();
            WpLog.recordLog(LogLevelConstant.INFO, "开始自动执行"+parts[parts.length-1]+"次");
            int round = Integer.parseInt(parts[parts.length-1]);
            String config = null;

            if(TestController.getDatabase().getBrand().equalsIgnoreCase("mysql") || TestController.getDatabase().getBrand().equalsIgnoreCase("tidb")){
                config = "props.mysql";
            }
            else if(TestController.getDatabase().getBrand().equalsIgnoreCase("postgresql")){
                config = "props.pg";
            }
            if(config==null){
                throw new Exception("未配置数据库，无法使用benchmarkSQL,请在Woodpecker.conf文件中配置数据库");
            }
            for(int i=0;i<round;i++){
                benchmarkSQLTools.startBenchmarkSQL(config);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *
     * @param variableName
     * @param keyword Starts with "benchmarksql["
     * @throws Exception
     */
    private void handleSecondGrammar(String variableName, String keyword) throws Exception
    {

    }

    /**
     *
     * @param dataType The data type of variable
     * @param variableName
     * @param keyword Starts with "benchmarksql["
     * @throws Exception
     */
    private void handleThirdGrammar(String dataType, String variableName, String keyword) throws Exception
    {

    }
}
