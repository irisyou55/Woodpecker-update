/*
 * @Author: SilvaXiang
 * @Date: 2021-03-01 20:33:33
 * @LastEditTime: 2021-03-03 16:12:15
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: /Woodpecker-209/src/main/java/edu/ecnu/woodpecker/executor/SQLKeywordOperator.java
 */
package edu.ecnu.Woodpecker.executor;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

/**
 *  The operator of SQL keyword
 *  包括读操作和写操作
 *
 */
public enum SQLKeywordOperator
{
    READ(1), READ_ERROR(-1),
    WRITE(2), WRITE_ERROR(-2),
    EXPLAIN(3), EXPLAIN_ERROR(-3);

    /**
     * The id of operator
     */
    private int value;

    private SQLKeywordOperator(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    /**
     * Returns an enum constant of the specified value
     *
     * @param value
     * @return
     * @throws NoSuchElementException
     */
    public static SQLKeywordOperator valueOf(int value) throws NoSuchElementException
    {
        Stream<SQLKeywordOperator> stream = Stream.of(SQLKeywordOperator.class.getEnumConstants());
        return stream.filter(ele -> ele.getValue() == value).findAny().get();
    }
}
