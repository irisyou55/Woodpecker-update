package edu.ecnu.Woodpecker.executor.keyword;

import edu.ecnu.Woodpecker.constant.ConfigConstant;
import edu.ecnu.Woodpecker.constant.LogLevelConstant;
import edu.ecnu.Woodpecker.controller.MySQLOperation;
import edu.ecnu.Woodpecker.controller.TestController;
import edu.ecnu.Woodpecker.executor.Executor;
import edu.ecnu.Woodpecker.executor.GrammarType;
import edu.ecnu.Woodpecker.log.WpLog;
import edu.ecnu.Woodpecker.util.ProcessExecInfo;
import edu.ecnu.Woodpecker.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Chen Lixiang
 * @Email: lixiang3608@outlook.com
 * @Date: 2021/11/9
 */


public class MySQLTestFrameworkProcessor extends Executor implements Keyword {

    public MySQLTestFrameworkProcessor() {}

    private String host;

    private int port;

    private String dbName;

    private String userName;

    private String password;

    @Override
    public void handle(String keyword, GrammarType type) throws Exception {
        WpLog.recordLog(LogLevelConstant.INFO, "START MYSQL_TEST_FRAMEWORK!");
        // 仅支持 MySQL 数据库

        retrieveConnection();

        StringBuilder cmdBuilder = new StringBuilder();
        String[] args = Util.removeBlankElement(keyword.split("\\[|]"));
        String[] testCase = args[1].split(";");
        String dbName = testCase[0].substring(1, testCase[0].length()-1);

        cmdBuilder.append("cd mysql_tests && ").append(MySQLOperation.getMysqlTestPath())
                .append(" --host=").append(host)
                .append(" --port=").append(port)
                .append(" --user=").append(userName)
                .append(" --password=").append(password)
                .append(" --database=").append(dbName);
        TestController.reportGenerator.appendNewBenchmark("MySQL Test Framework", "");

        if (testCase.length > 2) {
            // 用户指定了特殊的测试用例
            int round = Integer.parseInt(testCase[2]);
            for (int i = 0; i < round; i++) {
                // 预创建 database
                Util.createOrDropDatabase(host, String.valueOf(port), dbName, userName, password, true);
                String specifyCase = testCase[1].substring(1, testCase[1].length() - 1);
                cmdBuilder.append(" --test-file=t/").append(specifyCase).append(".test")
                        .append(" --result-file=r/").append(specifyCase).append(".result");
                ProcessExecInfo info = Util.execCommand(cmdBuilder.toString());
                if (info.getExitValue() == 0) {
                    WpLog.recordLog(LogLevelConstant.INFO, specifyCase + " 通过 MySQL Test Framework 测试");
                } else {
                    WpLog.recordLog(LogLevelConstant.ERROR, specifyCase + " 测试失败, MySQL Test Framework 报告的测试信息:%s, 失败信息:%s",
                            info.getStdMsg(), info.getErrMsg());
                    TestController.reportGenerator.appendBenchmarkDetail(specifyCase + " 测试失败")
                    .appendBenchmarkDetail("```")
                    .appendBenchmarkDetail(info.getStdMsg())
                    .appendBenchmarkDetail(info.getErrMsg())
                    .appendBenchmarkDetail("```");
                    throw new Exception("MySQL Test Framework: " + specifyCase + " failed.");
                }
            }
        } else {
            File directory = new File("mysql_tests/t");
            String[] fs = directory.list();
            int round = Integer.parseInt(testCase[1]);
            for (int i = 0; i < round; i++) {
                // 预创建 database
                Util.createOrDropDatabase(host, String.valueOf(port), dbName, userName, password, true);
                List<String> failedCases = new ArrayList<>();
                for (String tFileName : fs) {
                    if (tFileName.endsWith(".test")) {
                        StringBuilder curCmdBuilder = new StringBuilder(cmdBuilder);
                        curCmdBuilder.append(" --test-file=").append("t/").append(tFileName)
                                .append(" --result-file=").append("r/").append(tFileName, 0, tFileName.length() - 4)
                                .append("result");
                        ProcessExecInfo info = Util.execCommand(curCmdBuilder.toString());
                        if (info.getExitValue() == 0) {
                            WpLog.recordLog(LogLevelConstant.INFO, tFileName + " 通过 MySQL Test Framework 测试");
                        } else {
                            WpLog.recordLog(LogLevelConstant.ERROR, tFileName + " 测试失败, MySQL Test Framework 报告的测试信息:%s, 失败信息:%s",
                                    info.getStdMsg(), info.getErrMsg());
                            TestController.reportGenerator.appendBenchmarkDetail(tFileName + " 测试失败")
                            .appendBenchmarkDetail("```")
                            .appendBenchmarkDetail(info.getStdMsg())
                            .appendBenchmarkDetail(info.getErrMsg())
                            .appendBenchmarkDetail("```");
                            failedCases.add(caseFileName);
                        }
                    }
                }

                // 所有 case 测试完成后对未通过对抛出异常
                if (!failedCases.isEmpty()) {
                    StringBuilder failedInfo = new StringBuilder();
                    failedInfo.append(failedCases.size()).append(" 个 case 未通过本次 MySQL Test Framework 测试:\n");
                    TestController.reportGenerator.appendBenchmarkDetail(failedCases.size() + " 个 case 未通过 MySQL Test Framework 测试:");
                    for (String next : failedCases) {
                        failedInfo.append(next).append(";");
                        TestController.reportGenerator.appendBenchmarkDetail("- " + next);
                    }
                    throw new Exception(failedInfo.toString());
                } else {
                    TestController.reportGenerator.appendBenchmarkDetail("所有 MySQL Test Framework 测试均已通过。");
                }
            }
        }
    }

    private void retrieveConnection() throws Exception {
        String database = TestController.getDatabase().getBrand();
        if (!database.equals(ConfigConstant.MYSQL)) {
            throw new Exception("MySQL Test Framework 暂不支持 " + database);
        }
        this.host = MySQLOperation.getIP();
        this.port = MySQLOperation.getPort();
        this.userName = MySQLOperation.getDatabaseUser();
        this.password = MySQLOperation.getDatabasePassword();
    }

}
