package edu.ecnu.Woodpecker.constant;

import java.util.NoSuchElementException;
import java.util.stream.Stream;
/**
 * @description: Woodpecker支持的数据类型
 * 有int long boolean char float double Decimal String Timestamp List ResultSet IdealResultSet Connection
 */
public enum DataType
{
    INT(DataTypeConstant.INT_SHORT, DataTypeConstant.INT_FULL), 
    LONG(DataTypeConstant.LONG_SHORT, DataTypeConstant.LONG_FULL), 
    BOOLEAN(DataTypeConstant.BOOLEAN_SHORT, DataTypeConstant.BOOLEAN_FULL), 
    CHAR(DataTypeConstant.CHAR_SHORT, DataTypeConstant.CHAR_FULL), 
    FLOAT(DataTypeConstant.FLOAT_SHORT, DataTypeConstant.FLOAT_FULL), 
    DOUBLE(DataTypeConstant.DOUBLE_SHORT, DataTypeConstant.DOUBLE_FULL), 
    DECIMAL(DataTypeConstant.DECIMAL_SHORT, DataTypeConstant.DECIMAL_FULL),
    STRING(DataTypeConstant.STRING_SHORT, DataTypeConstant.STRING_FULL), 
    TIMESTAMP(DataTypeConstant.TIMESTAMP_SHORT, DataTypeConstant.TIMESTAMP_FULL),
    LIST(DataTypeConstant.LIST, null),
    RESULT_SET(DataTypeConstant.RESULT_SET, null),
    IDEAL_RESULT_SET(DataTypeConstant.IDEAL_RESULT_SET, null),
    CONNECTION(DataTypeConstant.CONNECTION, null);

    /**
     * The short name of data type, such as "int"
     */
    private String shortName = null;
    
    /**
     * The full name of data type, such as "java.lang.Integer"
     */
    private String fullName = null;

    private DataType(String shortName, String fullName)
    {
        this.shortName = shortName;
        this.fullName = fullName;
    }

    public String getShortName()
    {
        return shortName;
    }

    public String getFullName()
    {
        return fullName;
    }
    
    public String toString()
    {
        return shortName;
    }

    /**
     * 给定类型的shortName，比如int double，返回DataType类型的变量，因为DataType类把int和Integer放在了一起
     * 
     * @param shortName
     * @return
     * @throws NoSuchElementException
     */
    public static DataType of(String shortName) throws NoSuchElementException
    {
        Stream<DataType> stream = Stream.of(DataType.class.getEnumConstants());//用数组创建流
        return stream.filter(ele -> ele.getShortName().equals(shortName)).findAny().get();//返回具体的类型
    }
}
