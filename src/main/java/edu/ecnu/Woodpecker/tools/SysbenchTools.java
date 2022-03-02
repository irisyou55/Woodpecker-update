package edu.ecnu.Woodpecker.tools;

import edu.ecnu.Woodpecker.constant.LogLevelConstant;
import edu.ecnu.Woodpecker.controller.TestController;
import edu.ecnu.Woodpecker.log.WpLog;
import edu.ecnu.Woodpecker.util.Util;
import edu.ecnu.Woodpecker.log.RecordReport;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.*;

import com.jcraft.jsch.Session;
import net.sf.json.JSONObject;


public class SysbenchTools {
    public void seizeSysbenchOLTP(Session session, String keyword){
        String[] parts = keyword.split("\\[|;|]");
        for(int i=0; i< parts.length; i++)
            parts[i] = parts[i].trim();
        String sessionName = parts[1];
        String option = parts[2];
        String databaseType = parts[3];
        String test = parts[4];
        String database = parts[5];
        String host = parts[6];
        String port = parts[7];
        String usr = parts[8];
        String password = parts[9];
        String tables = parts[10];
        String table_size = parts[11];
        String threads = parts[12];
        String events = parts[13];
        String time = parts[14];
        String report_interval = parts[15];
        String outputfile = parts[16];
        int round = Integer.parseInt(parts[17]);

        String sysbenchCMD = "sysbench ";

        sysbenchCMD += " --test=" + test + " --" + databaseType + "-host=" + host
                + " --" + databaseType + "-port=" + port + " --" + databaseType + "-user=" + usr;
        if(password == null | password.equals(""))
            sysbenchCMD = sysbenchCMD;
        else
            sysbenchCMD += " --" + databaseType + "-password=" + password;

        sysbenchCMD += " --" +databaseType + "-db=" + database
                + " --tables=" + tables
                + " --table_size=" + table_size + " --threads=" + threads
                + " --events=" + events + " --time=" + time
                + " --report_interval=" + report_interval;
        switch (option){
            case "prepare":
                sysbenchCMD += " prepare";
                break;
            case "run":
                sysbenchCMD += " run";
                break;
            case "cleanup":
                sysbenchCMD += " cleanup";
                break;
            default:
                break;
        }
        if(option.equals("run")) {
            TestController.reportGenerator.appendNewBenchmark("sysbench OLTP 压测信息", "");
        }
        //循环执行round次
        for(int i=0; i<round; i++){
            int index = i+1;
            WpLog.recordLog(LogLevelConstant.INFO, "运行第"+index+"遍");
            if(option.equals("prepare"))
                CreateOrDropDatabase(host, port, database, usr, password,true);
            String result;
            if(session != null) {
                result = Util.execCmd(session, sysbenchCMD);
                WpLog.recordLog(LogLevelConstant.INFO, "远程调用sysbench命令: "+sysbenchCMD);
            }
            else {
                result = exec(sysbenchCMD);
                WpLog.recordLog(LogLevelConstant.INFO, "sysbench命令: "+sysbenchCMD);
            }

            if(option.equals("run")){
                TestController.reportGenerator.appendBenchmarkDetail("第 "+ index + " 次压测：")
                        .appendBenchmarkDetail("```");
                outFile("oltp", outputfile, result, index);
                TestController.reportGenerator.appendBenchmarkDetail("```");
            }
            if(option.equals("cleanup"))
                CreateOrDropDatabase(host, port, database, usr, password,false);
        }

    }

    public void seizeSysbenchCPU(Session session, String keyword){
        String[] parts = keyword.split("\\[|;|]");
        for(int i=0; i< parts.length; i++)
            parts[i] = parts[i].trim();
        String sessionName = parts[1];
        String prime = parts[2];
        String threads = parts[3];
        String time = parts[4];
        String events = parts[5];
        String outputfile = parts[6];
        int round = Integer.parseInt(parts[7]);

        String sysbenchCMD = "sysbench";

        sysbenchCMD += " cpu " + "--cpu-max-prime=" + prime + " --threads=" + threads
                    + " --time=" + time + " --events=" + events + " run";

        WpLog.recordLog(LogLevelConstant.INFO, "sysbench命令: "+sysbenchCMD);

        TestController.reportGenerator.appendNewBenchmark("sysbench CPU 压测信息", "");
        //循环执行round次
        for(int i=0; i<round; i++){
            int index = i+1;
            WpLog.recordLog(LogLevelConstant.INFO, "运行第"+index+"遍");
            String result;
            if(session != null)
                result = Util.execCmd(session, sysbenchCMD);
            else
                result = exec(sysbenchCMD);
            TestController.reportGenerator.appendBenchmarkDetail("第 "+ index + " 次压测：")
                    .appendBenchmarkDetail("```")
                    .appendBenchmarkDetail(result)
                    .appendBenchmarkDetail("```");
            outFile("cpu", outputfile, result, index);
        }

    }

    public void seizeSysbenchIO(Session session, String keyword){
        String[] parts = keyword.split("\\[|;|]");
        for(int i=0; i< parts.length; i++)
            parts[i] = parts[i].trim();
        String sessionName = parts[1];
        String file_num = parts[2];
        String file_block_size = parts[3];
        String file_total_size = parts[4];
        String file_test_mode = parts[5];
        String file_io_mode = parts[6];
        String file_async_backlog = parts[7];
        String file_fsync_freq = parts[8];
        String file_fsync_all = parts[9];
        String file_fsync_end = parts[10];
        String file_fsync_mode = parts[11];
        String file_merged_requests = parts[12];
        String file_rw_ratio = parts[13];
        String threads = parts[14];
        String time = parts[15];
        String events = parts[16];
        String outputfile = parts[17];
        int round = Integer.parseInt(parts[18]);

        String sysbenchCMD = "sysbench";

        sysbenchCMD += " fileio " + "--file-num=" + file_num + " --file-block-size=" + file_block_size
                + " --file-total-size=" + file_total_size + " --file-test-mode=" + file_test_mode
                + " --file-io-mode=" + file_io_mode + " --file-async-backlog=" + file_async_backlog
                + " --file-fsync-freq=" + file_fsync_freq + " --file-fsync-all=" + file_fsync_all
                + " --file-fsync-end=" + file_fsync_end + " --file-fsync-mode=" + file_fsync_mode
                + " --file-merged-requests=" + file_merged_requests + " --file-rw-ratio=" + file_rw_ratio
                + " --threads=" + threads + " --time=" + time + " --events=" + events;
        String sysbenchCMD_prepare = sysbenchCMD + " prepare";
        String sysbenchCMD_run = sysbenchCMD + " run";
        String sysbenchCMD_cleanup = sysbenchCMD + " cleanup";

        WpLog.recordLog(LogLevelConstant.INFO, "sysbench命令: "+sysbenchCMD);

        if(session != null)
            Util.execCmd(session, sysbenchCMD_prepare);
        else
            exec(sysbenchCMD_prepare);

        TestController.reportGenerator.appendNewBenchmark("sysbench IO 压测信息", "");
        //循环执行round次
        for(int i=0; i<round; i++){
            int index = i+1;
            WpLog.recordLog(LogLevelConstant.INFO, "运行第"+index+"遍");
            String result;
            if(session != null)
                result = Util.execCmd(session, sysbenchCMD_run);
            else
                result = exec(sysbenchCMD_run);
            TestController.reportGenerator.appendBenchmarkDetail("第 "+ index + " 次压测：")
                    .appendBenchmarkDetail("```")
                    .appendBenchmarkDetail(result)
                    .appendBenchmarkDetail("```");
            outFile("io", outputfile, result, index);
        }
        if(session != null)
            Util.execCmd(session, sysbenchCMD_cleanup);
        else
            exec(sysbenchCMD_cleanup);

    }

    public void seizeSysbenchMemory(Session session, String keyword){
        String[] parts = keyword.split("\\[|;|]");
        for(int i=0; i< parts.length; i++)
            parts[i] = parts[i].trim();
        String sessionName = parts[1];
        String memory_block_size = parts[2];
        String memory_total_size = parts[3];
        String memory_scope = parts[4];
        String memory_hugetlb = parts[5];
        String memory_oper = parts[6];
        String memory_access_mode = parts[7];
        String threads = parts[8];
        String time = parts[9];
        String events = parts[10];
        String outputfile = parts[11];
        int round = Integer.parseInt(parts[12]);

        String sysbenchCMD = "sysbench";

        sysbenchCMD += " memory " + " --memory-block-size=" + memory_block_size + " --memory-total-size=" + memory_total_size
                + " --memory-scope=" + memory_scope + " --memory-hugetlb=" + memory_hugetlb
                + " --memory-oper=" + memory_oper + " --memory-access-mode=" + memory_access_mode
                + " --threads=" + threads + " --time=" + time + " --events=" + events + " run";

        WpLog.recordLog(LogLevelConstant.INFO, "sysbench命令: "+sysbenchCMD);

        TestController.reportGenerator.appendNewBenchmark("sysbench 内存 压测信息", "");
        //循环执行round次
        for(int i=0; i<round; i++){
            int index = i+1;
            WpLog.recordLog(LogLevelConstant.INFO, "运行第"+index+"遍");
            String result;
            if(session != null)
                result = Util.execCmd(session, sysbenchCMD);
            else
                result = exec(sysbenchCMD);
            TestController.reportGenerator.appendBenchmarkDetail("第 "+ index + " 次压测：")
                    .appendBenchmarkDetail("```")
                    .appendBenchmarkDetail(result)
                    .appendBenchmarkDetail("```");
            outFile("memory", outputfile, result, index);
        }

    }

    public void seizeSysbenchThreads(Session session, String keyword){
        String[] parts = keyword.split("\\[|;|]");
        for(int i=0; i< parts.length; i++)
            parts[i] = parts[i].trim();
        String sessionName = parts[1];
        String thread_yields = parts[2];
        String thread_locks = parts[3];
        String threads = parts[4];
        String time = parts[5];
        String events = parts[6];
        String outputfile = parts[7];
        int round = Integer.parseInt(parts[8]);

        String sysbenchCMD = "sysbench";

        sysbenchCMD += " threads " + "--thread-yields=" + thread_yields + " --thread-locks=" + thread_locks
                + " --threads=" + threads + " --time=" + time + " --events=" + events + " run";

        WpLog.recordLog(LogLevelConstant.INFO, "sysbench命令: "+sysbenchCMD);

        TestController.reportGenerator.appendNewBenchmark("sysbench 线程 压测信息", "");

        //循环执行round次
        for(int i=0; i<round; i++){
            int index = i+1;
            WpLog.recordLog(LogLevelConstant.INFO, "运行第"+index+"遍");
            String result;
            if(session != null)
                result = Util.execCmd(session, sysbenchCMD);
            else
                result = exec(sysbenchCMD);
            TestController.reportGenerator.appendBenchmarkDetail("第 "+ index + " 次压测：")
                    .appendBenchmarkDetail("```")
                    .appendBenchmarkDetail(result)
                    .appendBenchmarkDetail("```");
            outFile("threads", outputfile, result, index);
        }

    }

    public void seizeSysbenchMutex(Session session, String keyword){
        String[] parts = keyword.split("\\[|;|]");
        for(int i=0; i< parts.length; i++)
            parts[i] = parts[i].trim();
        String sessionName = parts[1];
        String mutex_num = parts[2];
        String mutex_locks = parts[3];
        String mutex_loops = parts[4];
        String threads = parts[5];
        String time = parts[6];
        String events = parts[7];
        String outputfile = parts[8];
        int round = Integer.parseInt(parts[9]);

        String sysbenchCMD = "sysbench";

        sysbenchCMD += " mutex " + "--mutex-num=" + mutex_num + " --mutex-locks=" + mutex_locks
                + " --mutex-loops=" + mutex_loops + " --threads=" + threads + " --time=" + time + " --events=" + events
                + " run";

        WpLog.recordLog(LogLevelConstant.INFO, "sysbench命令: "+sysbenchCMD);

        TestController.reportGenerator.appendNewBenchmark("sysbench 互斥锁 压测信息", "");
        //循环执行round次
        for(int i=0; i<round; i++){
            int index = i+1;
            WpLog.recordLog(LogLevelConstant.INFO, "运行第"+index+"遍");
            String result;
            if(session != null)
                result = Util.execCmd(session, sysbenchCMD);
            else
                result = exec(sysbenchCMD);
            TestController.reportGenerator.appendBenchmarkDetail("第 "+ index + " 次压测：")
                    .appendBenchmarkDetail("```")
                    .appendBenchmarkDetail(result)
                    .appendBenchmarkDetail("```");
            outFile("mutex", outputfile, result, index);
        }
    }

    public static void outFile(String type, String filename, String result, int index) {
        //save results
        File file = new File("./sysbench_results");
        String newFileName;
        if (!file.exists()) {
            file.mkdir();
        }

        Calendar rightNow    =    Calendar.getInstance();
        Integer year = rightNow.get(Calendar.YEAR);
        Integer month = rightNow.get(Calendar.MONTH)+1; //第一个月从0开始，所以得到月份＋1
        Integer day = rightNow.get(rightNow.DAY_OF_MONTH);
        Integer hour24 = rightNow.get(rightNow.HOUR_OF_DAY);
        Integer minute = rightNow.get(rightNow.MINUTE);
        Integer second = rightNow.get(rightNow.SECOND);
        //用户指定输出结果文件名
        //转换格式后的文件名和原文件名一样，只是文件格式不一样
        if(!filename.equals("")) {
            newFileName = filename + "(" + index + ")" +year+"-"+month+"-"+day+"-"+hour24+"-"+minute+"-"+second;
            filename = file.getName() + File.separator + filename + "(" + index + ")"+
                    +year+"-"+month+"-"+day+"-"+hour24+"-"+minute+"-"+second+".txt";
        }
        else { //未指定，默认为type_result_当前时间
            filename = file.getName() + File.separator + type + "_result(" + index + ")_"
                    +year+"-"+month+"-"+day+"-"+hour24+"-"+minute+"-"+second+".txt";
            newFileName = type + "_result(" + index + ")_"
                    +year+"-"+month+"-"+day+"-"+hour24+"-"+minute+"-"+second;

        }
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename)))) {
            bw.write(result);
            bw.flush();
            if(type.equalsIgnoreCase("oltp")) {
                outCSV(filename,newFileName);
            }
            else
            {
                outJson(filename,newFileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void outJson(String filename,String newFileName) throws IOException {
        FileReader fileReader = new FileReader(filename);
        BufferedReader br = new BufferedReader(fileReader);
        Map<String, Map<String,String>> ResultMap = new HashMap<String,Map<String,String>>();
        Map<String,String> CPUSpeed = new HashMap<String,String>();
        Map<String,String> FileOpe = new HashMap<String,String>();
        Map<String,String> TotalOpe = new HashMap<String,String>();
        Map<String,String> Throughput = new HashMap<String,String>();
        Map<String,String> Memory = new HashMap<String,String>();
        Map<String,String> GenStatistics= new HashMap<String,String>();
        Map<String,String> Latency= new HashMap<String,String>();
        Map<String,String> Threads= new HashMap<String,String>();

        String line="";
        while ((line = br.readLine()) != null) {
            if (line.startsWith("CPU speed")) {//CPU keyword
                line = br.readLine();
                CPUSpeed.put(line.split(":")[0].trim(), line.split(":")[1].trim());
                ResultMap.put("CPU speed:", CPUSpeed);
            } else if (line.startsWith("File operations:")) {//IO keyword
                for (int i = 0; i < 3; i++) {
                    line = br.readLine();
                    FileOpe.put(line.split(":")[0].trim(), line.split(":")[1].trim());
                }
                ResultMap.put("File operations:", FileOpe);//IO keyword
            } else if (line.startsWith("Throughput:")) {
                for (int i = 0; i < 2; i++) {
                    line = br.readLine();
                    Throughput.put(line.split(":")[0].trim(), line.split(":")[1].trim());
                }
                ResultMap.put("Throughput:", Throughput);
            } else if (line.startsWith("Running memory")) {//memory keyword
                for (int i = 0; i < 4; i++) {
                    line = br.readLine();
                    Memory.put(line.split(":")[0].trim(), line.split(":")[1].trim());
                }
                ResultMap.put("Running memory speed test with the following options:", Memory);
            } else if (line.startsWith("Total operations:")) {//memory keyword
                TotalOpe.put(line.split(":")[0].trim(), line.split(":")[1].trim());
                ResultMap.put("Total operations:", TotalOpe);
            } else if (line.startsWith("General statistics")) {
                while ((line = br.readLine()).contains("total")) {
                    GenStatistics.put(line.split(":")[0].trim(), line.split(":")[1].trim());
                }
                ResultMap.put("General statistics:", GenStatistics);
            } else if (line.startsWith("Latency")) {
                for (int i = 0; i < 5; i++) {
                    line = br.readLine();
                    Latency.put(line.split(":")[0].trim(), line.split(":")[1].trim());
                }
                ResultMap.put("Latency (ms):", Latency);
            } else if (line.startsWith("Threads fairness:")) {
                for (int i = 0; i < 2; i++) {
                    line = br.readLine();
                    Threads.put(line.split(":")[0].trim(), line.split(":")[1].trim());
                }
                ResultMap.put("Threads fairness:", Threads);
            }
        }
        JSONObject jsonObject = JSONObject.fromObject(ResultMap);
        String jsonString = jsonObject.toString();
        RecordReport.CreateFileUtil.createJsonFile(jsonString, "./sysbench_results/sysbench_json_results", newFileName);
    }

    public static void outCSV(String filename,String newFileName) throws IOException {
        FileReader fileReader = new FileReader(filename);
        BufferedReader br = new BufferedReader(fileReader);

        File csvFile = new File("./sysbench_results/sysbench_oltp_results/" + newFileName +".csv"); // CSV数据文件
        if (!csvFile.getParentFile().exists()) { // 如果父目录不存在，创建父目录
            csvFile.getParentFile().mkdirs();
        }
        if (csvFile.exists()) { // 如果已存在,删除旧文件
            csvFile.delete();
        }
        csvFile.createNewFile();

        BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile, true));
        bw.write("time,thds,tps,qps,read_ope,write_ope,other_ope,lat(ms 95%),err/s,reconn/s"+"\n");
        String line="";
        String[] resultRows = null;
        while ((line = br.readLine()) != null) {
            if(line.startsWith("[")) {
                resultRows = line.replaceAll(" ",",").split(",");
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(resultRows[1].split("s")[0]);//time
                stringBuilder.append(",");
                stringBuilder.append(resultRows[4]);//thds
                stringBuilder.append(",");
                stringBuilder.append(resultRows[6]);//tps
                stringBuilder.append(",");
                stringBuilder.append(resultRows[8]);//qps
                stringBuilder.append(",");
                stringBuilder.append(resultRows[10].split("/")[0]);//read_ope
                stringBuilder.append(",");
                stringBuilder.append(resultRows[10].split("/")[1]);//write_ope
                stringBuilder.append(",");
                stringBuilder.append((resultRows[10].split("/")[2]).split("[)]")[0]);//other_ope
                stringBuilder.append(",");
                stringBuilder.append(resultRows[14]);//lat
                stringBuilder.append(",");
                stringBuilder.append(resultRows[16]);//err
                stringBuilder.append(",");
                stringBuilder.append(resultRows[18]);//reconn
                stringBuilder.append("\n");
                bw.write(stringBuilder.toString());
                bw.flush();
            }
            else{
                TestController.reportGenerator.appendBenchmarkDetail(line);
            }
        }
        bw.close();
    }

    public static String exec(String cmd) {
        try {
            String[] cmdA = { "/bin/sh", "-c", cmd };
            Process process = Runtime.getRuntime().exec(cmdA);
            LineNumberReader br = new LineNumberReader(new InputStreamReader(
                    process.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void CreateOrDropDatabase(String ip, String port, String dbName, String user, String password, Boolean flag){
        String url = null;
        String driver = null;
        if(TestController.getDatabase().getBrand().equalsIgnoreCase("mysql") || TestController.getDatabase().getBrand().equalsIgnoreCase("tidb")) {
            url = "jdbc:mysql://" + ip + ":" + port + "/?useSSL=false";
            driver = "com.mysql.jdbc.Driver";
        }
        else if(TestController.getDatabase().getBrand().equalsIgnoreCase("postgresql")){
            url = "jdbc:postgresql://" + ip + ":" + port + "/";
            driver = "org.postgresql.Driver";
        }

        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                WpLog.recordLog(LogLevelConstant.INFO, "数据库连接成功");
            } else {
                WpLog.recordLog(LogLevelConstant.INFO, "数据库连接失败");
            }
            Statement statement = conn.createStatement();
            String dropDB = "drop database if exists " + dbName;
            statement.execute(dropDB);

            if(flag) {
                String createDB = "create database " + dbName;
                statement.execute(createDB);
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
