package edu.ecnu.Woodpecker.executor.keyword;

import edu.ecnu.Woodpecker.executor.GrammarType;

/**
 * Keyword的处理，包括get_conn、SQL、get_stat等关键字
 * 使用状态模式
 *
 */
@FunctionalInterface
public interface Keyword
{
    /**
     * 处理各种测试定义语言的关键字，比如GET_CONN、SQL等
     * 
     * @param keyword The line of middle result exclude line number and grammar type
     * @param type grammar type
     * @throws Exception
     */
    public abstract void handle(String keyword, GrammarType type) throws Exception;

}
