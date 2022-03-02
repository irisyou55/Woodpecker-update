package edu.ecnu.Woodpecker.executor.keyword;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

import edu.ecnu.Woodpecker.constant.LogLevelConstant;
import edu.ecnu.Woodpecker.executor.Executor;
import edu.ecnu.Woodpecker.executor.GrammarType;
import edu.ecnu.Woodpecker.log.WpLog;


public class ParallelProcessor extends Executor implements Runnable,Keyword
{
    public static int threadnum = 0;
    public static CountDownLatch countDownLatch ;
    public static int containSqlNum = 0;
    
    /**
     * 记录并行块执行结束后的index值
     */
    public static int para_index = 0;
    
    public List<Integer> SQLvector = new ArrayList<>();
    public List<List<Integer>> paraBlockSQLvector = new ArrayList<>();


    public ParallelProcessor(){

    }
    public ParallelProcessor(List<Integer> singleBlockSQL,CountDownLatch countDownLatch2)
    {
        this.SQLvector = singleBlockSQL;
        countDownLatch = countDownLatch2;
    }
    
    
    @Override
    public void run()
    {
        // TODO Auto-generated method stub
        //int index = 0;
        for(int i=0; i<SQLvector.size(); i++)
        {
            //某个时刻只能有一个线程执行
            synchronized(this)
            {
                Executor.index = SQLvector.get(i);
                int paraIndex = SQLvector.get(i);
                //System.out.println("执行的SQL的index为："+Thread.currentThread().getName()+" "+paraIndex);

                try
                {
                    Thread.sleep(10);
                    Executor.assignStatementWithIndex(paraIndex);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        countDownLatch.countDown();
    }
    
    public void execute()
    {
        List<Integer> singleBlockSQL;
        CountDownLatch countDownLatch = new CountDownLatch(paraBlockSQLvector.size());
        for(int i=0; i < paraBlockSQLvector.size(); i++)
        {
            singleBlockSQL = paraBlockSQLvector.get(i);
            //System.out.println("singleBlockSQL为"+singleBlockSQL);

            new Thread(new ParallelProcessor(singleBlockSQL,countDownLatch)).start();
        }
        try
        {
            countDownLatch.await();
            Executor.index = para_index;
            paraBlockSQLvector = new ArrayList<>();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(String keyword, GrammarType type) throws Exception
    {
        WpLog.recordLog(LogLevelConstant.INFO, "PSQL:Parallel Execute: %s", keyword);
        switch (type)
        {
            case FIRST_GRAMMAR:
                int parall_begin_index = Executor.index;
                //System.out.println("开始并行的位置"+parall_begin_index);


                para_index = parall_begin_index+1;
                while(!getMIdresult(para_index).contains("end_parall"))
                {
                    if(getMIdresult(para_index).contains("mid_parall"))
                    {
                        paraBlockSQLvector.add(SQLvector);
                        SQLvector = new ArrayList<>();
                        para_index++;
                        continue;
                    }
                    SQLvector.add(para_index);
                    para_index++;
                }
                paraBlockSQLvector.add(SQLvector);
                SQLvector = new ArrayList<>();
                //System.out.println(paraBlockSQLvector);
                //去多线程并行的执行这些SQLBlock
                this.execute();
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
