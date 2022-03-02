package edu.ecnu.Woodpecker.executor;

/**
 * Grammar type of middle result
 * 文法类型
 *
 */
public enum GrammarType
{
    ZERO_GRAMMAR(0), FIRST_GRAMMAR(1), SECOND_GRAMMAR(2), THIRD_GRAMMAR(3), FOURTH_GRAMMAR(4), FIFTH_GRAMMAR(5), SIXTH_GRAMMAR(6);

    private final int value;

    GrammarType(int value)//Java枚举类型的默认构造方法是private
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
}
