package edu.ecnu.Woodpecker.test;

/**
 * @author xiangzhaokun
 * @ClassName TestCode.java
 * @Description TODO
 * @createTime 2021年11月09日 22:09:00
 */
public class TestCode {
    public static void main(String[] args) {
        String Conn = "jdbc:mysql://localhost:4000/tpcc?useSSL=false&useServerPrepStmts=true&useConfigs=maxPerformance&rewriteBatchedStatements=true";
        String[] test = Conn.split("\\?");
        String[] test2 = test[0].split("/");
        for(String t : test){
            System.out.println(t);
        }
        for(String t : test2){
            System.out.println(t);


        }
    }
}
