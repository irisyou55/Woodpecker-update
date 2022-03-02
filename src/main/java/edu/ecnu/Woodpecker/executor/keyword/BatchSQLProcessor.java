package edu.ecnu.Woodpecker.executor.keyword;

import edu.ecnu.Woodpecker.constant.LogLevelConstant;
import edu.ecnu.Woodpecker.executor.Executor;
import edu.ecnu.Woodpecker.executor.GrammarType;
import edu.ecnu.Woodpecker.log.WpLog;
import edu.ecnu.Woodpecker.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author : Youshuhong
 * @create : 2022/1/10 13:53
 */
public class BatchSQLProcessor extends Executor implements Runnable,Keyword{

    public static CountDownLatch countDownLatch ;
    public static int execution_time = 0;

    public static int count = 0;
    /**
     * 记录并行块执行结束后的index值
     */
    public static int batch_index = 0;

    public static List<Integer> SQLvector = new ArrayList<>();

    public BatchSQLProcessor(){

    }
    public BatchSQLProcessor(CountDownLatch countDownLatch2)
    {
        countDownLatch = countDownLatch2;
    }

    @Override
    public void run()
    {
        // TODO Auto-generated method stub
        //int index = 0;
       // System.out.println(Thread.currentThread().getName() + " invoked...");
 //       System.out.println(SQLvector);
        long startTime = System.currentTimeMillis();
        long endTime = System.currentTimeMillis();
        while((endTime - startTime)/1000 < execution_time){//每个线程都运行execution_time
            for (int i = 0; i < SQLvector.size(); i++) {
                //某个时刻只能有一个线程执行
                synchronized(Executor.class)
                {
                    Executor.index = SQLvector.get(i);
                    int batchIndex = SQLvector.get(i);
                    WpLog.recordLog(LogLevelConstant.INFO,"执行的SQL的index为："+Thread.currentThread().getName()+" "+batchIndex);
                    try {
                        Executor.assignStatementWithIndex(batchIndex);
                        endTime = System.currentTimeMillis();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            count++;
        }
        countDownLatch.countDown();
        WpLog.recordLog(LogLevelConstant.INFO,execution_time+"秒期间内一共执行了"+count+"次");
    }

    public void execute(int thread_num)
    {
        CountDownLatch countDownLatch = new CountDownLatch(thread_num);

        for (int i=0; i<thread_num;i++){
            new Thread(new BatchSQLProcessor(countDownLatch)).start();
        }
        try
        {
            countDownLatch.await();
            Executor.index = batch_index;
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(String keyword, GrammarType type) throws Exception
    {
        String[] parts = Util.removeBlankElement(keyword.split("\\[|;|]"));
        execution_time = Integer.parseInt(parts[1]);
        Integer thread_num = Integer.parseInt(parts[2]);
        WpLog.recordLog(LogLevelConstant.INFO, "Batch SQL Execute: %s", keyword);
        switch (type)
        {
            case FIRST_GRAMMAR:
                int batch_begin_index = Executor.index;

                batch_index = batch_begin_index+1;
                while(!getMIdresult(batch_index).contains("end_batch_sql"))
                {
                    SQLvector.add(batch_index);
                    batch_index++;
                }

                this.execute(thread_num);
                break;
            case SECOND_GRAMMAR:
                //TODO
                break;
            case THIRD_GRAMMAR:
                //TODO
                break;
            default:
                throw new Exception("Grammar error");
        }
    }

}
