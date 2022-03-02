package edu.ecnu.Woodpecker.sql;
import edu.ecnu.Woodpecker.constant.SQLConstant;
/**
 * @author Youshuhong
 * @create 2021/11/20 下午4:12
 */
public enum TransactionOperator
{
    START(SQLConstant.START),COMMIT(SQLConstant.COMMIT),ROLLBACK(SQLConstant.ROLLBACK);

    private String operator = null;

    private TransactionOperator(String operator)
    {
        this.operator = operator;
    }

    public String getOperator()
    {
        return operator;
    }

    public String toString()
    {
        return operator;
    }
}
